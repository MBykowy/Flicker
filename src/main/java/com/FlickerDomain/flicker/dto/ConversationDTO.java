// ConversationDTO.java
package com.FlickerDomain.flicker.dto;

public class ConversationDTO {

    private Long id; // Unikalny identyfikator konwersacji
    private Long otherParticipant; // ID drugiego uczestnika konwersacji
    private String lastMessage; // Treść ostatniej wiadomości w konwersacji
    private int unreadCount; // Liczba nieprzeczytanych wiadomości
    private String content; // Dodatkowe pole, zawierające treść konwersacji

    /**
     * Pobiera identyfikator konwersacji.
     *
     * @return id konwersacji jako Long.
     */
    public Long getId() {
        return id;
    }

    /**
     * Ustawia identyfikator konwersacji.
     *
     * @param id identyfikator konwersacji jako Long.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Pobiera identyfikator drugiego uczestnika konwersacji.
     *
     * @return identyfikator drugiego uczestnika jako Long.
     */
    public Long getOtherParticipant() {
        return otherParticipant;
    }

    /**
     * Ustawia identyfikator drugiego uczestnika konwersacji.
     *
     * @param otherParticipant identyfikator drugiego uczestnika konwersacji jako Long.
     */
    public void setOtherParticipant(Long otherParticipant) {
        this.otherParticipant = otherParticipant;
    }

    /**
     * Pobiera treść ostatniej wiadomości w konwersacji.
     *
     * @return treść ostatniej wiadomości jako String.
     */
    public String getLastMessage() {
        return lastMessage;
    }

    /**
     * Ustawia treść ostatniej wiadomości w konwersacji.
     *
     * @param lastMessage treść ostatniej wiadomości jako String.
     */
    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    /**
     * Pobiera liczbę nieprzeczytanych wiadomości w konwersacji.
     *
     * @return liczba nieprzeczytanych wiadomości jako int.
     */
    public int getUnreadCount() {
        return unreadCount;
    }

    /**
     * Ustawia liczbę nieprzeczytanych wiadomości w konwersacji.
     *
     * @param unreadCount liczba nieprzeczytanych wiadomości jako int.
     */
    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    /**
     * Pobiera treść konwersacji.
     *
     * @return treść konwersacji jako String.
     */
    public String getContent() {
        return content;
    }

    /**
     * Ustawia treść konwersacji.
     *
     * @param content treść konwersacji jako String.
     */
    public void setContent(String content) {
        this.content = content;
    }
}
