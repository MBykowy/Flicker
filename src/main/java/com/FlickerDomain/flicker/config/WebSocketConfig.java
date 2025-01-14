package com.FlickerDomain.flicker.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * Konfiguracja WebSocket w aplikacji.
 * Umożliwia komunikację w czasie rzeczywistym między klientem a serwerem za pomocą protokołu WebSocket,
 * przy użyciu STOMP (Simple Text Oriented Messaging Protocol).
 */
@Configuration
@EnableWebSocketMessageBroker  // Umożliwia wykorzystanie brokerów wiadomości w WebSocket
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * Konfiguruje broker wiadomości, który będzie obsługiwał komunikację między klientem a serwerem.
     * Wykorzystuje prosty broker wiadomości do obsługi tematów (np. /topic) oraz prefiksy aplikacyjne
     * dla wiadomości wysyłanych przez aplikację (np. /app).
     *
     * @param registry obiekt MessageBrokerRegistry, umożliwiający konfigurację brokera wiadomości.
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // Konfiguracja prostego brokera wiadomości obsługującego tematy (np. /topic)
        registry.enableSimpleBroker("/topic");

        // Konfiguracja prefiksu aplikacyjnego dla ścieżek, które będą obsługiwane przez aplikację
        registry.setApplicationDestinationPrefixes("/app");
    }

    /**
     * Rejestruje punkty końcowe STOMP (WebSocket) w aplikacji.
     * Punkty końcowe służą do ustanowienia połączenia WebSocket oraz umożliwiają komunikację z klientem.
     * Ustawia również dozwolone źródła połączeń oraz umożliwia korzystanie z SockJS jako alternatywnej
     * metody połączenia w przypadku, gdy WebSocket jest niedostępny.
     *
     * @param registry obiekt StompEndpointRegistry, który umożliwia rejestrację punktów końcowych WebSocket.
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Rejestracja punktu końcowego WebSocket pod ścieżką "/ws"
        registry.addEndpoint("/ws")
                .setAllowedOrigins("*")  // Zezwolenie na połączenia z dowolnych źródeł
                .withSockJS();  // Włączenie obsługi SockJS dla alternatywnych połączeń
    }
}
