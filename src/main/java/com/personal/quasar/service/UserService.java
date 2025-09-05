package com.personal.quasar.service;

import com.personal.quasar.dao.UserRepository;
import com.personal.quasar.exception.ImmutableFieldModificationException;
import com.personal.quasar.exception.InvalidFieldException;
import com.personal.quasar.exception.ResourceDoesNotExistException;
import com.personal.quasar.model.dto.UserDTO;
import com.personal.quasar.model.entity.User;
import com.personal.quasar.model.mapper.UserMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.List;

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
                .orElseThrow(() -> new ResourceDoesNotExistException("user", id));
        return userMapper.entityToDTO(user);
    }

    public Boolean checkUserWithEmailExist(String email) {
        return userRepository.existsByEmailAndIsDeletedFalse(email);
    }
    public UserDTO update(String id, UserDTO updatedUser)
            throws ImmutableFieldModificationException, ResourceDoesNotExistException {
        User existingUser = userRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(
                    () -> new ResourceDoesNotExistException("user", id)
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
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
        return userMapper.entityToDTO(user);
    }

    public UserDTO saveUser(
            @NotNull(message = "Email cannot be null")
            @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Provided invalid email")
            String email,

            @NotNull(message = "password cannot be null")
            String encryptedPassword
    ) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(encryptedPassword);
        auditService.populateAuditFields(user);
        return userMapper.entityToDTO(userRepository.save(user));
    }
}