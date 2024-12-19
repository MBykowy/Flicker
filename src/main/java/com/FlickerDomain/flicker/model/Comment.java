package com.FlickerDomain.flicker.model;

import jakarta.persistence.*;
import java.util.Date;

/**
 * Klasa Comment reprezentuje komentarz w systemie.
 * Zawiera informacje o treści komentarza, autorze, poście, do którego należy,
 * oraz dacie utworzenia.
 */
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String content;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = new Date();

    /**
     * Pobiera ID komentarza.
     *
     * @return ID komentarza jako Long
     */
    public Long getId() {
        return id;
    }

    /**
     * Ustawia ID komentarza.
     *
     * @param id ID komentarza jako Long
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Pobiera post, do którego należy komentarz.
     *
     * @return obiekt Post, do którego należy komentarz
     */
    public Post getPost() {
        return post;
    }

    /**
     * Ustawia post, do którego należy komentarz.
     *
     * @param post obiekt Post, do którego należy komentarz
     */
    public void setPost(Post post) {
        this.post = post;
    }

    /**
     * Pobiera użytkownika, który napisał komentarz.
     *
     * @return obiekt User, który napisał komentarz
     */
    public User getUser() {
        return user;
    }

    /**
     * Ustawia użytkownika, który napisał komentarz.
     *
     * @param user obiekt User, który napisał komentarz
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Pobiera treść komentarza.
     *
     * @return treść komentarza jako String
     */
    public String getContent() {
        return content;
    }

    /**
     * Ustawia treść komentarza.
     *
     * @param content treść komentarza jako String
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Pobiera datę utworzenia komentarza.
     *
     * @return data utworzenia komentarza jako obiekt Date
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * Ustawia datę utworzenia komentarza.
     *
     * @param createdAt data utworzenia komentarza jako obiekt Date
     */
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
