package com.FlickerDomain.flicker.controller;

import com.FlickerDomain.flicker.dto.RegisterRequest;
import com.FlickerDomain.flicker.dto.LoginRequest;
import com.FlickerDomain.flicker.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.ExchangeStrategies;

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

    public boolean validateCaptcha(String captchaResponse) {
        String secretKey = "6LffRYYqAAAAAJEVVPDGDtu_WPrNaVdSqAfsW1Ij"; // Twój sekretny klucz
        String verifyUrl = "https://www.google.com/recaptcha/api/siteverify";

        // Konfiguracja WebClient z większym limitem dla debugowania dużych odpowiedzi
        WebClient webClient = WebClient.builder()
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024))
                        .build())
                .build();

        try {
            String response = webClient.post()
                    .uri(verifyUrl)
                    .bodyValue("secret=" + secretKey + "&response=" + captchaResponse)
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            System.out.println("Captcha API Response: " + response);

            // Parsowanie odpowiedzi
            return response != null && response.contains("\"success\": true");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
