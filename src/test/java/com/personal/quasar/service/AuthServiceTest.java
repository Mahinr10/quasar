package com.personal.quasar.service;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.personal.quasar.UnitTest;
import com.personal.quasar.model.dto.AuthRequestDTO;
import com.personal.quasar.model.dto.UserDTO;
import com.personal.quasar.service.impl.AuthService;
import com.personal.quasar.service.impl.UserService;
import com.personal.quasar.util.JwtUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AuthServiceTest extends UnitTest {
    @Mock
    UserService userService;
    @Mock
    JwtUtil jwtUtil;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    AuthenticationManager authenticationManager;
    @InjectMocks
    AuthService authService;

    @Test
    public void loginTest() {
        var email = "user@test.com";
        var password = "password";
        var accessToken = "accessToken";
        var refreshToken = "refreshToken";
        var authRequest = new AuthRequestDTO();
        authRequest.setUserName(email);
        authRequest.setPassword(password);

        when(authenticationManager.authenticate(any())).thenReturn(null);
        when(userService.loadUserByUsername(email)).thenReturn(getUserDTO());
        when(jwtUtil.generateAccessToken(getUserDTO())).thenReturn(accessToken);
        when(jwtUtil.generateRefreshToken(email)).thenReturn(refreshToken);

        var result = authService.login(authRequest);

        verify(authenticationManager, times(1)).authenticate(any());
        Assertions.assertEquals(accessToken, result.getAccessToken());
        Assertions.assertEquals(refreshToken, result.getRefreshToken());
    }

    @Test
    public void registerTest() {
        var email = "user@test.com";
        var password = "password";
        var encodedPassword = "encodedPassword";
        var accessToken = "accessToken";
        var refreshToken = "refreshToken";
        when(userService.saveUser(email, password)).thenReturn(getUserDTO());
        when(userService.loadUserByUsername(email)).thenReturn(getUserDTO());
        when(authenticationManager.authenticate(any())).thenReturn(null);
        when(jwtUtil.generateAccessToken(getUserDTO())).thenReturn(accessToken);
        when(jwtUtil.generateRefreshToken(email)).thenReturn(refreshToken);
        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);
        var authRequestDTO = new AuthRequestDTO();
        authRequestDTO.setUserName(email);
        authRequestDTO.setPassword(password);
        var result = authService.registerAndLogin(authRequestDTO);
        verify(authenticationManager, times(1)).authenticate(any());
        Assertions.assertEquals(accessToken, result.getAccessToken());
        Assertions.assertEquals(refreshToken, result.getRefreshToken());
    }

    @Test
    public void refreshTest() {
        var refreshToken = "refreshToken_1";
        var accessToken = "accessToken";
        var newRefreshToken = "refreshToken_2";
        var email = "user@test.com";
        when(jwtUtil.extractEmail(refreshToken)).thenReturn(email);
        when(userService.loadUserByUsername(email)).thenReturn(getUserDTO());
        when(jwtUtil.validateToken(refreshToken)).thenReturn(true);
        when(jwtUtil.generateAccessToken(getUserDTO())).thenReturn(accessToken);
        when(jwtUtil.generateRefreshToken(email)).thenReturn(newRefreshToken);
        var result = authService.refresh(refreshToken);
        Assertions.assertEquals(accessToken, result.getAccessToken());
        Assertions.assertEquals(newRefreshToken, result.getRefreshToken());
    }

    private UserDTO getUserDTO() {
        var userDTO = new UserDTO();
        userDTO.setEmail("user@test.com");
        userDTO.setId("abc");
        return userDTO;
    }
}
