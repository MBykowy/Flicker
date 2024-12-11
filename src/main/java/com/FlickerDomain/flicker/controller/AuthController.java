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
 * Kontroler odpowiedzialny za obsługę operacji związanych z autentykacją użytkowników.
 * Obsługuje rejestrację użytkowników, logowanie, wylogowywanie, aktualizowanie zdjęcia
 * profilowego, sprawdzanie autentykacji oraz weryfikację CAPTCHA.
 */
@Controller
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    /**
     * Konstruktor kontrolera.
     *
     * @param userService serwis odpowiedzialny za operacje związane z użytkownikami.
     */
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Rejestruje nowego użytkownika.
     *
     * @param request obiekt zawierający dane rejestracyjne użytkownika.
     * @return odpowiedź HTTP z informacją o sukcesie rejestracji.
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest request) {
        userService.register(request);
        return ResponseEntity.ok("User registered successfully");
    }

    /**
     * Loguje użytkownika na podstawie danych logowania i weryfikacji CAPTCHA.
     *
     * @param loginRequest obiekt zawierający dane logowania użytkownika.
     * @param request obiekt żądania HTTP (do zarządzania sesją).
     * @param response obiekt odpowiedzi HTTP (do ustawiania ciasteczek).
     * @return odpowiedź HTTP z wynikiem logowania.
     */
    @PostMapping("/user-login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        String captchaResponse = loginRequest.getCaptchaResponse();

        System.out.println("Email: " + email);

        // Walidacja CAPTCHA
        if (!validateCaptcha(captchaResponse)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Captcha validation failed");
        }

        // Logowanie użytkownika
        boolean isAuthenticated = userService.authenticateByEmail(email, password);
        if (isAuthenticated) {
            // Ustawienie atrybutu sesji po udanym logowaniu
            HttpSession session = request.getSession(true);  // Tworzy sesję, jeśli nie istnieje
            session.setAttribute("user", email); // Przechowywanie emaila użytkownika w sesji

            // Tworzenie ciasteczka z emailem użytkownika
            Cookie emailCookie = new Cookie("email", email);
            emailCookie.setSecure(false); // Ciasteczko dostępne tylko przez HTTPS
            emailCookie.setMaxAge(7 * 24 * 60 * 60); // Ciasteczko ważne przez 7 dni
            emailCookie.setPath("/"); // Dostępność w całej witrynie
            response.addCookie(emailCookie);

            return ResponseEntity.ok("Login successful");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    /**
     * Sprawdza, czy użytkownik jest zalogowany na podstawie sesji.
     *
     * @param request obiekt żądania HTTP (do odczytu sesji).
     * @return odpowiedź HTTP z wynikiem autentykacji.
     */
    @GetMapping("/check-auth")
    public ResponseEntity<Boolean> checkAuth(HttpServletRequest request) {
        HttpSession session = request.getSession(false);  // Pobiera istniejącą sesję, jeśli istnieje
        boolean isAuthenticated = (session != null && session.getAttribute("user") != null);
        return ResponseEntity.ok(isAuthenticated);
    }

    /**
     * Aktualizuje zdjęcie profilowe użytkownika.
     *
     * @param email adres email użytkownika, którego zdjęcie profilowe ma zostać zaktualizowane.
     * @param request mapa zawierająca nowe URL zdjęcia profilowego.
     * @return odpowiedź HTTP z wynikiem aktualizacji.
     */
    @PostMapping("/update-picture")
    public ResponseEntity<?> updatePicture(@RequestParam String email, @RequestBody Map<String, String> request) {
        String pictureUrl = request.get("picture");
        userService.updateUserPicture(email, pictureUrl);
        return ResponseEntity.ok(Collections.singletonMap("success", true));
    }

    /**
     * Wylogowuje użytkownika, usuwając sesję i ciasteczko emaila.
     *
     * @param request obiekt żądania HTTP (do zarządzania sesją).
     * @param response obiekt odpowiedzi HTTP (do usuwania ciasteczek).
     * @return odpowiedź HTTP z komunikatem o wylogowaniu.
     */
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);  // Pobiera sesję, jeśli istnieje
        if (session != null) {
            session.invalidate();  // Unieważnia sesję
        }
        // Usuwanie ciasteczka email
        Cookie emailCookie = new Cookie("email", null);
        emailCookie.setHttpOnly(true);
        emailCookie.setSecure(true);
        emailCookie.setMaxAge(0); // Usuwa ciasteczko
        emailCookie.setPath("/");
        response.addCookie(emailCookie);

        return ResponseEntity.ok("Logged out successfully");
    }

    /**
     * Pobiera szczegóły użytkownika na podstawie adresu email.
     *
     * @param email adres email użytkownika, którego szczegóły mają zostać pobrane.
     * @return odpowiedź HTTP z danymi użytkownika.
     */
    @GetMapping("/user-details")
    public ResponseEntity<User> getUserDetails(@RequestParam String email) {
        User user = userService.getUserByEmail(email);
        return ResponseEntity.ok(user);
    }

    /**
     * Weryfikuje odpowiedź CAPTCHA za pomocą Google reCAPTCHA API.
     *
     * @param captchaResponse odpowiedź CAPTCHA od użytkownika.
     * @return true, jeśli CAPTCHA została poprawnie zweryfikowana, w przeciwnym razie false.
     */
    public boolean validateCaptcha(String captchaResponse) {
        String secretKey = "6LffRYYqAAAAAJEVVPDGDtu_WPrNaVdSqAfsW1Ij"; // Klucz tajny do reCAPTCHA
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
