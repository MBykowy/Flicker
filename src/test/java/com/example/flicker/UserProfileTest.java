package com.example.flicker;


import com.FlickerDomain.flicker.model.User;
import com.FlickerDomain.flicker.repository.UserRepository;
import com.FlickerDomain.flicker.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserProfileTest {

    private UserService userService;
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userService = new UserService(userRepository, null, null);
    }

    @Test
    void testUpdateUserPicture_Success() {
        // Arrange
        String email = "test@example.com";
        String newPictureUrl = "http://new-picture-url.com";
        User user = new User();
        user.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // Act
        userService.updateUserPicture(email, newPictureUrl);

        // Assert
        assertEquals(newPictureUrl, user.getPicture());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testUpdateUserPicture_UserNotFound() {
        // Arrange
        String email = "nonexistent@example.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act & Assert
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            userService.updateUserPicture(email, "http://new-picture-url.com");
        });

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void testUpdateUserUsername_Success() {
        // Arrange
        String email = "test@example.com";
        String newUsername = "NewUsername";
        User user = new User();
        user.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // Act
        userService.updateUserUsername(email, newUsername);

        // Assert
        assertEquals(newUsername, user.getUsername());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testUpdateUserUsername_UserNotFound() {
        // Arrange
        String email = "nonexistent@example.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act & Assert
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            userService.updateUserUsername(email, "NewUsername");
        });

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void testUpdateUserBio_Success() {
        // Arrange
        String email = "test@example.com";
        String newBio = "This is a new bio";
        User user = new User();
        user.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // Act
        userService.updateUserBio(email, newBio);

        // Assert
        assertEquals(newBio, user.getBio());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testUpdateUserBio_UserNotFound() {
        // Arrange
        String email = "nonexistent@example.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act & Assert
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            userService.updateUserBio(email, "This is a new bio");
        });

        assertEquals("User not found", exception.getMessage());
    }
}
