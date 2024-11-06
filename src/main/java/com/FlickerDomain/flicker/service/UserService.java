package com.FlickerDomain.flicker.service;

import com.FlickerDomain.flicker.User;                    // For User entity
import com.FlickerDomain.flicker.UserRepository;      // For UserRepository interface
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
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
    }

    public String authenticate(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return jwtProvider.generateToken(user);
        }
        throw new BadCredentialsException("Invalid credentials");
    }
}
