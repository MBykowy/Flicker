package com.FlickerDomain.flicker.service;

import com.FlickerDomain.flicker.dto.MessageDTO;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

/**
 * Service class for handling WebSocket messaging operations.
 */
@Service
public class WebSocketService {

    private final SimpMessagingTemplate messagingTemplate;

    /**
     * Constructor for WebSocketService.
     *
     * @param messagingTemplate the SimpMessagingTemplate used for sending messages
     */
    public WebSocketService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * Sends a message to a specific destination.
     *
     * @param destination the WebSocket destination (e.g., a chat room or user)
     * @param messageDTO the message to be sent
     */
    public void sendMessage(String destination, MessageDTO messageDTO) {
        messagingTemplate.convertAndSend(destination, messageDTO);
    }

    /**
     * Sends a typing notification to a specific destination.
     *
     * @param destination the WebSocket destination (e.g., a chat room or user)
     * @param typingStatus the typing status (e.g., "User is typing...")
     */
    public void sendTypingNotification(String destination, String typingStatus) {
        messagingTemplate.convertAndSend(destination, typingStatus);
    }
}
