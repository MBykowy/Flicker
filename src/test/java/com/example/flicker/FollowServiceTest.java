package com.example.flicker;

import com.FlickerDomain.flicker.model.Follow;
import com.FlickerDomain.flicker.model.User;
import com.FlickerDomain.flicker.repository.FollowRepository;
import com.FlickerDomain.flicker.repository.UserRepository;
import com.FlickerDomain.flicker.service.FollowService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FollowServiceTest {

    @Mock
    private FollowRepository followRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private FollowService followService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFollowUser() {
        String followerEmail = "follower@example.com";
        String followedEmail = "followed@example.com";

        User follower = new User();
        follower.setEmail(followerEmail);

        User followed = new User();
        followed.setEmail(followedEmail);

        when(userRepository.findByEmail(followerEmail)).thenReturn(Optional.of(follower));
        when(userRepository.findByEmail(followedEmail)).thenReturn(Optional.of(followed));
        when(followRepository.existsByFollowerAndFollowed(follower, followed)).thenReturn(false);

        followService.followUser(followerEmail, followedEmail);

        verify(followRepository, times(1)).save(any(Follow.class));
    }

    @Test
    void testUnfollowUser() {
        String followerEmail = "follower@example.com";
        String followedEmail = "followed@example.com";

        User follower = new User();
        follower.setEmail(followerEmail);

        User followed = new User();
        followed.setEmail(followedEmail);

        when(userRepository.findByEmail(followerEmail)).thenReturn(Optional.of(follower));
        when(userRepository.findByEmail(followedEmail)).thenReturn(Optional.of(followed));

        followService.unfollowUser(followerEmail, followedEmail);

        verify(followRepository, times(1)).deleteByFollowerAndFollowed(follower, followed);
    }

    @Test
    void testCheckFollowStatus() {
        String followerEmail = "follower@example.com";
        String followedEmail = "followed@example.com";

        User follower = new User();
        follower.setEmail(followerEmail);

        User followed = new User();
        followed.setEmail(followedEmail);

        when(userRepository.findByEmail(followerEmail)).thenReturn(Optional.of(follower));
        when(userRepository.findByEmail(followedEmail)).thenReturn(Optional.of(followed));
        when(followRepository.existsByFollowerAndFollowed(follower, followed)).thenReturn(true);

        boolean isFollowing = followService.checkFollowStatus(followerEmail, followedEmail);

        assertTrue(isFollowing);
        verify(followRepository, times(1)).existsByFollowerAndFollowed(follower, followed);
    }

    @Test
    void testFollowUser_UserNotFound() {
        String followerEmail = "nonexistent@example.com";
        String followedEmail = "followed@example.com";

        when(userRepository.findByEmail(followerEmail)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () ->
                followService.followUser(followerEmail, followedEmail)
        );

        assertEquals("Follower not found", exception.getMessage());
        verify(followRepository, never()).save(any(Follow.class));
    }

    @Test
    void testUnfollowUser_UserNotFound() {
        String followerEmail = "nonexistent@example.com";
        String followedEmail = "followed@example.com";

        when(userRepository.findByEmail(followerEmail)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () ->
                followService.unfollowUser(followerEmail, followedEmail)
        );

        assertEquals("Follower not found", exception.getMessage());
        verify(followRepository, never()).deleteByFollowerAndFollowed(any(User.class), any(User.class));
    }
}
