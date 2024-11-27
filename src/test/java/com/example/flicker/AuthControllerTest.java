package com.example.flicker;


import com.FlickerDomain.flicker.controller.AuthController;
import com.FlickerDomain.flicker.dto.RegisterRequest;
import com.FlickerDomain.flicker.dto.LoginRequest;
import com.FlickerDomain.flicker.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class AuthControllerTest {

    @InjectMocks
    private AuthController authController;  // Kontroler, który testujemy

    @Mock
    private UserService userService;        // Zamockowany serwis użytkownika

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);  // Inicjalizacja mocków przed każdym testem
    }

    // Testowanie rejestracji użytkownika
    @Test
    void testRegisterUser() {
        // Przygotowanie danych wejściowych
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("testuser");
        registerRequest.setEmail("testuser@example.com");
        registerRequest.setPassword("password");

        // Mockowanie rejestracji użytkownika
        doNothing().when(userService).register(registerRequest);

        // Wywołanie metody rejestracji
        ResponseEntity<?> response = authController.registerUser(registerRequest);

        // Sprawdzenie, czy odpowiedź jest zgodna z oczekiwaniami
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User registered successfully", response.getBody());

        // Weryfikacja, czy metoda register została wywołana raz
        verify(userService, times(1)).register(registerRequest);
    }

    // Testowanie udanego logowania
    @Test
    void testLoginSuccess() {
        // Przygotowanie danych wejściowych do logowania
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("testuser@example.com");
        loginRequest.setPassword("password");
        loginRequest.setCaptchaResponse("valid-captcha-response");

        // Mockowanie odpowiedzi z metody authenticateByEmail, że dane są poprawne
        when(userService.authenticateByEmail(anyString(), anyString())).thenReturn(true);

        // Tworzymy "szpiega" na kontrolerze, aby mockować wywołanie private validateCaptcha
        AuthController spyController = spy(authController);
        doReturn(true).when(spyController).validateCaptcha(anyString());  // Mockowanie odpowiedzi CAPTCHA na true

        // Wywołanie metody logowania
        ResponseEntity<String> response = spyController.login(loginRequest);

        // Sprawdzamy, czy odpowiedź to 200 OK oraz komunikat o sukcesie
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Login successful", response.getBody());

        // Weryfikacja, czy metoda authenticateByEmail została wywołana raz
        verify(userService, times(1)).authenticateByEmail(loginRequest.getEmail(), loginRequest.getPassword());
    }

    // Testowanie logowania, kiedy CAPTCHA jest niepoprawne
    @Test
    void testLoginCaptchaFailed() {
        // Przygotowanie danych wejściowych do logowania
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("testuser@example.com");
        loginRequest.setPassword("password");
        loginRequest.setCaptchaResponse("invalid-captcha-response");

        // Tworzymy "szpiega" na kontrolerze, aby mockować wywołanie private validateCaptcha
        AuthController spyController = spy(authController);
        doReturn(false).when(spyController).validateCaptcha(anyString());  // Mockowanie odpowiedzi CAPTCHA na false

        // Wywołanie metody logowania
        ResponseEntity<String> response = spyController.login(loginRequest);

        // Sprawdzamy, czy odpowiedź to 403 Forbidden oraz komunikat o nieudanej walidacji CAPTCHA
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Captcha validation failed", response.getBody());
    }

    // Testowanie logowania, kiedy dane logowania są niepoprawne
    @Test
    void testLoginInvalidCredentials() {
        // Przygotowanie danych wejściowych do logowania
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("testuser@example.com");
        loginRequest.setPassword("wrongpassword");
        loginRequest.setCaptchaResponse("valid-captcha-response");

        // Mockowanie odpowiedzi z metody authenticateByEmail, że dane logowania są niepoprawne
        when(userService.authenticateByEmail(anyString(), anyString())).thenReturn(false);

        // Tworzymy "szpiega" na kontrolerze, aby mockować wywołanie private validateCaptcha
        AuthController spyController = spy(authController);
        doReturn(true).when(spyController).validateCaptcha(anyString());  // Mockowanie odpowiedzi CAPTCHA na true

        // Wywołanie metody logowania
        ResponseEntity<String> response = spyController.login(loginRequest);

        // Sprawdzamy, czy odpowiedź to 401 Unauthorized oraz komunikat o niepoprawnych danych logowania
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid credentials", response.getBody());

        // Weryfikacja, czy metoda authenticateByEmail została wywołana raz
        verify(userService, times(1)).authenticateByEmail(loginRequest.getEmail(), loginRequest.getPassword());
    }
}
