package com.personal.quasar.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserProfileService implements UserProfileFacade {
    @Autowired
    UserService userService;
    @Override
    public String getActiveUserId() {
        var userId = SecurityContextHolder.getContext().getAuthentication().getName();
        if(userId == null) {
            return "System";
        }
        return userId;
    }
}
