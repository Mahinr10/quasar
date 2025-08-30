package com.personal.quasar.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserDTO extends BaseDTO {
    private String name;
    private String email;
}
