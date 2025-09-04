package com.personal.quasar.service;

import com.personal.quasar.dao.UserRepository;
import com.personal.quasar.exception.ImmutableFieldModificationException;
import com.personal.quasar.exception.ResourceDoesNotExistException;
import com.personal.quasar.model.dto.UserDTO;
import com.personal.quasar.model.entity.User;
import com.personal.quasar.model.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    public String create(User user) {
        user.setCreatedBy("system"); // Replace with actual user if available
        user.setCreatedDate(new Date());
        user.setLastModifiedBy("system"); // Replace with actual user if available
        user.setLastModifiedDate(new Date());
        user.setId(UUID.randomUUID().toString());
        user.setIsDeleted(false); // Ensure the user is not marked as deleted
        return userRepository.save(user).getId();
    }

    public UserDTO get(String id) throws ResourceDoesNotExistException {
        var user = userRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceDoesNotExistException("user", id));
        return userMapper.entityToDTO(user);
    }

    public User getByEmail(String email) {
        return userRepository.findByEmailAndIsDeletedFalse(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    public Boolean checkUserWithEmailExist(String email) {
        return userRepository.existsByEmailAndIsDeletedFalse(email);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
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

        existingUser.setFirstName(updatedUser.getFirstName());
        existingUser.setLastName(updatedUser.getLastName());
        existingUser.setLastModifiedBy("system");
        existingUser.setLastModifiedDate(new Date());

        var savedUser = userRepository.save(existingUser);
        return userMapper.entityToDTO(savedUser);
    }

    private boolean equalsIgnoringAuditFields(User user, UserDTO userDTO) {
        return user.getFirstName().equals(userDTO.getFirstName()) &&
                user.getLastName().equals(userDTO.getLastName()) &&
                user.getEmail().equals(userDTO.getEmail());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmailAndIsDeletedFalse(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
    }
}