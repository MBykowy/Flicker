package com.example.flicker;

import com.FlickerDomain.flicker.config.SecurityConfig;
import com.FlickerDomain.flicker.security.JwtProvider;
import com.FlickerDomain.flicker.security.JwtAuthenticationFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class SecurityConfigTest {

    @Mock
    private JwtProvider jwtProvider;

    private SecurityConfig securityConfig;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        securityConfig = new SecurityConfig(jwtProvider);
    }

    @Test
    void testPasswordEncoder() {
        // Test czy bean PasswordEncoder zwraca BCryptPasswordEncoder
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
        assertThat(passwordEncoder).isNotNull();
        assertThat(passwordEncoder.getClass().getSimpleName()).isEqualTo("BCryptPasswordEncoder");
    }

    @Test
    void testRestTemplate() {
        // Test czy bean RestTemplate jest prawidłowo skonfigurowany
        RestTemplate restTemplate = securityConfig.restTemplate();
        assertThat(restTemplate).isNotNull();
    }

    @Test
    void testSecurityFilterChain() throws Exception {
        // Test czy SecurityFilterChain jest prawidłowo skonfigurowany
        HttpSecurity httpSecurity = mock(HttpSecurity.class);
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtProvider);

        SecurityConfig spyConfig = new SecurityConfig(jwtProvider) {

            public JwtAuthenticationFilter jwtAuthenticationFilter() {
                return jwtAuthenticationFilter;
            }
        };

        assertThat(spyConfig.securityFilterChain(httpSecurity)).isNotNull();
    }
}
