package com.FlickerDomain.flicker.controller;

import com.FlickerDomain.flicker.dto.MessageDTO;
import com.FlickerDomain.flicker.service.ChatService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatWebSocketController {
    private final ChatService chatService;

    public ChatWebSocketController(ChatService chatService) {
        this.chatService = chatService;
    }

    @MessageMapping("/chat.send")
    @SendTo("/topic/messages")
    public MessageDTO sendMessage(MessageDTO messageDTO) {
        chatService.sendMessage(messageDTO);
        return messageDTO;
    }

    @MessageMapping("/chat.typing")
    @SendTo("/topic/typing")
    public String sendTypingNotification(String typingStatus) {
        return typingStatus;
    }
}