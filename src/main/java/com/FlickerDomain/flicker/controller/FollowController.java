package com.FlickerDomain.flicker.controller;

import com.FlickerDomain.flicker.service.FollowService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/follow")
public class FollowController {

    private final FollowService followService;

    public FollowController(FollowService followService) {
        this.followService = followService;
    }

    @PostMapping("/follow")
    public ResponseEntity<String> followUser(@RequestParam String followerEmail, @RequestParam String followedEmail) {
        followService.followUser(followerEmail, followedEmail);
        return ResponseEntity.ok("Followed successfully");
    }

    @PostMapping("/unfollow")
    public ResponseEntity<String> unfollowUser(@RequestParam String followerEmail, @RequestParam String followedEmail) {
        followService.unfollowUser(followerEmail, followedEmail);
        return ResponseEntity.ok("Unfollowed successfully");
    }
}