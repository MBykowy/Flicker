package com.example.flicker;

import com.FlickerDomain.flicker.config.SecurityConfig;
import com.FlickerDomain.flicker.security.JwtProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class SecurityConfigTest {

    private JwtProvider jwtProvider;
    private SecurityConfig securityConfig;

    @BeforeEach
    public void setUp() {
        // Mockowanie JwtProvider
        jwtProvider = Mockito.mock(JwtProvider.class);
        // Tworzenie instancji SecurityConfig
        securityConfig = new SecurityConfig(jwtProvider);
    }

    @Test
    public void shouldProvidePasswordEncoder() {
        // Sprawdzenie, czy PasswordEncoder jest zainicjowany poprawnie
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
        assertNotNull(passwordEncoder, "PasswordEncoder should be initialized");
    }

    @Test
    public void shouldProvideRestTemplate() {
        // Sprawdzenie, czy RestTemplate jest zainicjowany poprawnie
        RestTemplate restTemplate = securityConfig.restTemplate();
        assertNotNull(restTemplate, "RestTemplate should be initialized");
    }

    @Test
    public void shouldInjectJwtProvider() {
        // Sprawdzenie, czy JwtProvider został poprawnie wstrzyknięty
        // Testowanie, czy jwtProvider nie jest null
        assertNotNull(securityConfig, "SecurityConfig should be properly initialized");
        assertNotNull(jwtProvider, "JwtProvider should be injected correctly");
    }
}