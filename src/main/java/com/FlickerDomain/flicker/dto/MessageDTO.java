package com.FlickerDomain.flicker.dto;

import java.time.LocalDateTime;

/**
 * Klasa reprezentująca wiadomość.
 * Zawiera informacje o nadawcy, odbiorcy, treści wiadomości, dacie i godzinie wysłania oraz statusie przeczytania.
 */
public class MessageDTO {
    private Long id; // ID wiadomości
    private Long senderId; // ID nadawcy wiadomości
    private Long receiverId; // ID odbiorcy wiadomości
    private String content; // Treść wiadomości
    private LocalDateTime timestamp; // Data i godzina wysłania wiadomości
    private boolean readStatus; // Status przeczytania wiadomości

    /**
     * Zwraca ID wiadomości.
     *
     * @return ID wiadomości
     */
    public Long getId() {
        return id;
    }

    /**
     * Ustawia ID wiadomości.
     *
     * @param id ID wiadomości
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Zwraca ID nadawcy wiadomości.
     *
     * @return ID nadawcy wiadomości
     */
    public Long getSenderId() {
        return senderId;
    }

    /**
     * Ustawia ID nadawcy wiadomości.
     *
     * @param senderId ID nadawcy wiadomości
     */
    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    /**
     * Zwraca ID odbiorcy wiadomości.
     *
     * @return ID odbiorcy wiadomości
     */
    public Long getReceiverId() {
        return receiverId;
    }

    /**
     * Ustawia ID odbiorcy wiadomości.
     *
     * @param receiverId ID odbiorcy wiadomości
     */
    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    /**
     * Zwraca treść wiadomości.
     *
     * @return treść wiadomości
     */
    public String getContent() {
        return content;
    }

    /**
     * Ustawia treść wiadomości.
     *
     * @param content treść wiadomości
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Zwraca datę i godzinę wysłania wiadomości.
     *
     * @return timestamp wiadomości
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Ustawia datę i godzinę wysłania wiadomości.
     *
     * @param timestamp timestamp wiadomości
     */
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Zwraca status przeczytania wiadomości.
     *
     * @return true jeśli wiadomość została przeczytana, false w przeciwnym razie
     */
    public boolean isReadStatus() {
        return readStatus;
    }

    /**
     * Ustawia status przeczytania wiadomości.
     *
     * @param readStatus status przeczytania wiadomości (true - przeczytana, false - nieprzeczytana)
     */
    public void setReadStatus(boolean readStatus) {
        this.readStatus = readStatus;
    }
}
