package com.example.flicker;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.FlickerDomain.flicker.config.SecurityConfig;
import com.FlickerDomain.flicker.security.JwtAuthenticationFilter;
import com.FlickerDomain.flicker.security.JwtProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

class SecurityConfigTest {

    @Mock
    private JwtProvider jwtProvider; // Mocking JwtProvider

    @InjectMocks
    private SecurityConfig securityConfig; // Injecting mocks into the SecurityConfig

    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    void setUp() {
        // Initialize JwtAuthenticationFilter with the mocked JwtProvider
        jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtProvider);
    }

    @Test
    void testSecurityFilterChain() throws Exception {
        // Create a mock of HttpSecurity
        HttpSecurity httpSecurity = mock(HttpSecurity.class);

        // Call the method to test
        SecurityFilterChain filterChain = securityConfig.securityFilterChain(httpSecurity);

        // Verify that the filter chain is not null
        assertThat(filterChain).isNotNull();

        // Verify if the JWT filter is correctly added before UsernamePasswordAuthenticationFilter
        verify(httpSecurity, times(1)).addFilterBefore(eq(jwtAuthenticationFilter), eq(UsernamePasswordAuthenticationFilter.class));
    }

    @Test
    void testPasswordEncoder() {
        // Test the password encoder bean
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();

        // Assert that the returned encoder is an instance of BCryptPasswordEncoder
        assertThat(passwordEncoder).isInstanceOf(BCryptPasswordEncoder.class);
    }

    @Test
    void testRestTemplate() {
        // Test the RestTemplate bean
        RestTemplate restTemplate = securityConfig.restTemplate();

        // Assert that the returned RestTemplate is not null
        assertThat(restTemplate).isNotNull();
    }
}
