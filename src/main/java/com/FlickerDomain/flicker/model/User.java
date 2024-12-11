package com.FlickerDomain.flicker.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.io.Serializable;

/**
 * Reprezentuje użytkownika w systemie.
 * Klasa {@link User} jest encją JPA, co oznacza, że będzie mapowana na tabelę w bazie danych.
 * Zawiera dane użytkownika, takie jak identyfikator, nazwa użytkownika, e-mail, hasło, biografia oraz zdjęcie.
 *
 * Używa adnotacji {@link Entity}, aby wskazać, że jest encją JPA.
 * @see javax.persistence.Entity
 */
@Entity  // This annotation is important!
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Unikalny identyfikator użytkownika

    private String username;  // Nazwa użytkownika
    private String email;  // E-mail użytkownika
    private String password;  // Hasło użytkownika
    private String bio;  // Biografia użytkownika
    private String picture;  // URL zdjęcia profilowego użytkownika

    /**
     * Zwraca unikalny identyfikator użytkownika.
     *
     * @return id użytkownika
     */
    public Long getId() {
        return id;
    }

    /**
     * Ustawia unikalny identyfikator użytkownika.
     *
     * @param id unikalny identyfikator użytkownika
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Zwraca nazwę użytkownika.
     *
     * @return nazwa użytkownika
     */
    public String getUsername() {
        return username;
    }

    /**
     * Ustawia nazwę użytkownika.
     *
     * @param username nazwa użytkownika
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Zwraca adres e-mail użytkownika.
     *
     * @return adres e-mail użytkownika
     */
    public String getEmail() {
        return email;
    }

    /**
     * Ustawia adres e-mail użytkownika.
     *
     * @param email adres e-mail użytkownika
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Zwraca hasło użytkownika.
     *
     * @return hasło użytkownika
     */
    public String getPassword() {
        return password;
    }

    /**
     * Ustawia hasło użytkownika.
     *
     * @param password hasło użytkownika
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Zwraca biografię użytkownika.
     *
     * @return biografia użytkownika
     */
    public String getBio() {
        return bio;
    }

    /**
     * Ustawia biografię użytkownika.
     *
     * @param bio biografia użytkownika
     */
    public void setBio(String bio) {
        this.bio = bio;
    }

    /**
     * Zwraca URL zdjęcia profilowego użytkownika.
     *
     * @return URL zdjęcia profilowego użytkownika
     */
    public String getPicture() {
        return picture;
    }

    /**
     * Ustawia URL zdjęcia profilowego użytkownika.
     *
     * @param picture URL zdjęcia profilowego użytkownika
     */
    public void setPicture(String picture) {
        this.picture = picture;
    }
}
