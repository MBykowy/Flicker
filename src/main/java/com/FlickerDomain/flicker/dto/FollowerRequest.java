// FollowerRequest.java
package com.FlickerDomain.flicker.dto;

public class FollowerRequest {

    private String followerEmail; // Email użytkownika, który chce śledzić innego użytkownika
    private String followedEmail; // Email użytkownika, który ma być śledzony

    /**
     * Pobiera email użytkownika, który chce śledzić innego użytkownika.
     *
     * @return email użytkownika, który chce śledzić jako String.
     */
    public String getFollowerEmail() {
        return followerEmail;
    }

    /**
     * Ustawia email użytkownika, który chce śledzić innego użytkownika.
     *
     * @param followerEmail email użytkownika, który chce śledzić jako String.
     */
    public void setFollowerEmail(String followerEmail) {
        this.followerEmail = followerEmail;
    }

    /**
     * Pobiera email użytkownika, który ma być śledzony.
     *
     * @return email użytkownika, który ma być śledzony jako String.
     */
    public String getFollowedEmail() {
        return followedEmail;
    }

    /**
     * Ustawia email użytkownika, który ma być śledzony.
     *
     * @param followedEmail email użytkownika, który ma być śledzony jako String.
     */
    public void setFollowedEmail(String followedEmail) {
        this.followedEmail = followedEmail;
    }
}
