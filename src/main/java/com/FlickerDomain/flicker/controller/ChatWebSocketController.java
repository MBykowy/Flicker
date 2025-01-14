package com.FlickerDomain.flicker.controller;

import com.FlickerDomain.flicker.dto.MessageDTO;
import com.FlickerDomain.flicker.service.ChatService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

/**
 * Kontroler obsługujący wiadomości WebSocket związane z czatem.
 * Obsługuje wysyłanie wiadomości oraz powiadomienia o pisaniu,
 * które są wysyłane do wszystkich subskrybujących odpowiednie tematy.
 */
@Controller
public class ChatWebSocketController {
    private final ChatService chatService;

    /**
     * Konstruktor kontrolera.
     *
     * @param chatService serwis czatu, który będzie używany do obsługi wiadomości
     */
    public ChatWebSocketController(ChatService chatService) {
        this.chatService = chatService;
    }

    /**
     * Obsługuje wysyłanie wiadomości na kanał WebSocket.
     * Po odebraniu wiadomości od klienta, wiadomość jest przekazywana do serwisu czatu
     * oraz wysyłana do subskrybujących kanał "/topic/messages".
     *
     * @param messageDTO obiekt zawierający wiadomość do wysłania
     * @return obiekt `MessageDTO`, który jest wysyłany do wszystkich subskrybujących kanał
     */
    @MessageMapping("/chat.send")
    @SendTo("/topic/messages")
    public MessageDTO sendMessage(MessageDTO messageDTO) {
        chatService.sendMessage(messageDTO); // Przekazanie wiadomości do serwisu
        return messageDTO; // Zwrócenie wiadomości, aby została wysłana do subskrybentów
    }

    /**
     * Obsługuje powiadomienie o tym, że użytkownik zaczyna pisać wiadomość.
     * Po odebraniu statusu pisania, jest on wysyłany do wszystkich subskrybujących kanał
     * "/topic/typing", aby zaktualizować stan w interfejsie użytkownika.
     *
     * @param typingStatus status informujący, że użytkownik zaczyna pisać
     * @return status pisania, który jest wysyłany do subskrybentów
     */
    @MessageMapping("/chat.typing")
    @SendTo("/topic/typing")
    public String sendTypingNotification(String typingStatus) {
        return typingStatus; // Zwrócenie statusu pisania do subskrybentów
    }
}
