package com.FlickerDomain.flicker.model;

import jakarta.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Klasa Post reprezentuje post w systemie.
 * Zawiera informacje o autorze, treści, multimediów, liczbie polubień oraz użytkownikach,
 * którzy polubili post.
 */
@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String content;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = new Date();

    private String mediaUrl;

    private int likes; // Liczba polubień posta

    @ManyToMany
    @JoinTable(
            name = "post_likes",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> likedBy = new HashSet<>();

    /**
     * Pobiera ID posta.
     *
     * @return ID posta jako Long
     */
    public Long getId() {
        return id;
    }

    /**
     * Ustawia ID posta.
     *
     * @param id ID posta jako Long
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Pobiera użytkownika, który utworzył post.
     *
     * @return obiekt User, który utworzył post
     */
    public User getUser() {
        return user;
    }

    /**
     * Ustawia użytkownika, który utworzył post.
     *
     * @param user obiekt User, który utworzył post
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Pobiera treść posta.
     *
     * @return treść posta jako String
     */
    public String getContent() {
        return content;
    }

    /**
     * Ustawia treść posta.
     *
     * @param content treść posta jako String
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Pobiera datę utworzenia posta.
     *
     * @return data utworzenia posta jako obiekt Date
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * Ustawia datę utworzenia posta.
     *
     * @param createdAt data utworzenia posta jako obiekt Date
     */
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Pobiera URL multimediów związanych z postem.
     *
     * @return URL multimediów jako String
     */
    public String getMediaUrl() {
        return mediaUrl;
    }

    /**
     * Ustawia URL multimediów związanych z postem.
     *
     * @param mediaUrl URL multimediów jako String
     */
    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    /**
     * Pobiera liczbę polubień posta.
     *
     * @return liczba polubień jako int
     */
    public int getLikes() {
        return likes;
    }

    /**
     * Ustawia liczbę polubień posta.
     *
     * @param likes liczba polubień jako int
     */
    public void setLikes(int likes) {
        this.likes = likes;
    }

    /**
     * Pobiera zestaw użytkowników, którzy polubili post.
     *
     * @return zestaw użytkowników jako Set<User>
     */
    public Set<User> getLikedBy() {
        return likedBy;
    }

    /**
     * Ustawia zestaw użytkowników, którzy polubili post.
     *
     * @param likedBy zestaw użytkowników jako Set<User>
     */
    public void setLikedBy(Set<User> likedBy) {
        this.likedBy = likedBy;
    }
}
