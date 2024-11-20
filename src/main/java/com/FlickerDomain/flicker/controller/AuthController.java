package com.FlickerDomain.flicker.controller;

import com.FlickerDomain.flicker.service.UserService;
import com.FlickerDomain.flicker.dto.RegisterRequest;             // For RegisterRequest DTO
import com.FlickerDomain.flicker.dto.LoginRequest;                // For LoginRequest DTO
import com.FlickerDomain.flicker.dto.AuthResponse;                // For AuthResponse DTO

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
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

//    @PostMapping("/login")
//    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
//        boolean isAuthenticated = userService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());
//        if (isAuthenticated) {
//            return ResponseEntity.ok("Login successful");
//        } else {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
//        }
//    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";  // Zwraca stronę login.html
    }

    @PostMapping("/user-login")
    public ResponseEntity<String> login(@RequestParam String email, @RequestParam String password) {
        boolean isAuthenticated = userService.authenticateByEmail(email, password);
        if (isAuthenticated) {
            return ResponseEntity.ok("Login successful");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }


}
