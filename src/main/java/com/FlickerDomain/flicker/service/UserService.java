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

    public boolean authenticate(String username, String password) {
        Optional<User> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            // Sprawdzamy, czy podane hasło pasuje do zahashowanego hasła w bazie
            return passwordEncoder.matches(password, user.getPassword());
        }

        return false;
    }

    public boolean authenticateByEmail(String email, String password) {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            // Porównanie hasła wprowadzonego przez użytkownika z zahashowanym hasłem
            boolean passwordMatches = passwordEncoder.matches(password, user.getPassword());

            System.out.println("Password match: " + passwordMatches);  // Sprawdzenie wyniku porównania

            return passwordMatches;  // Zwrócenie, czy hasło pasuje
        }

        System.out.println("User not found with email: " + email);
        return false;  // Jeżeli użytkownik nie został znaleziony
    }



    public void registerNewUser(String username, String email, String password) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password); // Nie zapomnij o hashowaniu hasła przed zapisem
        userRepository.save(user);
    }
}
