package com.personal.quasar.service;

import com.personal.quasar.model.dto.UserDTO;
import com.personal.quasar.model.enums.UserRole;

public interface UserProfileFacade {
    String getActiveUserId();

    Boolean isUsersWithRole(UserRole role);
}
