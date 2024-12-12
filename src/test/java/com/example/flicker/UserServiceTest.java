package com.example.flicker;

import com.FlickerDomain.flicker.model.User;
import com.FlickerDomain.flicker.repository.UserRepository;
import com.FlickerDomain.flicker.dto.RegisterRequest;
import com.FlickerDomain.flicker.dto.LoginRequest;
import com.FlickerDomain.flicker.security.JwtProvider;
import com.FlickerDomain.flicker.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private static final Logger logger = Logger.getLogger(UserServiceTest.class.getName());
    private static final String GREEN = "\u001B[32m";
    private static final String RESET = "\u001B[0m";

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtProvider jwtProvider;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Register user successfully")
    void registerUserSuccessfully() {
        logger.info("Starting test: registerUserSuccessfully");
        RegisterRequest request = new RegisterRequest();
        request.setUsername("username");
        request.setEmail("email@example.com");
        request.setPassword("password");
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");

        userService.register(request);

        verify(userRepository, times(1)).save(any(User.class));
        System.out.println(GREEN + "Test registerUserSuccessfully completed successfully" + RESET);
    }

    @Test
    @DisplayName("Register user with existing email throws exception")
    void registerUserWithExistingEmailThrowsException() {
        logger.info("Starting test: registerUserWithExistingEmailThrowsException");
        RegisterRequest request = new RegisterRequest();
        request.setUsername("username");
        request.setEmail("email@example.com");
        request.setPassword("password");
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> userService.register(request));
        System.out.println(GREEN + "Test registerUserWithExistingEmailThrowsException completed successfully" + RESET);
    }

    @Test
    @DisplayName("Authenticate user successfully")
    void authenticateUserSuccessfully() {
        logger.info("Starting test: authenticateUserSuccessfully");
        LoginRequest request = new LoginRequest();
        request.setEmail("email@example.com");
        request.setPassword("password");
        User user = new User();
        user.setPassword("encodedPassword");
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.getPassword(), user.getPassword())).thenReturn(true);
        when(jwtProvider.generateToken(user)).thenReturn("jwtToken");

        String token = userService.authenticate(request);

        assertEquals("jwtToken", token);
        System.out.println(GREEN + "Test authenticateUserSuccessfully completed successfully" + RESET);
    }

    @Test
    @DisplayName("Authenticate user with invalid credentials throws exception")
    void authenticateUserWithInvalidCredentialsThrowsException() {
        logger.info("Starting test: authenticateUserWithInvalidCredentialsThrowsException");
        LoginRequest request = new LoginRequest();
        request.setEmail("email@example.com");
        request.setPassword("wrongPassword");
        User user = new User();
        user.setPassword("encodedPassword");
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.getPassword(), user.getPassword())).thenReturn(false);

        assertThrows(BadCredentialsException.class, () -> userService.authenticate(request));
        System.out.println(GREEN + "Test authenticateUserWithInvalidCredentialsThrowsException completed successfully" + RESET);
    }

    @Test
    @DisplayName("Authenticate user by email successfully")
    void authenticateUserByEmailSuccessfully() {
        logger.info("Starting test: authenticateUserByEmailSuccessfully");
        String email = "email@example.com";
        String password = "password";
        User user = new User();
        user.setPassword("encodedPassword");
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, user.getPassword())).thenReturn(true);

        boolean isAuthenticated = userService.authenticateByEmail(email, password);

        assertTrue(isAuthenticated);
        System.out.println(GREEN + "Test authenticateUserByEmailSuccessfully completed successfully" + RESET);
    }

    @Test
    @DisplayName("Authenticate user by email with invalid credentials")
    void authenticateUserByEmailWithInvalidCredentials() {
        logger.info("Starting test: authenticateUserByEmailWithInvalidCredentials");
        String email = "email@example.com";
        String password = "wrongPassword";
        User user = new User();
        user.setPassword("encodedPassword");
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, user.getPassword())).thenReturn(false);

        boolean isAuthenticated = userService.authenticateByEmail(email, password);

        assertFalse(isAuthenticated);
        System.out.println(GREEN + "Test authenticateUserByEmailWithInvalidCredentials completed successfully" + RESET);
    }

    @Test
    @DisplayName("Get user by email successfully")
    void getUserByEmailSuccessfully() {
        logger.info("Starting test: getUserByEmailSuccessfully");
        String email = "email@example.com";
        User user = new User();
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        User foundUser = userService.getUserByEmail(email);

        assertEquals(user, foundUser);
        System.out.println(GREEN + "Test getUserByEmailSuccessfully completed successfully" + RESET);
    }

    @Test
    @DisplayName("Get user by email throws exception when user not found")
    void getUserByEmailThrowsExceptionWhenUserNotFound() {
        logger.info("Starting test: getUserByEmailThrowsExceptionWhenUserNotFound");
        String email = "email@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.getUserByEmail(email));
        System.out.println(GREEN + "Test getUserByEmailThrowsExceptionWhenUserNotFound completed successfully" + RESET);
    }

    @Test
    @DisplayName("Update user picture successfully")
    void updateUserPictureSuccessfully() {
        logger.info("Starting test: updateUserPictureSuccessfully");
        String email = "email@example.com";
        String pictureUrl = "newPictureUrl";
        User user = new User();
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        userService.updateUserPicture(email, pictureUrl);

        assertEquals(pictureUrl, user.getPicture());
        verify(userRepository, times(1)).save(user);
        System.out.println(GREEN + "Test updateUserPictureSuccessfully completed successfully" + RESET);
    }

    @Test
    @DisplayName("Update user username successfully")
    void updateUserUsernameSuccessfully() {
        logger.info("Starting test: updateUserUsernameSuccessfully");
        String email = "email@example.com";
        String username = "newUsername";
        User user = new User();
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        userService.updateUserUsername(email, username);

        assertEquals(username, user.getUsername());
        verify(userRepository, times(1)).save(user);
        System.out.println(GREEN + "Test updateUserUsernameSuccessfully completed successfully" + RESET);
    }

    @Test
    @DisplayName("Update user bio successfully")
    void updateUserBioSuccessfully() {
        logger.info("Starting test: updateUserBioSuccessfully");
        String email = "email@example.com";
        String bio = "newBio";
        User user = new User();
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        userService.updateUserBio(email, bio);

        assertEquals(bio, user.getBio());
        verify(userRepository, times(1)).save(user);
        System.out.println(GREEN + "Test updateUserBioSuccessfully completed successfully" + RESET);
    }
}