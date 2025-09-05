package com.personal.quasar.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.personal.quasar.filter.JwtAuthenticationFilter;
import com.personal.quasar.model.dto.AuthRequestDTO;
import com.personal.quasar.model.dto.AuthResponseDTO;
import com.personal.quasar.service.AuthService;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthControllerTest extends ControllerTest {
    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    JwtAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    public void beforeEach() throws ServletException, IOException {
        var mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
        setMockMvc(mockMvc);
    }

    @Test
    public void registerTest() throws Exception {
        when(authService.registerAndLogin(any(AuthRequestDTO.class))).thenReturn(getAuthDTO());
        getMockMvc().perform(post("/api/v1/auth/register")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(getAuthRequestDTO())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("accessToken"))
                .andExpect(jsonPath("$.refreshToken").value("refreshToken"))
                .andExpect(jsonPath("$.expiredAfter").value("3600s"));
    }

    private AuthRequestDTO getAuthRequestDTO() {
        var authRequestDTO = new AuthRequestDTO();
        authRequestDTO.setPassword("password123");
        authRequestDTO.setUserName(getUserName());
        return authRequestDTO;
    }
    private AuthResponseDTO getAuthDTO() {
        var authDTO = new AuthResponseDTO();
        authDTO.setAccessToken("accessToken");
        authDTO.setRefreshToken("refreshToken");
        authDTO.setExpiredAfter("3600s");
        return authDTO;
    }
}
