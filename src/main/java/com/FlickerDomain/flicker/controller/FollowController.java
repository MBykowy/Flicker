// FollowController.java
package com.FlickerDomain.flicker.controller;

import com.FlickerDomain.flicker.dto.FollowerRequest;
import com.FlickerDomain.flicker.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/follow")
public class FollowController {

    @Autowired
    private FollowService followService;

    public FollowController(FollowService followService) {
        this.followService = followService;
    }

    @PostMapping("/follow")
    public ResponseEntity<String> followUser(@RequestBody FollowerRequest followerRequest) {
        followService.followUser(followerRequest.getFollowerEmail(), followerRequest.getFollowedEmail());
        return ResponseEntity.ok("Followed successfully");
    }

    @PostMapping("/unfollow")
    public ResponseEntity<String> unfollowUser(@RequestBody FollowerRequest followerRequest) {
        followService.unfollowUser(followerRequest.getFollowerEmail(), followerRequest.getFollowedEmail());
        return ResponseEntity.ok("Unfollowed successfully");
    }

    @GetMapping("/check")
    public boolean checkFollowStatus(@RequestParam("followerEmail") String followerEmail,
                                     @RequestParam("followedEmail") String followedEmail) {
        return followService.checkFollowStatus(followerEmail, followedEmail);
    }
}