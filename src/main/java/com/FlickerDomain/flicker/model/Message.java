package com.FlickerDomain.flicker.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Klasa Message reprezentuje wiadomość wysłaną między użytkownikami w systemie.
 * Zawiera informacje o nadawcy, odbiorcy, treści wiadomości, konwersacji oraz czasie wysłania.
 */
@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;

    @ManyToOne
    @JoinColumn(name = "conversation_id", nullable = false)
    private Conversation conversation;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false)
    private boolean readStatus;

    /**
     * Pobiera ID wiadomości.
     *
     * @return ID wiadomości jako Long.
     */
    public Long getId() {
        return id;
    }

    /**
     * Ustawia ID wiadomości.
     *
     * @param id ID wiadomości jako Long.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Pobiera użytkownika, który wysłał wiadomość.
     *
     * @return obiekt User reprezentujący nadawcę wiadomości.
     */
    public User getSender() {
        return sender;
    }

    /**
     * Ustawia użytkownika, który wysłał wiadomość.
     *
     * @param sender obiekt User reprezentujący nadawcę wiadomości.
     */
    public void setSender(User sender) {
        this.sender = sender;
    }

    /**
     * Pobiera użytkownika, który otrzymał wiadomość.
     *
     * @return obiekt User reprezentujący odbiorcę wiadomości.
     */
    public User getReceiver() {
        return receiver;
    }

    /**
     * Ustawia użytkownika, który otrzymał wiadomość.
     *
     * @param receiver obiekt User reprezentujący odbiorcę wiadomości.
     */
    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    /**
     * Pobiera konwersację, do której należy wiadomość.
     *
     * @return obiekt Conversation reprezentujący konwersację.
     */
    public Conversation getConversation() {
        return conversation;
    }

    /**
     * Ustawia konwersację, do której należy wiadomość.
     *
     * @param conversation obiekt Conversation reprezentujący konwersację.
     */
    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

    /**
     * Pobiera treść wiadomości.
     *
     * @return treść wiadomości jako String.
     */
    public String getContent() {
        return content;
    }

    /**
     * Ustawia treść wiadomości.
     *
     * @param content treść wiadomości jako String.
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Pobiera czas wysłania wiadomości.
     *
     * @return czas wysłania wiadomości jako LocalDateTime.
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Ustawia czas wysłania wiadomości.
     *
     * @param timestamp czas wysłania wiadomości jako LocalDateTime.
     */
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Sprawdza, czy wiadomość została przeczytana.
     *
     * @return true, jeśli wiadomość została przeczytana, false w przeciwnym razie.
     */
    public boolean isReadStatus() {
        return readStatus;
    }

    /**
     * Ustawia status przeczytania wiadomości.
     *
     * @param readStatus status przeczytania wiadomości (true = przeczytana, false = nieprzeczytana).
     */
    public void setReadStatus(boolean readStatus) {
        this.readStatus = readStatus;
    }
}
