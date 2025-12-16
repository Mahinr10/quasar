package com.personal.quasar.service.impl;

import com.personal.quasar.common.exception.InvalidFieldException;
import com.personal.quasar.common.exception.UnprivilegedToModificationException;
import com.personal.quasar.dao.UserRepository;
import com.personal.quasar.common.exception.ImmutableFieldModificationException;
import com.personal.quasar.common.exception.ResourceDoesNotExistException;
import com.personal.quasar.model.dto.UserDTO;
import com.personal.quasar.model.entity.User;
import com.personal.quasar.model.enums.UserRole;
import com.personal.quasar.model.mapper.UserMapper;
import com.personal.quasar.service.AuditService;
import com.personal.quasar.service.TimeZoneService;
import com.personal.quasar.service.UserProfileFacade;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.List;
import java.util.Objects;

import static com.personal.quasar.util.RepositoryErrorMessageConstants.USER_ENTITY;
import static com.personal.quasar.util.RepositoryErrorMessageConstants.USER_NOT_FOUND_WITH_EMAIL;
import static com.personal.quasar.util.ValidationConstants.EMAIL_REGEX;
import static com.personal.quasar.util.ValidationErrorMessages.EMAIL_CANNOT_BE_NULL;
import static com.personal.quasar.util.ValidationErrorMessages.NON_ADMIN_USER_ROLE_MODIFICATION_ERROR_MESSAGE;
import static com.personal.quasar.util.ValidationErrorMessages.PASSWORD_CANNOT_BE_NULL;
import static com.personal.quasar.util.ValidationErrorMessages.PROVIDED_INVALID_EMAIL;
import static com.personal.quasar.util.ValidationErrorMessages.PROVIDED_INVALID_TIMEZONE;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuditService auditService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserProfileFacade userProfileFacade;

    @Autowired
    private TimeZoneService timeZoneService;

    public UserDTO get(String id) throws ResourceDoesNotExistException {
        var user = userRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceDoesNotExistException(USER_ENTITY, id));
        return userMapper.entityToDTO(user);
    }

    public UserDTO get() throws ResourceDoesNotExistException {
        log.info("get method started");
        var email = userProfileFacade.getActiveUserId();
        var user = userRepository.findByEmailAndIsDeletedFalse(email)
                .orElseThrow(() -> new ResourceDoesNotExistException(USER_ENTITY, email));
        return userMapper.entityToDTO(user);
    }

    public Boolean checkUserWithEmailExist(String email) {
        return userRepository.existsByEmailAndIsDeletedFalse(email);
    }
    public UserDTO update(String id, UserDTO updatedUser)
            throws ImmutableFieldModificationException, ResourceDoesNotExistException,
            UnprivilegedToModificationException, InvalidFieldException {
        log.info("update method started with id - {} and payload - {}", id, updatedUser);
        User existingUser = userRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(
                    () -> new ResourceDoesNotExistException(USER_ENTITY, id)
                );
        if (!existingUser.getEmail().equals(updatedUser.getEmail())) {
            throw new ImmutableFieldModificationException(List.of(UserDTO.Fields.email));
        }
        validateTimeZone(existingUser, updatedUser);
        validateUpdatingUserRole(existingUser, updatedUser);

        if (equalsIgnoringAuditFields(existingUser, updatedUser)) {
            updatedUser.setId(id);
            return updatedUser;
        }

        User newUser = userMapper.dtoToEntity(updatedUser);
        newUser.setEmail(existingUser.getEmail());
        newUser.setId(id);
        newUser.setCreatedBy(existingUser.getCreatedBy());
        newUser.setCreatedDate(existingUser.getCreatedDate());
        newUser.setPassword(existingUser.getPassword());
        auditService.populateAuditFields(newUser);

        var savedUser = userRepository.save(newUser);
        return userMapper.entityToDTO(savedUser);
    }

    private void validateTimeZone(User existingUser, UserDTO newUser) throws InvalidFieldException {
        if(existingUser.getTimeZoneId() == null && newUser.getTimeZoneId() == null) {
            return;
        }
        if(newUser.getTimeZoneId() == null) {
            throw new InvalidFieldException(PROVIDED_INVALID_TIMEZONE);
        }
        if(!timeZoneService.isValidTImeZone(newUser.getTimeZoneId())) {
            throw new InvalidFieldException(PROVIDED_INVALID_TIMEZONE);
        }
    }

    private void validateUpdatingUserRole(User existingUser, UserDTO newUser) throws UnprivilegedToModificationException {
        if(!existingUser.getUserRole().equals(newUser.getUserRole()) && !userProfileFacade.isUsersWithRole(UserRole.ADMIN)) {
            throw new UnprivilegedToModificationException(NON_ADMIN_USER_ROLE_MODIFICATION_ERROR_MESSAGE);
        }
    }

    private boolean equalsIgnoringAuditFields(User user, UserDTO userDTO) {
        return Objects.equals(user.getFirstName(), userDTO.getFirstName()) &&
                Objects.equals(user.getLastName(), userDTO.getLastName()) &&
                Objects.equals(user.getEmail(), userDTO.getEmail()) &&
                Objects.equals(user.getUserRole(), userDTO.getUserRole()) &&
                Objects.equals(user.getTimeZoneId(), userDTO.getTimeZoneId());
    }

    public UserDTO loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmailAndIsDeletedFalse(username)
                .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND_WITH_EMAIL + " " + username));
        return userMapper.entityToDTO(user);
    }

    User getUserByEmail(String username) throws ResourceDoesNotExistException {
        return userRepository.findByEmailAndIsDeletedFalse(username)
                .orElseThrow(() -> new ResourceDoesNotExistException(USER_NOT_FOUND_WITH_EMAIL + " " + username));
    }

    public UserDTO saveUser(
            @NotNull(message = EMAIL_CANNOT_BE_NULL)
            @Pattern(regexp = EMAIL_REGEX, message = PROVIDED_INVALID_EMAIL)
            String email,

            @NotNull(message = PASSWORD_CANNOT_BE_NULL)
            String encryptedPassword
    ) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(encryptedPassword);
        auditService.populateAuditFields(user);
        return userMapper.entityToDTO(userRepository.save(user));
    }
}