// BlockUserRequest.java
package com.FlickerDomain.flicker.controller;

public class BlockUserRequest {
    private String blockerEmail;
    private String blockedEmail;

    // Getters and setters
    public String getBlockerEmail() {
        return blockerEmail;
    }

    public void setBlockerEmail(String blockerEmail) {
        this.blockerEmail = blockerEmail;
    }

    public String getBlockedEmail() {
        return blockedEmail;
    }

    public void setBlockedEmail(String blockedEmail) {
        this.blockedEmail = blockedEmail;
    }
}