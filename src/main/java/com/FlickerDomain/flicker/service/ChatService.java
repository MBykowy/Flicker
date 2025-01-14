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

/**
 * Serwis {@link ChatService} odpowiedzialny za operacje związane z wiadomościami i rozmowami użytkowników.
 *
 * {@link ChatService} zapewnia funkcje umożliwiające wysyłanie wiadomości, pobieranie historii rozmów,
 * oznaczanie wiadomości jako przeczytanych oraz zarządzanie rozmowami użytkowników.
 *
 * @see MessageDTO
 * @see ConversationDTO
 * @see MessageRepository
 * @see ConversationRepository
 */
@Service
public class ChatService {

    /**
     * Repozytorium do zarządzania wiadomościami.
     */
    private final MessageRepository messageRepository;

    /**
     * Repozytorium do zarządzania rozmowami.
     */
    private final ConversationRepository conversationRepository;

    /**
     * Konstruktor klasy {@link ChatService}.
     *
     * Inicjalizuje serwis przyjmując repozytoria do zarządzania wiadomościami i rozmowami.
     *
     * @param messageRepository repozytorium do zarządzania wiadomościami.
     * @param conversationRepository repozytorium do zarządzania rozmowami.
     */
    public ChatService(MessageRepository messageRepository, ConversationRepository conversationRepository) {
        this.messageRepository = messageRepository;
        this.conversationRepository = conversationRepository;
    }

    /**
     * Wysyła wiadomość do odbiorcy.
     *
     * Tworzy obiekt {@link Message} na podstawie danych przekazanych w {@link MessageDTO},
     * ustawia nadawcę, odbiorcę, treść wiadomości, znacznik czasu i status przeczytania,
     * a następnie zapisuje wiadomość w bazie danych.
     *
     * @param messageDTO obiekt DTO reprezentujący wiadomość.
     */
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

    /**
     * Pobiera historię wiadomości dla danej rozmowy.
     *
     * Wyszukuje wszystkie wiadomości związane z identyfikatorem rozmowy i konwertuje je na listę
     * obiektów DTO {@link MessageDTO}.
     *
     * @param conversationId identyfikator rozmowy, której historię wiadomości chcemy pobrać.
     * @return lista wiadomości w postaci obiektów {@link MessageDTO}.
     */
    public List<MessageDTO> getChatHistory(Long conversationId) {
        List<Message> messages = messageRepository.findByConversationId(conversationId);
        return messages.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    /**
     * Oznacza wszystkie wiadomości jako przeczytane dla danego odbiorcy.
     *
     * Wyszukuje wszystkie nieprzeczytane wiadomości dla odbiorcy i zmienia ich status na "przeczytane",
     * a następnie zapisuje je w bazie danych.
     *
     * @param receiverId identyfikator odbiorcy, dla którego wiadomości mają zostać oznaczone jako przeczytane.
     */
    public void markMessagesAsRead(Long receiverId) {
        List<Message> unreadMessages = messageRepository.findByReceiverIdAndReadStatusFalse(receiverId);
        unreadMessages.forEach(message -> message.setReadStatus(true));
        messageRepository.saveAll(unreadMessages);
    }

    /**
     * Pobiera rozmowy użytkownika.
     *
     * Wyszukuje wszystkie rozmowy, w których dany użytkownik jest jednym z uczestników,
     * a następnie konwertuje je na listę obiektów DTO {@link ConversationDTO}.
     *
     * @param userId identyfikator użytkownika, dla którego rozmowy mają zostać pobrane.
     * @return lista rozmów w postaci obiektów {@link ConversationDTO}.
     */
    public List<ConversationDTO> getUserConversations(Long userId) {
        List<Conversation> conversations = conversationRepository.findByParticipant1IdOrParticipant2Id(userId, userId);
        return conversations.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    /**
     * Konwertuje obiekt {@link Message} na obiekt DTO {@link MessageDTO}.
     *
     * @param message obiekt {@link Message}, który ma zostać przekonwertowany na DTO.
     * @return obiekt DTO {@link MessageDTO}.
     */
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

    /**
     * Konwertuje obiekt {@link Conversation} na obiekt DTO {@link ConversationDTO}.
     *
     * @param conversation obiekt {@link Conversation}, który ma zostać przekonwertowany na DTO.
     * @return obiekt DTO {@link ConversationDTO}.
     */
    private ConversationDTO convertToDTO(Conversation conversation) {
        ConversationDTO conversationDTO = new ConversationDTO();
        conversationDTO.setId(conversation.getId());
        conversationDTO.setOtherParticipant(conversation.getParticipant1().getId());
        conversationDTO.setLastMessage(conversation.getLastMessageTimestamp().toString());

        // Fetch the last message content
        Message lastMessage = messageRepository.findTopByConversationOrderByTimestampDesc(conversation);
        if (lastMessage != null) {
            conversationDTO.setContent(lastMessage.getContent());
        } else {
            conversationDTO.setContent(""); // Default to empty string if no messages
        }

        conversationDTO.setUnreadCount(0); // Placeholder for unread count logic
        return conversationDTO;
    }
}
