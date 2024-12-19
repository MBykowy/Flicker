package com.FlickerDomain.flicker.controller;

/**
 * Klasa PostRequest reprezentuje żądanie do tworzenia posta.
 * Zawiera dane takie jak email użytkownika, treść posta i URL multimediów.
 */
public class PostRequest {

    private String email;
    private String content;
    private String mediaUrl;

    /**
     * Pobiera email użytkownika.
     *
     * @return email użytkownika jako String
     */
    public String getEmail() {
        return email;
    }

    /**
     * Ustawia email użytkownika.
     *
     * @param email email użytkownika jako String
     */
    public void setEmail(String email) {
        this.email = email;
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
}
