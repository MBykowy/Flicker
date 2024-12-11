package com.FlickerDomain.flicker.controller;

import com.FlickerDomain.flicker.dto.RegisterRequest;
import com.FlickerDomain.flicker.dto.LoginRequest;
import com.FlickerDomain.flicker.model.User;
import com.FlickerDomain.flicker.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.ExchangeStrategies;

import java.util.Collections;
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
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response) {
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
            // Set session attribute after successful login
            HttpSession session = request.getSession(true);  // Creates a session if none exists
            session.setAttribute("user", email); // Store the user's email in the session

            // Create a cookie with the user's email
            Cookie emailCookie = new Cookie("email", email);
            //emailCookie.setHttpOnly(true); // Zabezpiecza ciasteczko przed dostępem z JavaScript
            emailCookie.setSecure(false); // Tylko przez HTTPS
            emailCookie.setMaxAge(7 * 24 * 60 * 60); // 7 dni
            emailCookie.setPath("/"); // Dostępność w całej witrynie
            response.addCookie(emailCookie);

            return ResponseEntity.ok("Login successful");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @GetMapping("/check-auth")
    public ResponseEntity<Boolean> checkAuth(HttpServletRequest request) {
        HttpSession session = request.getSession(false);  // Retrieve the existing session, if any
        boolean isAuthenticated = (session != null && session.getAttribute("user") != null);
        return ResponseEntity.ok(isAuthenticated);
    }

    @PostMapping("/update-picture")
    public ResponseEntity<?> updatePicture(@RequestParam String email, @RequestBody Map<String, String> request) {
        String pictureUrl = request.get("picture");
        userService.updateUserPicture(email, pictureUrl);
        return ResponseEntity.ok(Collections.singletonMap("success", true));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);  // Retrieve the session if it exists
        if (session != null) {
            session.invalidate();  // Invalidate the session to log out the user
        }
        // Remove the email cookie
        Cookie emailCookie = new Cookie("email", null);
        emailCookie.setHttpOnly(true);
        emailCookie.setSecure(true);
        emailCookie.setMaxAge(0); // Delete cookie
        emailCookie.setPath("/");
        response.addCookie(emailCookie);

        return ResponseEntity.ok("Logged out successfully");
    }

    @GetMapping("/user-details")
    public ResponseEntity<User> getUserDetails(@RequestParam String email) {
        User user = userService.getUserByEmail(email);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/update-username")
    public ResponseEntity<?> updateUsername(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String username = request.get("username");
        userService.updateUserUsername(email, username);
        return ResponseEntity.ok(Collections.singletonMap("success", true));
    }

    @PostMapping("/update-bio")
    public ResponseEntity<?> updateBio(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String bio = request.get("bio");
        userService.updateUserBio(email, bio);
        return ResponseEntity.ok(Collections.singletonMap("success", true));
    }


    // Walidacja CAPTCHA
    public boolean validateCaptcha(String captchaResponse) {
        String secretKey = "6LffRYYqAAAAAJEVVPDGDtu_WPrNaVdSqAfsW1Ij"; // Your secret key
        String verifyUrl = "https://www.google.com/recaptcha/api/siteverify";

        // Konfiguracja WebClient z większym limitem na debugowanie dużych odpowiedzi
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
