package com.FlickerDomain.flicker.service;

import com.FlickerDomain.flicker.dto.MessageDTO;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketService {
    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendMessage(String destination, MessageDTO messageDTO) {
        messagingTemplate.convertAndSend(destination, messageDTO);
    }

    public void sendTypingNotification(String destination, String typingStatus) {
        messagingTemplate.convertAndSend(destination, typingStatus);
    }
}