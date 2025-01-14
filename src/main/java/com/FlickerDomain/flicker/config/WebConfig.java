package com.FlickerDomain.flicker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Klasa konfigurująca ustawienia CORS (Cross-Origin Resource Sharing) oraz obsługę zasobów w aplikacji.
 * CORS pozwala na kontrolowanie, które domeny mogą uzyskiwać dostęp do zasobów aplikacji z poziomu przeglądarki.
 * Dodatkowo, klasa ta konfiguruje ścieżki do lokalnych zasobów, takich jak pliki w katalogu 'uploads'.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Konfiguruje zasady CORS (Cross-Origin Resource Sharing) dla aplikacji.
     * CORS określa, które źródła (domeny) mogą uzyskiwać dostęp do zasobów aplikacji.
     * Konfiguracja ta umożliwia frontendowym aplikacjom działającym na innych portach lub domenach dostęp do zasobów.
     *
     * @param registry obiekt CorsRegistry, który umożliwia rejestrowanie zasad CORS.
     *                Przy jego pomocy określane są dozwolone źródła (domeny) oraz metody HTTP.
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Ogólna konfiguracja CORS dla całej aplikacji
        registry.addMapping("/**")  // Zezwolenie na dostęp do wszystkich zasobów w aplikacji
                .allowedOrigins("http://localhost:3000", "http://localhost:3001")  // Dozwolone źródła, które mogą wykonywać zapytania
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");  // Dozwolone metody HTTP

        // Specjalna konfiguracja CORS dla API (ścieżki zaczynające się od "/api/**")
        registry.addMapping("/api/**")  // Zezwolenie na dostęp do zasobów w ścieżkach "/api/**"
                .allowedOrigins("http://localhost:3000", "http://localhost:3001")  // Dozwolone źródła
                .allowedMethods("GET", "POST", "PUT", "DELETE")  // Dozwolone metody HTTP
                .allowedHeaders("*")  // Zezwolenie na wszystkie nagłówki
                .allowCredentials(true);  // Zezwolenie na wysyłanie ciasteczek i poświadczeń (np. sesji użytkownika)
    }

    /**
     * Konfiguruje obsługę zasobów w aplikacji.
     * Umożliwia dostęp do plików umieszczonych w katalogu 'uploads'.
     * Zasoby te mogą być wykorzystywane w aplikacji frontendowej (np. do pobierania plików).
     *
     * @param registry obiekt ResourceHandlerRegistry, który umożliwia rejestrowanie obsługi zasobów.
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Ścieżka do plików znajdujących się w katalogu 'uploads'
        registry.addResourceHandler("/uploads/**")  // Określenie ścieżki zasobów
                .addResourceLocations("file:uploads/");  // Lokalizacja plików na dysku
    }
}
