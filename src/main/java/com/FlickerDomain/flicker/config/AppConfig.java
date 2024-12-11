package com.FlickerDomain.flicker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Konfiguracja aplikacji, która dostarcza konfigurację Spring.
 * Ta klasa zawiera definicję beana dla obiektu {@link RestTemplate}, który jest używany
 * do wykonywania żądań HTTP do zewnętrznych usług.
 */
@Configuration
public class AppConfig {

    /**
     * Definicja beana {@link RestTemplate} dla aplikacji.
     * {@link RestTemplate} jest wykorzystywany do komunikacji z zewnętrznymi serwisami
     * poprzez protokół HTTP. Umożliwia wysyłanie żądań oraz odbieranie odpowiedzi.
     *
     * @return instancja {@link RestTemplate}.
     */
    @Bean(name = "appRestTemplate")
    public RestTemplate restTemplate() {
        return new RestTemplate(); // Zwraca nową instancję RestTemplate
    }
}
