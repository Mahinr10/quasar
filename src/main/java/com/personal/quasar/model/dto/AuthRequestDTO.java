package com.personal.quasar.model.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthRequestDTO {
    @NotNull
    @Size(min = 10, max = 100)
    private String userName;

    @NotNull
    @Size(min = 8, max = 100)
    private String password;
}
