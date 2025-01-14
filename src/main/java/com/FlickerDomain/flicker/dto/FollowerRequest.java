package com.FlickerDomain.flicker.dto;

public class FollowerRequest {
    private String followerEmail;
    private String followedEmail;

    // Getters and setters
    public String getFollowerEmail() {
        return followerEmail;
    }

    public void setFollowerEmail(String followerEmail) {
        this.followerEmail = followerEmail;
    }

    public String getFollowedEmail() {
        return followedEmail;
    }

    public void setFollowedEmail(String followedEmail) {
        this.followedEmail = followedEmail;
    }
}