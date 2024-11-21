package com.example.flicker;

import com.FlickerDomain.flicker.config.SecurityConfig;
import com.FlickerDomain.flicker.security.JwtProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class SecurityConfigTest {

    @Autowired
    private SecurityConfig securityConfig;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void testPasswordEncoderBean() {
        // Sprawdzenie, czy PasswordEncoder jest zarejestrowany jako bean
        assertThat(passwordEncoder).isNotNull();
        assertThat(passwordEncoder).isInstanceOf(PasswordEncoder.class);

        // Sprawdzenie, czy działa enkodowanie i dekodowanie hasła
        String rawPassword = "testPassword";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        assertThat(passwordEncoder.matches(rawPassword, encodedPassword)).isTrue();
    }

    @Test
    public void testRestTemplateBean() {
        // Sprawdzenie, czy RestTemplate jest zarejestrowany jako bean
        assertThat(restTemplate).isNotNull();
        assertThat(restTemplate).isInstanceOf(RestTemplate.class);
    }

    @Test
    public void testSecurityFilterChain() throws Exception {
        JwtProvider mockJwtProvider = new JwtProvider(); // Można użyć mockowania, jeśli JwtProvider ma zależności
        SecurityConfig localConfig = new SecurityConfig(mockJwtProvider);

        // Sprawdzenie, czy SecurityFilterChain jest tworzony bez błędów
        assertThat(localConfig.securityFilterChain(null)).isNotNull();
    }
}