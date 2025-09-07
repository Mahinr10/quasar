package com.personal.quasar.service.impl;

import com.personal.quasar.common.exception.ResourceDoesNotExistException;
import com.personal.quasar.dao.UserRepository;
import com.personal.quasar.model.dto.UserAuthorityDTO;
import com.personal.quasar.model.dto.UserDetailsDTO;
import com.personal.quasar.model.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsAdapter implements UserDetailsService {
    @Autowired
    UserService userService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            User user = userService.getUserByEmail(username);
            UserDetailsDTO userDetails = new UserDetailsDTO();
            userDetails.setUsername(user.getEmail());
            userDetails.setPassword(user.getPassword());
            var authority = new UserAuthorityDTO();
            authority.setAuthority(user.getUserRole().name());
            userDetails.setAuthorities(java.util.List.of(authority));
            return userDetails;
        } catch (ResourceDoesNotExistException ex) {
            throw new UsernameNotFoundException(ex.getMessage(), ex);
        }
    }
}
