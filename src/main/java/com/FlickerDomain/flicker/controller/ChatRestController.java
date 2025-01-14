package com.FlickerDomain.flicker.controller;

import com.FlickerDomain.flicker.dto.MessageDTO;
import com.FlickerDomain.flicker.dto.ConversationDTO;
import com.FlickerDomain.flicker.service.ChatService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Kontroler obsługujący żądania związane z czatem.
 * Obejmuje funkcje umożliwiające pobieranie rozmów użytkownika,
 * pobieranie historii wiadomości dla konkretnej rozmowy oraz oznaczanie
 * wiadomości jako przeczytane.
 */
@RestController
@RequestMapping("/api/chat")
public class ChatRestController {
    private final ChatService chatService;

    /**
     * Konstruktor kontrolera.
     *
     * @param chatService serwis czatu, który będzie używany do operacji na rozmowach i wiadomościach
     */
    public ChatRestController(ChatService chatService) {
        this.chatService = chatService;
    }

    /**
     * Pobiera listę rozmów użytkownika.
     *
     * @param userId identyfikator użytkownika, dla którego mają zostać pobrane rozmowy
     * @return lista obiektów `ConversationDTO` reprezentujących rozmowy użytkownika
     */
    @GetMapping("/conversations")
    public List<ConversationDTO> getConversations(@RequestParam Long userId) {
        return chatService.getUserConversations(userId);
    }

    /**
     * Pobiera historię wiadomości dla danej rozmowy.
     *
     * @param conversationId identyfikator rozmowy, dla której mają zostać pobrane wiadomości
     * @return lista obiektów `MessageDTO` reprezentujących wiadomości w danej rozmowie
     */
    @GetMapping("/messages/{conversationId}")
    public List<MessageDTO> getMessages(@PathVariable Long conversationId) {
        return chatService.getChatHistory(conversationId);
    }

    /**
     * Oznacza wiadomości jako przeczytane dla określonego odbiorcy.
     *
     * @param receiverId identyfikator odbiorcy wiadomości, których status ma zostać zmieniony na "przeczytane"
     */
    @PostMapping("/messages/read")
    public void markMessagesAsRead(@RequestParam Long receiverId) {
        chatService.markMessagesAsRead(receiverId);
    }
}
