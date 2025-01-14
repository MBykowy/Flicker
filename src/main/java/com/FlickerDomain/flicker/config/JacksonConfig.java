package com.FlickerDomain.flicker.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Konfiguracja Jacksona w aplikacji Spring.
 *
 * Ta klasa konfiguruje bibliotekę Jackson, która jest używana do
 * serializacji i deserializacji obiektów Java do formatu JSON
 * oraz odwrotnie. Jackson jest wykorzystywany w aplikacjach
 * Spring do obsługi formatowania JSON w żądaniach HTTP i odpowiedziach.
 */
@Configuration
public class JacksonConfig {

    /**
     * Tworzy i zwraca instancję {@link ObjectMapper}, która jest
     * głównym narzędziem w Jacksonie do pracy z JSON-em.
     *
     * @return nową instancję {@link ObjectMapper}.
     */
    @Bean
    public ObjectMapper objectMapper() {
        // Tworzy i zwraca nową instancję ObjectMapper
        return new ObjectMapper();
    }
}
