package com.FlickerDomain.flicker.controller;

import com.FlickerDomain.flicker.dto.RegisterRequest;
import com.FlickerDomain.flicker.service.UserService;
import com.FlickerDomain.flicker.dto.LoginRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

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

    @PostMapping("/user-login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        String captchaResponse = loginRequest.getCaptchaResponse();

        System.out.println("Email: " + email);

        // Validate CAPTCHA
        if (!validateCaptcha(captchaResponse)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Captcha validation failed");
        }

        // Login logic
        boolean isAuthenticated = userService.authenticateByEmail(email, password);
        if (isAuthenticated) {
            return ResponseEntity.ok("Login successful");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    private boolean validateCaptcha(String captchaResponse) {
        String secretKey = "6LffRYYqAAAAAJEVVPDGDtu_WPrNaVdSqAfsW1Ij"; // Twój sekretny klucz
        String verifyUrl = "https://www.google.com/recaptcha/api/siteverify";

        WebClient webClient = WebClient.create();

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("secret", secretKey);
        requestBody.put("response", captchaResponse);

        try {
            CaptchaResponse response = webClient.post()
                    .uri(verifyUrl)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(CaptchaResponse.class)
                    .block();

            System.out.println("Captcha Response: " + response);
            return response != null && response.isSuccess();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static class CaptchaResponse {
        private boolean success;

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        @Override
        public String toString() {
            return "CaptchaResponse{" +
                    "success=" + success +
                    '}';
        }
    }
}