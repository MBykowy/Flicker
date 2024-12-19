package com.FlickerDomain.flicker.controller;

import com.FlickerDomain.flicker.dto.MessageDTO;
import com.FlickerDomain.flicker.dto.ConversationDTO;
import com.FlickerDomain.flicker.service.ChatService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatRestController {
    private final ChatService chatService;

    public ChatRestController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/conversations")
    public List<ConversationDTO> getConversations(@RequestParam Long userId) {
        return chatService.getUserConversations(userId);
    }

    @GetMapping("/messages/{conversationId}")
    public List<MessageDTO> getMessages(@PathVariable Long conversationId) {
        return chatService.getChatHistory(conversationId);
    }

    @PostMapping("/messages/read")
    public void markMessagesAsRead(@RequestParam Long receiverId) {
        chatService.markMessagesAsRead(receiverId);
    }
}