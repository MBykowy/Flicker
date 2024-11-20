package com.FlickerDomain.flicker.Testy;

import com.FlickerDomain.flicker.controller.AuthController;
import com.FlickerDomain.flicker.service.UserService;
import com.FlickerDomain.flicker.dto.RegisterRequest;
import com.FlickerDomain.flicker.dto.LoginRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class AuthControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test for registerUser
    @Test
    void testRegisterUser() {
        // Arrange
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("test@example.com");
        registerRequest.setPassword("password123");

        // Act
        ResponseEntity<?> response = authController.registerUser(registerRequest);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("User registered successfully", response.getBody());

        // Verify that the userService.register() was called once
        verify(userService, times(1)).register(registerRequest);
    }

    // Test for login (correct credentials)
    @Test
    void testLoginSuccessful() {
        // Arrange
        String email = "test@example.com";
        String password = "password123";
        when(userService.authenticateByEmail(email, password)).thenReturn(true);

        // Act
        ResponseEntity<String> response = authController.login(email, password);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Login successful", response.getBody());

        // Verify that the userService.authenticateByEmail() was called once
        verify(userService, times(1)).authenticateByEmail(email, password);
    }

    // Test for login (incorrect credentials)
    @Test
    void testLoginFailed() {
        // Arrange
        String email = "test@example.com";
        String password = "wrongPassword";
        when(userService.authenticateByEmail(email, password)).thenReturn(false);

        // Act
        ResponseEntity<String> response = authController.login(email, password);

        // Assert
        assertEquals(401, response.getStatusCodeValue());
        assertEquals("Invalid credentials", response.getBody());

        // Verify that the userService.authenticateByEmail() was called once
        verify(userService, times(1)).authenticateByEmail(email, password);
    }
}
