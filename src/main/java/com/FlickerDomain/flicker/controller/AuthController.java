package com.FlickerDomain.flicker.controller;

import com.FlickerDomain.flicker.service.UserService;
import com.FlickerDomain.flicker.dto.RegisterRequest;             // For RegisterRequest DTO
import com.FlickerDomain.flicker.dto.LoginRequest;                // For LoginRequest DTO
import com.FlickerDomain.flicker.dto.AuthResponse;                // For AuthResponse DTO

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest request) {
        userService.register(request);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest request) {
        String token = userService.authenticate(request);
        return ResponseEntity.ok(new AuthResponse(token));
    }
}
