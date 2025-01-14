package com.FlickerDomain.flicker.service;

import com.FlickerDomain.flicker.model.Follow;
import com.FlickerDomain.flicker.model.User;
import com.FlickerDomain.flicker.repository.FollowRepository;
import com.FlickerDomain.flicker.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    public FollowService(FollowRepository followRepository, UserRepository userRepository) {
        this.followRepository = followRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void followUser(String followerEmail, String followedEmail) {
        User follower = userRepository.findByEmail(followerEmail)
                .orElseThrow(() -> new RuntimeException("Follower not found"));
        User followed = userRepository.findByEmail(followedEmail)
                .orElseThrow(() -> new RuntimeException("Followed user not found"));

        if (!followRepository.existsByFollowerAndFollowed(follower, followed)) {
            Follow follow = new Follow();
            follow.setFollower(follower);
            follow.setFollowed(followed);
            followRepository.save(follow);
        }
    }

    @Transactional
    public void unfollowUser(String followerEmail, String followedEmail) {
        User follower = userRepository.findByEmail(followerEmail)
                .orElseThrow(() -> new RuntimeException("Follower not found"));
        User followed = userRepository.findByEmail(followedEmail)
                .orElseThrow(() -> new RuntimeException("Followed user not found"));

        followRepository.deleteByFollowerAndFollowed(follower, followed);
    }
    public boolean checkFollowStatus(String followerEmail, String followedEmail) {
        User follower = userRepository.findByEmail(followerEmail)
                .orElseThrow(() -> new RuntimeException("Follower not found"));
        User followed = userRepository.findByEmail(followedEmail)
                .orElseThrow(() -> new RuntimeException("Followed user not found"));

        return followRepository.existsByFollowerAndFollowed(follower, followed);
    }
}