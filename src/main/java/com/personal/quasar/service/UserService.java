package com.personal.quasar.service;

import com.personal.quasar.dao.UserRepository;
import com.personal.quasar.model.entity.User;
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

    public String create(User user) {
        // Fill audit fields
        user.setCreatedBy("system"); // Replace with actual user if available
        user.setCreatedDate(new Date());
        user.setLastModifiedBy("system"); // Replace with actual user if available
        user.setLastModifiedDate(new Date());
        user.setId(UUID.randomUUID().toString());
        user.setIsDeleted(false); // Ensure the user is not marked as deleted
        return userRepository.save(user).getId();
    }

    public User get(String id) {
        return userRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
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

    public void delete(String id) {
        var user = get(id);
        user.setIsDeleted(true);
        update(id, user);
    }

    public String update(String id, User updatedUser) {
        User existingUser = get(id);

        if (equalsIgnoringAuditFields(updatedUser, existingUser)) {
            return existingUser.getId();
        }

        existingUser.setName(updatedUser.getName());
        existingUser.setEmail(updatedUser.getEmail());
        // Update other fields as needed
        existingUser.setLastModifiedBy("system");
        existingUser.setLastModifiedDate(new Date());

        return userRepository.save(existingUser).getId();
    }

    private boolean equalsIgnoringAuditFields(User user1, User user2) {
        return user1.getName().equals(user2.getName()) &&
                user1.getEmail().equals(user2.getEmail());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmailAndIsDeletedFalse(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
    }
}