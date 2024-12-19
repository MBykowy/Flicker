package com.FlickerDomain.flicker.service;

import com.FlickerDomain.flicker.dto.MessageDTO;
import com.FlickerDomain.flicker.dto.ConversationDTO;
import com.FlickerDomain.flicker.model.Message;
import com.FlickerDomain.flicker.model.Conversation;
import com.FlickerDomain.flicker.model.User;
import com.FlickerDomain.flicker.repository.MessageRepository;
import com.FlickerDomain.flicker.repository.ConversationRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatService {
    private final MessageRepository messageRepository;
    private final ConversationRepository conversationRepository;

    public ChatService(MessageRepository messageRepository, ConversationRepository conversationRepository) {
        this.messageRepository = messageRepository;
        this.conversationRepository = conversationRepository;
    }

    public void sendMessage(MessageDTO messageDTO) {
        Message message = new Message();
        User sender = new User();
        sender.setId(messageDTO.getSenderId());
        User receiver = new User();
        receiver.setId(messageDTO.getReceiverId());
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(messageDTO.getContent());
        message.setTimestamp(messageDTO.getTimestamp());
        message.setReadStatus(messageDTO.isReadStatus());
        messageRepository.save(message);
    }

    public List<MessageDTO> getChatHistory(Long conversationId) {
        List<Message> messages = messageRepository.findByConversationId(conversationId);
        return messages.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public void markMessagesAsRead(Long receiverId) {
        List<Message> unreadMessages = messageRepository.findByReceiverIdAndReadStatusFalse(receiverId);
        unreadMessages.forEach(message -> message.setReadStatus(true));
        messageRepository.saveAll(unreadMessages);
    }

    public List<ConversationDTO> getUserConversations(Long userId) {
        List<Conversation> conversations = conversationRepository.findByParticipant1IdOrParticipant2Id(userId, userId);
        return conversations.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private MessageDTO convertToDTO(Message message) {
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setId(message.getId());
        messageDTO.setSenderId(message.getSender().getId());
        messageDTO.setReceiverId(message.getReceiver().getId());
        messageDTO.setContent(message.getContent());
        messageDTO.setTimestamp(message.getTimestamp());
        messageDTO.setReadStatus(message.isReadStatus());
        return messageDTO;
    }

    private ConversationDTO convertToDTO(Conversation conversation) {
        ConversationDTO conversationDTO = new ConversationDTO();
        conversationDTO.setId(conversation.getId());
        conversationDTO.setOtherParticipant(conversation.getParticipant1().getId());
        conversationDTO.setLastMessage(conversation.getLastMessageTimestamp().toString());
        conversationDTO.setUnreadCount(0); // Placeholder for unread count logic
        return conversationDTO;
    }
}