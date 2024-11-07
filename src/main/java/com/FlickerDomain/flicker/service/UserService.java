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

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }

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



    public void registerNewUser(String username, String email, String password) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password); // Nie zapomnij o hashowaniu hasła przed zapisem
        userRepository.save(user);
    }
}
