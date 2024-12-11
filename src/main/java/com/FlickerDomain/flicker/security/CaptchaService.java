package com.FlickerDomain.flicker.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

/**
 * Usługa do weryfikacji CAPTCHA Google reCAPTCHA.
 *
 * Klasa {@link CaptchaService} zapewnia mechanizm weryfikacji odpowiedzi CAPTCHA przesyłanej przez użytkownika,
 * komunikując się z API Google reCAPTCHA w celu sprawdzenia, czy odpowiedź jest prawidłowa.
 * Wykorzystuje do tego celu usługę {@link RestTemplate} do wysyłania żądania HTTP do Google API.
 *
 * @see RestTemplate
 * @see org.springframework.http.ResponseEntity
 */
@Service
public class CaptchaService {

    /**
     * Klucz tajny używany do weryfikacji odpowiedzi CAPTCHA.
     */
    @Value("${recaptcha.secret.key}")
    private String recaptchaSecretKey;

    /**
     * URL API Google reCAPTCHA do weryfikacji odpowiedzi.
     */
    private static final String RECAPTCHA_VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";

    /**
     * Instancja {@link RestTemplate} używana do wysyłania żądań HTTP.
     */
    private final RestTemplate restTemplate;

    /**
     * Konstruktor klasy {@link CaptchaService}. Inicjuje obiekt {@link RestTemplate} za pomocą adnotacji {@link Qualifier}.
     *
     * @param restTemplate obiekt {@link RestTemplate} wstrzykiwany do usługi.
     */
    @Autowired
    public CaptchaService(@Qualifier("appRestTemplate") RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Weryfikuje odpowiedź CAPTCHA, wysyłając zapytanie do Google reCAPTCHA API.
     *
     * @param captchaResponse odpowiedź użytkownika na CAPTCHA, którą należy zweryfikować
     * @return true, jeśli odpowiedź CAPTCHA jest prawidłowa (zgodna z odpowiedzią Google reCAPTCHA), w przeciwnym razie false
     */
    public boolean verifyCaptcha(String captchaResponse) {
        // Tworzenie URL-a z tajnym kluczem oraz odpowiedzią użytkownika
        String url = RECAPTCHA_VERIFY_URL + "?secret=" + recaptchaSecretKey + "&response=" + captchaResponse;

        // Wysyłanie żądania do Google reCAPTCHA
        ResponseEntity<String> response = restTemplate.postForEntity(url, null, String.class);

        // Sprawdzanie, czy odpowiedź zawiera "success": true
        return response.getBody().contains("\"success\": true");
    }
}
