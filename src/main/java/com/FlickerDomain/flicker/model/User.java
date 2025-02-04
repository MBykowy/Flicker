package com.FlickerDomain.flicker.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.io.Serializable;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Reprezentuje użytkownika w systemie.
 * Ta encja jest mapowana na tabelę w bazie danych za pomocą adnotacji JPA.
 */
@Entity  // Adnotacja wskazująca, że klasa jest encją JPA
@JsonIgnoreProperties({"followers", "following", "blockedUsers", "blockedByUsers"})

public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;
    private String password;
    private String bio;
    private String picture;

    @OneToMany(mappedBy = "followed")
    private Set<Follow> followers = new HashSet<>();

    @OneToMany(mappedBy = "follower")
    private Set<Follow> following = new HashSet<>();

    @OneToMany(mappedBy = "blocker")
    private Set<Block> blockedUsers = new HashSet<>();

    @OneToMany(mappedBy = "blocked")
    private Set<Block> blockedByUsers = new HashSet<>();


    @ElementCollection
    private List<String> followedBy = new ArrayList<>();


    // Getters and setters


    public void setFollowedBy(List<String> followedBy) {
        this.followedBy = followedBy;
    }

    public Set<Follow> getFollowers() {
        return followers;
    }

    public Set<Follow> getFollowing() {
        return following;
    }
    public List<String> getFollowedBy() {
        return followedBy;
    }




    /**
     * Pobiera unikalny identyfikator użytkownika.
     *
     * @return ID użytkownika
     */
    public Long getId() {
        return id;
    }

    /**
     * Ustawia unikalny identyfikator użytkownika.
     *
     * @param id ID użytkownika
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Pobiera nazwę użytkownika.
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
     * Pobiera email użytkownika.
     *
     * @return email użytkownika
     */
    public String getEmail() {
        return email;
    }

    /**
     * Ustawia email użytkownika.
     *
     * @param email email użytkownika
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Pobiera hasło użytkownika.
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
     * Pobiera biografię użytkownika.
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
     * Pobiera URL zdjęcia profilowego użytkownika.
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

    public Set<Block> getBlockedUsers() {
        return blockedUsers;
    }

    public void setBlockedUsers(Set<Block> blockedUsers) {
        this.blockedUsers = blockedUsers;
    }

    public Set<Block> getBlockedByUsers() {
        return blockedByUsers;
    }

    public void setBlockedByUsers(Set<Block> blockedByUsers) {
        this.blockedByUsers = blockedByUsers;
    }
}
