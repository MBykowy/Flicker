package com.FlickerDomain.flicker.dto;

public class ConversationDTO {
    private Long id;
    private Long otherParticipant;
    private String lastMessage;
    private int unreadCount;

    public Long getOtherParticipant() {
        return otherParticipant;
    }

    public void setOtherParticipant(Long otherParticipant) {
        this.otherParticipant = otherParticipant;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }
}