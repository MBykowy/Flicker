package com.FlickerDomain.flicker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Konfiguracja CORS (Cross-Origin Resource Sharing) aplikacji.
 * Klasa ta umożliwia skonfigurowanie zasad CORS, które pozwalają na kontrolowanie,
 * które domeny mogą uzyskiwać dostęp do zasobów aplikacji z poziomu przeglądarki.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Dodaje konfigurację CORS dla aplikacji.
     * Umożliwia dostęp do zasobów aplikacji z określonych źródeł i dla określonych metod HTTP.
     * Konfiguracja ta pozwala na udostępnienie zasobów frontendowym aplikacjom, które
     * działają na innych portach lub domenach.
     *
     * @param registry obiekt CorsRegistry, który umożliwia rejestrowanie zasad CORS.
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Ogólna konfiguracja CORS dla całej aplikacji
        registry.addMapping("/**")  // Zezwolenie na dostęp do wszystkich zasobów w aplikacji
                .allowedOrigins("http://localhost:3000", "http://localhost:3001")  // Określenie dozwolonych źródeł
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");  // Określenie dozwolonych metod HTTP

        // Konfiguracja CORS dla specyficznego API (ścieżki zaczynające się od "/api/**")
        registry.addMapping("/api/**")  // Zezwolenie na dostęp do zasobów w ścieżkach "/api/**"
                .allowedOrigins("http://localhost:3000", "http://localhost:3001")  // Określenie dozwolonych źródeł
                .allowedMethods("GET", "POST", "PUT", "DELETE")  // Określenie dozwolonych metod HTTP
                .allowedHeaders("*")  // Zezwolenie na wszystkie nagłówki
                .allowCredentials(true);  // Zezwolenie na wysyłanie ciasteczek i poświadczeń
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
    }
}
