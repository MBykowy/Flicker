package com.FlickerDomain.flicker.controller;

import com.FlickerDomain.flicker.service.UserService;
import com.FlickerDomain.flicker.dto.RegisterRequest;             // For RegisterRequest DTO
import com.FlickerDomain.flicker.dto.LoginRequest;                // For LoginRequest DTO
import com.FlickerDomain.flicker.dto.AuthResponse;                // For AuthResponse DTO

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;


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
    public ResponseEntity<String> login(@RequestParam String email,
                                        @RequestParam String password,
                                        @RequestParam("g-recaptcha-response") String captchaResponse) {
        // Walidacja CAPTCHA
        if (!validateCaptcha(captchaResponse)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Captcha validation failed");
        }

        // Logika logowania
        boolean isAuthenticated = userService.authenticateByEmail(email, password);
        if (isAuthenticated) {
            return ResponseEntity.ok("Login successful");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    private boolean validateCaptcha(String captchaResponse) {
        String secretKey = "6LffRYYqAAAAAJEVVPDGDtu_WPrNaVdSqAfsW1Ij"; // Wstaw swój secret key
        String verifyUrl = "https://www.google.com/recaptcha/api/siteverify";

        RestTemplate restTemplate = new RestTemplate();
        String requestUrl = verifyUrl + "?secret=" + secretKey + "&response=" + captchaResponse;

        try {
            CaptchaResponse response = restTemplate.postForObject(requestUrl, null, CaptchaResponse.class);
            return response != null && response.isSuccess();
        } catch (Exception e) {
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
    }


}
