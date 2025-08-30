package com.personal.quasar.controller;

import com.personal.quasar.model.dto.AuthResponse;
import com.personal.quasar.model.entity.User;
import com.personal.quasar.service.UserService;
import com.personal.quasar.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody User user) {
        if(userService.checkUserWithEmailExist(user.getEmail())) {
            throw new RuntimeException("User already exists with email: " + user.getEmail());
        }
        var pass = user.getPassword();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.create(user);
        user.setPassword(pass);
        return login(user);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody User user) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                user.getEmail(), user.getPassword()
        ));
        var res = new AuthResponse();
        res.setAccessToken(jwtUtil.generateAccessToken(user.getEmail()));
        res.setRefreshToken(jwtUtil.generateRefreshToken(user.getEmail()));
        res.setExpiredAfter("3600s");
        return ResponseEntity.ok(res);
    }

    @GetMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@PathVariable("token") String refreshToken) {
        if(!jwtUtil.validateToken(refreshToken)) {
            throw new RuntimeException("Invalid refresh token");
        }
        var email = jwtUtil.extractEmail(refreshToken);
        var res = new AuthResponse();
        res.setAccessToken(jwtUtil.generateAccessToken(email));
        res.setRefreshToken(jwtUtil.generateRefreshToken(email));
        res.setExpiredAfter("3600s");
        return ResponseEntity.ok(res);
    }

    @GetMapping("")
    public ResponseEntity<List<User>> getAll() {
        //TODO: Configure it env specific
        return ResponseEntity.ok(userService.getAllUsers());
    }

}
