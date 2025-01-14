// FollowController.java
package com.FlickerDomain.flicker.controller;

import com.FlickerDomain.flicker.dto.FollowerRequest;
import com.FlickerDomain.flicker.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/follow")
public class FollowController {

    @Autowired
    private FollowService followService;

    public FollowController(FollowService followService) {
        this.followService = followService;
    }

    @PostMapping("/follow")
    public ResponseEntity<Map<String, String>> followUser(@RequestBody FollowerRequest followerRequest) {
        followService.followUser(followerRequest.getFollowerEmail(), followerRequest.getFollowedEmail());
        Map<String, String> response = new HashMap<>();
        response.put("message", "Followed successfully");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/unfollow")
    public ResponseEntity<Map<String, String>> unfollowUser(@RequestBody FollowerRequest followerRequest) {
        followService.unfollowUser(followerRequest.getFollowerEmail(), followerRequest.getFollowedEmail());
        Map<String, String> response = new HashMap<>();
        response.put("message", "Unfollowed successfully");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> checkFollowStatus(@RequestParam("followerEmail") String followerEmail,
                                                     @RequestParam("followedEmail") String followedEmail) {
        boolean isFollowing = followService.checkFollowStatus(followerEmail, followedEmail);
        return ResponseEntity.ok(isFollowing);
    }
}