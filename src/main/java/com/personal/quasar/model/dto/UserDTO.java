package com.personal.quasar.model.dto;

import com.personal.quasar.model.enums.UserRole;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

import java.util.TimeZone;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldNameConstants
public class UserDTO extends AuditDTO {
    @Size(max = 50)
    private String firstName;
    @Size(max = 50)
    private String lastName;
    @Size(max = 100)
    private String email;
    @NotNull
    private UserRole userRole = UserRole.USER;
    private String TimeZoneId;
}
