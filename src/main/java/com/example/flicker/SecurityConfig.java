package com.example.flicker;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;



@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Definicja beana PasswordEncoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Konfiguracja zabezpieczeń dla aplikacji
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/login", "/register").permitAll()  // Pozwól na dostęp do login i rejestracji
                        .anyRequest().authenticated())  // Inne strony wymagają logowania
                .formLogin(form -> form
                        .loginPage("/login")  // Strona logowania
                        .defaultSuccessUrl("/", true))  // Po zalogowaniu użytkownik wraca na stronę główną
                .logout(logout -> logout
                        .logoutSuccessUrl("/login")  // Po wylogowaniu wraca do logowania
                        .permitAll());

        return http.build();
    }
}