package com.personal.quasar.controller;

import com.personal.quasar.model.dto.AuthRequestDTO;
import com.personal.quasar.model.dto.AuthResponseDTO;
import com.personal.quasar.service.AuthService;
import com.personal.quasar.service.UserService;
import com.personal.quasar.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody AuthRequestDTO request) {
        return ResponseEntity.ok(authService.registerAndLogin(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody AuthRequestDTO request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @GetMapping("/refresh")
    public ResponseEntity<AuthResponseDTO> refresh(@PathVariable("token") String refreshToken) {
        return ResponseEntity.ok(authService.refresh(refreshToken));
    }
}
