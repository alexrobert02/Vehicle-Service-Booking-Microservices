package com.vsbm.authentication.controller;

import com.vsbm.authentication.service.KeycloakUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final KeycloakUserService keycloakUserService;

    @GetMapping("/role/{role}")
    public ResponseEntity<List<Map>> getUsersByRole(@PathVariable String role) {
        List<Map> users = keycloakUserService.getUsersByRole(role);
        return ResponseEntity.ok(users);
    }
}
