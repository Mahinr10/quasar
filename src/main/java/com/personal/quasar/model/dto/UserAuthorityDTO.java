package com.personal.quasar.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@NoArgsConstructor
@Data
public class UserAuthorityDTO implements GrantedAuthority {
    String authority;
}
