    package com.FlickerDomain.flicker.service;

    import com.FlickerDomain.flicker.model.User;                // For User entity
    import com.FlickerDomain.flicker.repository.UserRepository;      // For UserRepository interface
    import com.FlickerDomain.flicker.dto.RegisterRequest;            // For RegisterRequest DTO
    import com.FlickerDomain.flicker.dto.LoginRequest;               // For LoginRequest DTO
    import com.FlickerDomain.flicker.security.JwtProvider;           // For JwtProvider

    import org.springframework.security.authentication.BadCredentialsException;
    import org.springframework.security.core.userdetails.UsernameNotFoundException;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.stereotype.Service;

    import java.util.Optional;

    /**
     * Service class for managing user-related operations.
     */
    @Service
    public class UserService {

        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;
        private final JwtProvider jwtProvider;

        /**
         * Constructor for UserService.
         *
         * @param userRepository the user repository
         * @param passwordEncoder the password encoder
         * @param jwtProvider the JWT provider
         */
        public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtProvider jwtProvider) {
            this.userRepository = userRepository;
            this.passwordEncoder = passwordEncoder;
            this.jwtProvider = jwtProvider;
        }

        /**
         * Registers a new user.
         *
         * @param request the registration request containing user details
         * @throws IllegalArgumentException if the email is already in use
         */
        public void register(RegisterRequest request) {
            // Check if the user already exists by email
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new IllegalArgumentException("Email already in use");
            }

            // Create and save the new user
            User user = new User();
            user.setUsername(request.getUsername());
            user.setEmail(request.getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            userRepository.save(user);
        }

        /**
         * Authenticates a user and generates a JWT token.
         *
         * @param request the login request containing user credentials
         * @return the generated JWT token
         * @throws UsernameNotFoundException if the user is not found
         * @throws BadCredentialsException if the credentials are invalid
         */
        public String authenticate(LoginRequest request) {
            // Find the user by email
            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            // Check if the provided password matches the stored password
            if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                return jwtProvider.generateToken(user);
            }

            throw new BadCredentialsException("Invalid credentials");
        }

        /**
         * Authenticates a user by username and password.
         *
         * @param username the username
         * @param password the password
         * @return true if authentication is successful, false otherwise
         */
        public boolean authenticate(String username, String password) {
            Optional<User> optionalUser = userRepository.findByUsername(username);

            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                // Check if the provided password matches the stored password
                return passwordEncoder.matches(password, user.getPassword());
            }

            return false;
        }

        /**
         * Authenticates a user by email and password.
         *
         * @param email the email
         * @param password the password
         * @return true if authentication is successful, false otherwise
         */
        public boolean authenticateByEmail(String email, String password) {
            Optional<User> optionalUser = userRepository.findByEmail(email);

            if (optionalUser.isPresent()) {
                User user = optionalUser.get();

                // Compare the provided password with the stored hashed password
                boolean passwordMatches = passwordEncoder.matches(password, user.getPassword());

                System.out.println("Password match: " + passwordMatches);  // Check the comparison result

                return passwordMatches;  // Return whether the password matches
            }

            System.out.println("User not found with email: " + email);
            return false;  // If the user is not found
        }

        /**
         * Retrieves a user by email.
         *
         * @param email the email
         * @return the user
         * @throws UsernameNotFoundException if the user is not found
         */
        public User getUserByEmail(String email) {
            return userRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        }

        /**
         * Registers a new user with the provided details.
         *
         * @param username the username
         * @param email the email
         * @param password the password
         */
        public void registerNewUser(String username, String email, String password) {
            User user = new User();
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(password); // Don't forget to hash the password before saving
            userRepository.save(user);
        }

        /**
         * Updates the user's profile picture.
         *
         * @param email the email
         * @param pictureUrl the new picture URL
         * @throws UsernameNotFoundException if the user is not found
         */
        public void updateUserPicture(String email, String pictureUrl) {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            user.setPicture(pictureUrl);
            userRepository.save(user);
        }

        /**
         * Updates the user's username.
         *
         * @param email the email
         * @param username the new username
         * @throws UsernameNotFoundException if the user is not found
         */
        public void updateUserUsername(String email, String username) {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            if (user != null) {
                user.setUsername(username);
                userRepository.save(user);
            }
        }

        /**
         * Updates the user's bio.
         *
         * @param email the email
         * @param bio the new bio
         * @throws UsernameNotFoundException if the user is not found
         */
        public void updateUserBio(String email, String bio) {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            if (user != null) {
                user.setBio(bio);
                userRepository.save(user);
            }
        }
    }