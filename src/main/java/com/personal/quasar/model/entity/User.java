package com.personal.quasar.model.entity;

import com.personal.quasar.model.enums.UserRole;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.TimeZone;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document("user")
public class User extends AuditEntity {
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private UserRole userRole = UserRole.USER;
}
