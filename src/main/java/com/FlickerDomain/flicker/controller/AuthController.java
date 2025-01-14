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
 * Kontroler obsługujący żądania związane z autentykacją użytkownika,
 * takie jak rejestracja, logowanie, wylogowywanie oraz aktualizacja danych użytkownika.
 * Dodatkowo obsługuje również walidację CAPTCHA podczas procesu logowania.
 */
@Controller
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    /**
     * Konstruktor kontrolera.
     *
     * @param userService serwis użytkownika, który będzie używany do operacji na użytkownikach
     */
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Rejestruje nowego użytkownika.
     *
     * @param request żądanie rejestracyjne zawierające dane użytkownika
     * @return odpowiedź wskazującą rezultat procesu rejestracji
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest request) {
        userService.register(request);
        return ResponseEntity.ok("User registered successfully");
    }

    /**
     * Loguje użytkownika.
     * Sprawdza dane logowania, waliduje odpowiedź CAPTCHA i ustawia sesję oraz ciasteczko.
     *
     * @param loginRequest dane logowania zawierające dane użytkownika
     * @param request żądanie HTTP
     * @param response odpowiedź HTTP
     * @return odpowiedź wskazującą rezultat logowania
     */
    @PostMapping("/user-login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        String captchaResponse = loginRequest.getCaptchaResponse();

        // Walidacja CAPTCHA
        if (!validateCaptcha(captchaResponse)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Captcha validation failed");
        }

        // Logowanie użytkownika
        boolean isAuthenticated = userService.authenticateByEmail(email, password);
        if (isAuthenticated) {
            // Ustawienie atrybutu sesji po udanym logowaniu
            HttpSession session = request.getSession(true);  // Tworzy sesję, jeśli nie istnieje
            session.setAttribute("user", email); // Przechowywanie adresu e-mail w sesji

            // Utworzenie ciasteczka z e-mailem użytkownika
            Cookie emailCookie = new Cookie("email", email);
            emailCookie.setSecure(false); // Zezwolenie na połączenie bez HTTPS
            emailCookie.setMaxAge(7 * 24 * 60 * 60); // Ciasteczko ważne przez 7 dni
            emailCookie.setPath("/"); // Dostępne w całej aplikacji
            response.addCookie(emailCookie);

            return ResponseEntity.ok("Login successful");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    /**
     * Sprawdza, czy użytkownik jest zalogowany.
     *
     * @param request żądanie HTTP
     * @return odpowiedź wskazującą, czy użytkownik jest zalogowany
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
     * @param request mapa zawierająca e-mail użytkownika i nowe URL zdjęcia
     * @return odpowiedź wskazującą rezultat aktualizacji
     */
    @PostMapping("/update-picture")
    public ResponseEntity<?> updatePicture(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String pictureUrl = request.get("picture");
        userService.updateUserPicture(email, pictureUrl);
        return ResponseEntity.ok(Collections.singletonMap("success", true));
    }

    /**
     * Wylogowuje użytkownika.
     * Usuwa sesję oraz ciasteczko przechowujące e-mail.
     *
     * @param request żądanie HTTP
     * @param response odpowiedź HTTP
     * @return odpowiedź wskazującą rezultat wylogowania
     */
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);  // Pobiera istniejącą sesję, jeśli istnieje
        if (session != null) {
            session.invalidate();  // Inwalidacja sesji, co skutkuje wylogowaniem użytkownika
        }
        // Usunięcie ciasteczka z e-mailem użytkownika
        Cookie emailCookie = new Cookie("email", null);
        emailCookie.setMaxAge(0); // Ustalenie czasu życia ciasteczka na 0, co powoduje jego usunięcie
        emailCookie.setPath("/");
        response.addCookie(emailCookie);

        return ResponseEntity.ok("Logged out successfully");
    }

    /**
     * Pobiera dane użytkownika.
     *
     * @param email adres e-mail użytkownika
     * @return odpowiedź zawierająca dane użytkownika
     */
    @GetMapping("/user-details")
    public ResponseEntity<User> getUserDetails(@RequestParam String email) {
        User user = userService.getUserByEmail(email);
        return ResponseEntity.ok(user);
    }

    /**
     * Aktualizuje nazwę użytkownika.
     *
     * @param request mapa zawierająca e-mail użytkownika i nową nazwę użytkownika
     * @return odpowiedź wskazującą rezultat aktualizacji
     */
    @PostMapping("/update-username")
    public ResponseEntity<?> updateUsername(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String username = request.get("username");
        userService.updateUserUsername(email, username);
        return ResponseEntity.ok(Collections.singletonMap("success", true));
    }

    /**
     * Aktualizuje biografię użytkownika.
     *
     * @param request mapa zawierająca e-mail użytkownika i nową biografię
     * @return odpowiedź wskazującą rezultat aktualizacji
     */
    @PostMapping("/update-bio")
    public ResponseEntity<?> updateBio(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String bio = request.get("bio");
        userService.updateUserBio(email, bio);
        return ResponseEntity.ok(Collections.singletonMap("success", true));
    }

    /**
     * Waliduje odpowiedź CAPTCHA.
     *
     * @param captchaResponse odpowiedź CAPTCHA przesłana przez klienta
     * @return true, jeśli CAPTCHA jest poprawna, false w przeciwnym razie
     */
    public boolean validateCaptcha(String captchaResponse) {
        String secretKey = "6LffRYYqAAAAAJEVVPDGDtu_WPrNaVdSqAfsW1Ij"; // Twój klucz tajny
        String verifyUrl = "https://www.google.com/recaptcha/api/siteverify";

        // Konfiguracja WebClient z większym limitem pamięci na duże odpowiedzi
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

            // Analiza odpowiedzi z CAPTCHA
            return response != null && response.contains("\"success\": true");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
