package com.FlickerDomain.flicker.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

@Service
public class CaptchaService {

    @Value("${recaptcha.secret.key}")
    private String recaptchaSecretKey;

    private static final String RECAPTCHA_VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";

    private final RestTemplate restTemplate;

    // Constructor-based injection with @Qualifier to specify the bean to inject
    @Autowired
    public CaptchaService(@Qualifier("appRestTemplate") RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean verifyCaptcha(String captchaResponse) {
        String url = RECAPTCHA_VERIFY_URL + "?secret=" + recaptchaSecretKey + "&response=" + captchaResponse;

        // Wysyłanie żądania do Google reCAPTCHA
        ResponseEntity<String> response = restTemplate.postForEntity(url, null, String.class);

        // Sprawdzanie, czy odpowiedź zawiera "success": true
        return response.getBody().contains("\"success\": true");
    }
}
