package com.personal.quasar.model.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldNameConstants
public class UserDTO extends BaseDTO {
    @Size(max = 50)
    private String firstName;
    @Size(max = 50)
    private String lastName;
    @Size(max = 100)
    private String email;
}
