package com.FlickerDomain.flicker.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Conversation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "participant1_id", nullable = false)
    private User participant1;

    @ManyToOne
    @JoinColumn(name = "participant2_id", nullable = false)
    private User participant2;

    @Column(nullable = false)
    private LocalDateTime lastMessageTimestamp;

    /**
     * Pobiera ID rozmowy.
     *
     * @return ID rozmowy jako Long.
     */
    public Long getId() {
        return id;
    }

    /**
     * Ustawia ID rozmowy.
     *
     * @param id ID rozmowy jako Long.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Pobiera uczestnika 1 rozmowy.
     *
     * @return obiekt User reprezentujący pierwszego uczestnika rozmowy.
     */
    public User getParticipant1() {
        return participant1;
    }

    /**
     * Ustawia uczestnika 1 rozmowy.
     *
     * @param participant1 obiekt User reprezentujący pierwszego uczestnika rozmowy.
     */
    public void setParticipant1(User participant1) {
        this.participant1 = participant1;
    }

    /**
     * Pobiera uczestnika 2 rozmowy.
     *
     * @return obiekt User reprezentujący drugiego uczestnika rozmowy.
     */
    public User getParticipant2() {
        return participant2;
    }

    /**
     * Ustawia uczestnika 2 rozmowy.
     *
     * @param participant2 obiekt User reprezentujący drugiego uczestnika rozmowy.
     */
    public void setParticipant2(User participant2) {
        this.participant2 = participant2;
    }

    /**
     * Pobiera datę i godzinę ostatniej wiadomości w rozmowie.
     *
     * @return data i godzina ostatniej wiadomości jako obiekt LocalDateTime.
     */
    public LocalDateTime getLastMessageTimestamp() {
        return lastMessageTimestamp;
    }

    /**
     * Ustawia datę i godzinę ostatniej wiadomości w rozmowie.
     *
     * @param lastMessageTimestamp data i godzina ostatniej wiadomości jako obiekt LocalDateTime.
     */
    public void setLastMessageTimestamp(LocalDateTime lastMessageTimestamp) {
        this.lastMessageTimestamp = lastMessageTimestamp;
    }
}
