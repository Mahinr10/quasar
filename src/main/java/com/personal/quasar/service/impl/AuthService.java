package com.personal.quasar.service.impl;

import com.personal.quasar.dao.UserRepository;
import com.personal.quasar.model.dto.AuthRequestDTO;
import com.personal.quasar.model.dto.AuthResponseDTO;
import com.personal.quasar.model.entity.User;
import com.personal.quasar.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

@Service
public class AuthService {
    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;
    public AuthResponseDTO registerAndLogin(AuthRequestDTO request) {
        if(userService.checkUserWithEmailExist(request.getUserName())) {
            throw new RuntimeException("User already exists with email: " + request.getUserName());
        }
        saveUser(request);
        return login(request);
    }

    public AuthResponseDTO login(AuthRequestDTO request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUserName(), request.getPassword()
        ));
        var result = new AuthResponseDTO();
        var userDTO = userService.loadUserByUsername(request.getUserName());
        result.setAccessToken(jwtUtil.generateAccessToken(userDTO));
        result.setRefreshToken(jwtUtil.generateRefreshToken(userDTO.getEmail()));
        result.setExpiredAfter("3600s");
        return result;
    }

    public AuthResponseDTO refresh(@PathVariable("token") String refreshToken) {
        if(!jwtUtil.validateToken(refreshToken)) {
            throw new RuntimeException("Invalid refresh token");
        }
        var email = jwtUtil.extractEmail(refreshToken);
        var userDTO = userService.loadUserByUsername(email);
        var result = new AuthResponseDTO();
        result.setAccessToken(jwtUtil.generateAccessToken(userDTO));
        result.setRefreshToken(jwtUtil.generateRefreshToken(userDTO.getEmail()));
        result.setExpiredAfter("3600s");
        return result;
    }
    private void saveUser(AuthRequestDTO request) {
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        userService.saveUser(request.getUserName(), encodedPassword);
    }
}
