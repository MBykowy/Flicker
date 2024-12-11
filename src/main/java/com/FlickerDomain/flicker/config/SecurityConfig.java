package com.FlickerDomain.flicker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.FlickerDomain.flicker.security.JwtProvider;
import com.FlickerDomain.flicker.security.JwtAuthenticationFilter;
import org.springframework.web.client.RestTemplate;

/**
 * Konfiguracja bezpieczeństwa aplikacji.
 * Ta klasa konfiguruje zabezpieczenia aplikacji, w tym filtrowanie żądań HTTP,
 * szyfrowanie haseł oraz wstawianie filtra JWT (JSON Web Token) do łańcucha filtrów bezpieczeństwa.
 */
@Configuration
public class SecurityConfig {

    private final JwtProvider jwtProvider; // Provider do generowania i weryfikowania JWT

    /**
     * Konstruktor klasy SecurityConfig.
     * Inicjalizuje klasę z instancją JwtProvider, który jest używany do operacji związanych z JWT.
     *
     * @param jwtProvider instancja klasy odpowiedzialnej za operacje z JWT.
     */
    public SecurityConfig(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    /**
     * Definiuje filtr bezpieczeństwa aplikacji.
     * Ustawia konfigurację zabezpieczeń, w tym wyłączenie CSRF, oraz dodaje filtr do obsługi JWT.
     * Filtr JWT jest dodawany przed standardowym filtrem `UsernamePasswordAuthenticationFilter`.
     *
     * @param http obiekt HttpSecurity, umożliwiający konfigurację zasad bezpieczeństwa.
     * @return obiekt SecurityFilterChain, który buduje konfigurację filtrów bezpieczeństwa.
     * @throws Exception jeśli wystąpi błąd w konfiguracji bezpieczeństwa.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())  // Wyłączenie mechanizmu CSRF (Cross-Site Request Forgery)
                // Konfiguracja filtrów bezpieczeństwa (zakomentowana w kodzie)
                //.authorizeHttpRequests(auth -> auth
                //    .requestMatchers("/auth/**").permitAll()  // Pozwolenie na dostęp do ścieżek /auth/**
                //    .anyRequest().authenticated()           // Pozostałe ścieżki wymagają autoryzacji
                //)
                .addFilterBefore(new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class);  // Dodanie filtra JWT przed filtrem logowania

        return http.build(); // Budowa i zwrócenie obiektu SecurityFilterChain
    }

    /**
     * Definiuje bean odpowiedzialny za kodowanie haseł.
     * Używa BCryptPasswordEncoder do kodowania haseł, co zapewnia ich bezpieczeństwo.
     *
     * @return instancja BCryptPasswordEncoder, która jest używana do kodowania haseł.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Użycie algorytmu bcrypt do kodowania haseł
    }

    /**
     * Definiuje bean RestTemplate, który będzie używany do wykonywania żądań HTTP w aplikacji.
     *
     * @return instancja RestTemplate, która umożliwia komunikację z zewnętrznymi serwisami.
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate(); // Zwrócenie nowej instancji RestTemplate
    }
}
