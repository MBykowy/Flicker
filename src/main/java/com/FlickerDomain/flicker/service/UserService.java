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
 * Klasa {@link UserService} zarządza operacjami związanymi z użytkownikami w systemie.
 * Odpowiada za rejestrację, autentykację, aktualizację danych użytkowników oraz zarządzanie tokenami JWT.
 *
 * {@link UserService} współpracuje z repozytorium użytkowników {@link UserRepository}, encją użytkownika {@link User},
 * oraz zapewnia metody dla rejestracji nowych użytkowników, logowania, autentykacji oraz aktualizacji profilu użytkowników.
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    /**
     * Konstruktor klasy {@link UserService}.
     *
     * @param userRepository obiekt {@link UserRepository} do komunikacji z bazą danych.
     * @param passwordEncoder obiekt {@link PasswordEncoder} do haszowania haseł.
     * @param jwtProvider obiekt {@link JwtProvider} do generowania i weryfikacji tokenów JWT.
     */
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }

    /**
     * Rejestruje nowego użytkownika w systemie.
     *
     * Sprawdza, czy e-mail użytkownika nie jest już zajęty. Następnie tworzy nowego użytkownika,
     * haszuje jego hasło i zapisuje użytkownika w bazie danych.
     *
     * @param request dane rejestracyjne użytkownika {@link RegisterRequest}.
     * @throws IllegalArgumentException jeżeli e-mail jest już w użyciu.
     */
    public void register(RegisterRequest request) {
        // Sprawdzenie, czy użytkownik już istnieje w bazie na podstawie adresu e-mail
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }

        // Tworzenie nowego użytkownika
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // Haszowanie hasła przed zapisaniem
        userRepository.save(user); // Zapisanie użytkownika w bazie danych
    }

    /**
     * Aktualizuje zdjęcie profilowe użytkownika.
     *
     * Wyszukuje użytkownika po adresie e-mail, a następnie zapisuje nowy URL do jego zdjęcia profilowego.
     *
     * @param email adres e-mail użytkownika.
     * @param pictureUrl nowy URL do zdjęcia profilowego użytkownika.
     * @throws UsernameNotFoundException jeżeli użytkownik nie zostanie znaleziony.
     */
    public void updateUserPicture(String email, String pictureUrl) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setPicture(pictureUrl);
        userRepository.save(user);
    }

    /**
     * Przeprowadza proces autentykacji użytkownika na podstawie danych logowania.
     *
     * Sprawdza, czy użytkownik o podanym adresie e-mail istnieje w bazie danych i czy hasło
     * użytkownika pasuje do przechowywanego hasła. Jeśli dane są poprawne, generuje i zwraca token JWT.
     *
     * @param request dane logowania użytkownika {@link LoginRequest}.
     * @return token JWT użytkownika.
     * @throws UsernameNotFoundException jeżeli użytkownik nie zostanie znaleziony.
     * @throws BadCredentialsException jeżeli dane logowania są niepoprawne.
     */
    public String authenticate(LoginRequest request) {
        // Wyszukiwanie użytkownika po e-mailu
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Sprawdzenie, czy podane hasło zgadza się z zapisanym hasłem
        if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return jwtProvider.generateToken(user); // Generowanie i zwracanie tokenu JWT
        }

        throw new BadCredentialsException("Invalid credentials"); // Jeżeli hasła nie pasują
    }

    /**
     * Autentykacja użytkownika na podstawie nazwy użytkownika i hasła.
     *
     * Sprawdza, czy użytkownik o podanej nazwie użytkownika istnieje i czy podane hasło
     * pasuje do zapisanego hasła.
     *
     * @param username nazwa użytkownika.
     * @param password hasło użytkownika.
     * @return {@code true} jeśli dane są poprawne, w przeciwnym razie {@code false}.
     */
    public boolean authenticate(String username, String password) {
        Optional<User> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            // Sprawdzenie, czy podane hasło pasuje do zapisanych danych
            return passwordEncoder.matches(password, user.getPassword());
        }

        return false; // Użytkownik nie został znaleziony
    }

    /**
     * Autentykacja użytkownika na podstawie adresu e-mail i hasła.
     *
     * Sprawdza, czy użytkownik o podanym adresie e-mail istnieje i czy podane hasło
     * pasuje do zapisanego hasła.
     *
     * @param email adres e-mail użytkownika.
     * @param password hasło użytkownika.
     * @return {@code true} jeśli dane są poprawne, w przeciwnym razie {@code false}.
     */
    public boolean authenticateByEmail(String email, String password) {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            // Sprawdzenie, czy podane hasło pasuje do zapisanych danych
            boolean passwordMatches = passwordEncoder.matches(password, user.getPassword());

            System.out.println("Password match: " + passwordMatches);  // Debugowanie wyniku porównania
            return passwordMatches; // Zwrócenie wyniku porównania
        }

        System.out.println("User not found with email: " + email); // Debugowanie przypadku nieznalezienia użytkownika
        return false;
    }

    /**
     * Pobiera dane użytkownika na podstawie jego adresu e-mail.
     *
     * @param email adres e-mail użytkownika.
     * @return obiekt {@link User} odpowiadający użytkownikowi o podanym adresie e-mail.
     * @throws UsernameNotFoundException jeżeli użytkownik nie zostanie znaleziony.
     */
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    /**
     * Rejestruje nowego użytkownika z nazwą użytkownika, adresem e-mail i hasłem.
     *
     * Hasło powinno być hashowane przed zapisaniem. Użytkownik jest tworzony i zapisywany w bazie danych.
     *
     * @param username nazwa użytkownika.
     * @param email adres e-mail użytkownika.
     * @param password hasło użytkownika.
     */
    public void registerNewUser(String username, String email, String password) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password); // Hasło powinno być hashowane przed zapisaniem
        userRepository.save(user); // Zapisanie użytkownika w bazie danych
    }
}
