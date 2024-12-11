package com.FlickerDomain.flicker.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Filtr do autentykacji JWT (JSON Web Token).
 *
 * {@link JwtAuthenticationFilter} jest filtrem, który weryfikuje token JWT w nagłówku żądania HTTP
 * i na podstawie poprawności tokenu ustawia informacje o użytkowniku w kontekście bezpieczeństwa aplikacji.
 * Filtr ten jest uruchamiany raz na każde żądanie przy użyciu metody {@link #doFilterInternal}.
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    /**
     * Obiekt {@link JwtProvider}, który zapewnia metody do walidacji i ekstrakcji informacji z tokenu JWT.
     */
    private final JwtProvider jwtProvider;

    /**
     * Konstruktor klasy {@link JwtAuthenticationFilter}.
     * Inicjalizuje filtr JWT przyjmując obiekt {@link JwtProvider}, który będzie używany do
     * weryfikacji tokenu oraz ekstrakcji danych użytkownika z tokenu JWT.
     *
     * @param jwtProvider obiekt {@link JwtProvider}, który zapewnia funkcjonalności związane z JWT.
     */
    public JwtAuthenticationFilter(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    /**
     * Przeprowadza filtrację żądania HTTP, weryfikuje obecność i ważność tokenu JWT,
     * a następnie ustawia informacje o użytkowniku w kontekście bezpieczeństwa aplikacji.
     *
     * @param request obiekt {@link HttpServletRequest}, który zawiera dane o nadchodzącym żądaniu HTTP.
     * @param response obiekt {@link HttpServletResponse}, który służy do wysyłania odpowiedzi HTTP.
     * @param filterChain łańcuch filtrów, który pozwala przekazać żądanie do kolejnych filtrów w aplikacji.
     * @throws ServletException jeśli wystąpi problem z przetwarzaniem żądania.
     * @throws IOException jeśli wystąpi problem z przetwarzaniem odpowiedzi.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // Pobranie tokenu JWT z nagłówka żądania
        String token = getJwtFromRequest(request);

        // Sprawdzenie, czy token jest obecny i czy jest ważny
        if (StringUtils.hasText(token) && jwtProvider.validateToken(token)) {
            // Ekstrakcja adresu email użytkownika z tokenu JWT
            String email = jwtProvider.getEmailFromJwt(token);

            // Tworzenie obiektu autentykacji z danymi użytkownika
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(email, null, new ArrayList<>());

            // Ustawienie autentykacji w kontekście bezpieczeństwa aplikacji
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        // Kontynuacja przetwarzania żądania przez inne filtry
        filterChain.doFilter(request, response);
    }

    /**
     * Pobiera token JWT z nagłówka "Authorization" żądania HTTP.
     * Token powinien być poprzedzony słowem "Bearer ".
     *
     * @param request obiekt {@link HttpServletRequest}, który zawiera dane żądania.
     * @return token JWT jako {@link String}, lub null jeśli nie znaleziono poprawnego tokenu.
     */
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        // Sprawdzenie, czy nagłówek zawiera token typu Bearer
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // Zwrócenie samego tokenu (usunięcie "Bearer " na początku)
        }
        return null;
    }
}
