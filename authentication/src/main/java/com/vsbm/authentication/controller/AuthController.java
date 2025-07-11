package com.vsbm.authentication.controller;

import com.vsbm.authentication.service.KeycloakUserService;
import com.vsbm.authentication.dto.LoginRequest;
import com.vsbm.authentication.dto.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final KeycloakUserService keycloakUserService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        keycloakUserService.registerUser(request);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Map tokenResponse = keycloakUserService.login(loginRequest);
        return ResponseEntity.ok(tokenResponse);
    }
}
