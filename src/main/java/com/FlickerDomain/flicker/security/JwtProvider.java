package com.FlickerDomain.flicker.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;

import java.util.Date;
import com.FlickerDomain.flicker.model.User;

/**
 * Klasa {@link JwtProvider} odpowiedzialna za generowanie, walidację i ekstrakcję informacji z tokenów JWT.
 *
 * {@link JwtProvider} używa biblioteki JJWT do generowania tokenów JWT, ich walidacji oraz ekstrakcji danych z tokenów,
 * takich jak adres e-mail użytkownika. Klasa ta jest używana do zapewnienia autentykacji opartej na tokenach JWT
 * w aplikacjach opartych na Spring Security.
 */
@Component
public class JwtProvider {

    /**
     * Sekretny klucz używany do podpisywania i weryfikacji tokenów JWT.
     */
    private final String SECRET_KEY = "your_secret_key";

    /**
     * Generuje token JWT na podstawie danych użytkownika.
     *
     * Token zawiera informacje o użytkowniku, takie jak adres e-mail (jako temat), datę wystawienia
     * i datę wygaśnięcia (po 24 godzinach). Token jest podpisywany za pomocą algorytmu HMAC SHA-512.
     *
     * @param user obiekt {@link User}, który zawiera dane użytkownika, które mają być zawarte w tokenie.
     * @return wygenerowany token JWT jako {@link String}.
     */
    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail()) // Ustawienie e-maila użytkownika jako "subject"
                .setIssuedAt(new Date()) // Ustawienie daty wystawienia tokenu
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // Ustawienie daty wygaśnięcia (1 dzień)
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY) // Podpisanie tokenu za pomocą algorytmu HS512 i sekretnego klucza
                .compact(); // Zbudowanie tokenu
    }

    /**
     * Waliduje token JWT, sprawdzając jego integralność i podpis.
     *
     * @param token token JWT do walidacji.
     * @return {@code true} jeśli token jest ważny i podpis jest poprawny, w przeciwnym razie {@code false}.
     */
    public boolean validateToken(String token) {
        try {
            // Próba parsowania i weryfikacji tokenu przy użyciu sekretnego klucza
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true; // Jeśli parsowanie i weryfikacja powiodą się, token jest ważny
        } catch (Exception e) {
            return false; // W przypadku wyjątku (np. nieprawidłowy podpis), token jest nieważny
        }
    }

    /**
     * Ekstrahuje adres e-mail użytkownika z tokenu JWT.
     *
     * @param token token JWT, z którego ma zostać wyciągnięty adres e-mail.
     * @return adres e-mail użytkownika zawarty w tokenie.
     */
    public String getEmailFromJwt(String token) {
        // Parsowanie tokenu i ekstrakcja informacji o użytkowniku (subject)
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY) // Ustawienie sekretnego klucza do weryfikacji
                .parseClaimsJws(token) // Parsowanie tokenu
                .getBody(); // Pobranie ciała tokenu (Claims)
        return claims.getSubject(); // Zwrócenie adresu e-mail (subject)
    }
}
