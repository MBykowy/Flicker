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

/**
 * Controller for handling authentication-related requests.
 */
@Controller
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    /**
     * Constructor for AuthController.
     *
     * @param userService the user service to be used for user operations
     */
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Registers a new user.
     *
     * @param request the registration request containing user details
     * @return a response entity indicating the result of the registration
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest request) {
        userService.register(request);
        return ResponseEntity.ok("User registered successfully");
    }

    /**
     * Logs in a user.
     *
     * @param loginRequest the login request containing user credentials
     * @param request the HTTP servlet request
     * @param response the HTTP servlet response
     * @return a response entity indicating the result of the login attempt
     */
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
            //emailCookie.setHttpOnly(true); // Protects the cookie from JavaScript access
            emailCookie.setSecure(false); // Only through HTTPS
            emailCookie.setMaxAge(7 * 24 * 60 * 60); // 7 days
            emailCookie.setPath("/"); // Available throughout the site
            response.addCookie(emailCookie);

            return ResponseEntity.ok("Login successful");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    /**
     * Checks if a user is authenticated.
     *
     * @param request the HTTP servlet request
     * @return a response entity indicating whether the user is authenticated
     */
    @GetMapping("/check-auth")
    public ResponseEntity<Boolean> checkAuth(HttpServletRequest request) {
        HttpSession session = request.getSession(false);  // Retrieve the existing session, if any
        boolean isAuthenticated = (session != null && session.getAttribute("user") != null);
        return ResponseEntity.ok(isAuthenticated);
    }

    /**
     * Updates the user's profile picture.
     *
     * @param email the user's email
     * @param request a map containing the new picture URL
     * @return a response entity indicating the result of the update
     */
    @PostMapping("/update-picture")
    public ResponseEntity<?> updatePicture(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String pictureUrl = request.get("picture");
        userService.updateUserPicture(email, pictureUrl);
        return ResponseEntity.ok(Collections.singletonMap("success", true));
    }


    /**
     * Logs out a user.
     *
     * @param request the HTTP servlet request
     * @param response the HTTP servlet response
     * @return a response entity indicating the result of the logout attempt
     */
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

    /**
     * Retrieves user details.
     *
     * @param email the user's email
     * @return a response entity containing the user details
     */
    @GetMapping("/user-details")
    public ResponseEntity<User> getUserDetails(@RequestParam String email) {
        User user = userService.getUserByEmail(email);
        return ResponseEntity.ok(user);
    }

    /**
     * Updates the user's username.
     *
     * @param request a map containing the user's email and new username
     * @return a response entity indicating the result of the update
     */
    @PostMapping("/update-username")
    public ResponseEntity<?> updateUsername(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String username = request.get("username");
        userService.updateUserUsername(email, username);
        return ResponseEntity.ok(Collections.singletonMap("success", true));
    }

    /**
     * Updates the user's bio.
     *
     * @param request a map containing the user's email and new bio
     * @return a response entity indicating the result of the update
     */
    @PostMapping("/update-bio")
    public ResponseEntity<?> updateBio(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String bio = request.get("bio");
        userService.updateUserBio(email, bio);
        return ResponseEntity.ok(Collections.singletonMap("success", true));
    }

    /**
     * Validates the CAPTCHA response.
     *
     * @param captchaResponse the CAPTCHA response from the client
     * @return true if the CAPTCHA is valid, false otherwise
     */
    public boolean validateCaptcha(String captchaResponse) {
        String secretKey = "6LffRYYqAAAAAJEVVPDGDtu_WPrNaVdSqAfsW1Ij"; // Your secret key
        String verifyUrl = "https://www.google.com/recaptcha/api/siteverify";

        // Configure WebClient with a larger limit for debugging large responses
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

            // Parse the response
            return response != null && response.contains("\"success\": true");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}