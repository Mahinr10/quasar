package com.personal.quasar.service.impl;

import com.personal.quasar.dao.UserRepository;
import com.personal.quasar.common.exception.ImmutableFieldModificationException;
import com.personal.quasar.common.exception.ResourceDoesNotExistException;
import com.personal.quasar.model.dto.UserDTO;
import com.personal.quasar.model.entity.User;
import com.personal.quasar.model.mapper.UserMapper;
import com.personal.quasar.service.AuditService;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.List;

import static com.personal.quasar.util.RepositoryErrorMessageConstants.USER_ENTITY;
import static com.personal.quasar.util.RepositoryErrorMessageConstants.USER_NOT_FOUND_WITH_EMAIL;
import static com.personal.quasar.util.ValidationConstants.EMAIL_REGEX;
import static com.personal.quasar.util.ValidationErrorMessages.EMAIL_CANNOT_BE_NULL;
import static com.personal.quasar.util.ValidationErrorMessages.PASSWORD_CANNOT_BE_NULL;
import static com.personal.quasar.util.ValidationErrorMessages.PROVIDED_INVALID_EMAIL;

@Service
@Validated
@ApplicationScope
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuditService auditService;

    @Autowired
    private UserMapper userMapper;

    public UserDTO get(String id) throws ResourceDoesNotExistException {
        var user = userRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceDoesNotExistException(USER_ENTITY, id));
        return userMapper.entityToDTO(user);
    }

    public Boolean checkUserWithEmailExist(String email) {
        return userRepository.existsByEmailAndIsDeletedFalse(email);
    }
    public UserDTO update(String id, UserDTO updatedUser)
            throws ImmutableFieldModificationException, ResourceDoesNotExistException {
        User existingUser = userRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(
                    () -> new ResourceDoesNotExistException(USER_ENTITY, id)
                );
        if (!existingUser.getEmail().equals(updatedUser.getEmail())) {
            throw new ImmutableFieldModificationException(List.of(UserDTO.Fields.email));
        }
        if (equalsIgnoringAuditFields(existingUser, updatedUser)) {
            return updatedUser;
        }

        auditService.populateAuditFields(existingUser);

        var savedUser = userRepository.save(existingUser);
        return userMapper.entityToDTO(savedUser);
    }

    private boolean equalsIgnoringAuditFields(User user, UserDTO userDTO) {
        return user.getFirstName().equals(userDTO.getFirstName()) &&
                user.getLastName().equals(userDTO.getLastName()) &&
                user.getEmail().equals(userDTO.getEmail());
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