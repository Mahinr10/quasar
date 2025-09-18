package com.personal.quasar.service.impl;

import com.personal.quasar.model.dto.UserAuthorityDTO;
import com.personal.quasar.model.enums.UserRole;
import com.personal.quasar.service.UserProfileFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserProfileService implements UserProfileFacade {
    @Override
    public String getActiveUserId() {
        var userId = SecurityContextHolder.getContext().getAuthentication().getName();
        if(userId == null) {
            return "System";
        }
        return userId;
    }

    @Override
    public Boolean isUsersWithRole(UserRole role) {
        var authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        var authority = new UserAuthorityDTO();
        authority.setAuthority(role.name());
        return authorities.contains(authority);
    }
}
