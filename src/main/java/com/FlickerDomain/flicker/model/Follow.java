package com.FlickerDomain.flicker.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

/**
 * Klasa Follow reprezentuje relację między dwoma użytkownikami w systemie, gdzie jeden użytkownik
 * obserwuje drugiego. Zawiera informacje o obserwującym oraz obserwowanym użytkowniku.
 */
@Entity
@JsonIgnoreProperties({"follower", "followed"})
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "follower_id", nullable = false)
    private User follower;

    @ManyToOne
    @JoinColumn(name = "followed_id", nullable = false)
    private User followed;

    /**
     * Pobiera ID relacji Follow.
     *
     * @return ID relacji Follow jako Long.
     */
    public Long getId() {
        return id;
    }

    /**
     * Ustawia ID relacji Follow.
     *
     * @param id ID relacji Follow jako Long.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Pobiera użytkownika, który obserwuje.
     *
     * @return obiekt User reprezentujący użytkownika, który obserwuje.
     */
    public User getFollower() {
        return follower;
    }

    /**
     * Ustawia użytkownika, który obserwuje.
     *
     * @param follower obiekt User reprezentujący użytkownika, który obserwuje.
     */
    public void setFollower(User follower) {
        this.follower = follower;
    }

    /**
     * Pobiera użytkownika, który jest obserwowany.
     *
     * @return obiekt User reprezentujący użytkownika, który jest obserwowany.
     */
    public User getFollowed() {
        return followed;
    }

    /**
     * Ustawia użytkownika, który jest obserwowany.
     *
     * @param followed obiekt User reprezentujący użytkownika, który jest obserwowany.
     */
    public void setFollowed(User followed) {
        this.followed = followed;
    }
}
