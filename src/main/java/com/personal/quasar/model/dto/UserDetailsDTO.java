package com.personal.quasar.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

@NoArgsConstructor
@Data
public class UserDetailsDTO implements UserDetails {
    private String username;
    private String password;
    private List<UserAuthorityDTO> authorities;
}
