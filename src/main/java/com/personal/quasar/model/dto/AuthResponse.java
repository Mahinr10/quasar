package com.personal.quasar.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthResponse {
    String accessToken;
    String refreshToken;
    String expiredAfter;
}
