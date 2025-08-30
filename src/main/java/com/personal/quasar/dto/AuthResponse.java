package com.personal.quasar.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthResponse {
    String accessToken;
    String refreshToken;
    String expiredAfter;
}
