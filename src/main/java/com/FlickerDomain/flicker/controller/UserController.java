// UserController.java
package com.FlickerDomain.flicker.controller;

import com.FlickerDomain.flicker.model.User;
import com.FlickerDomain.flicker.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    /**
     * Konstruktor inicjalizujący serwis użytkowników.
     *
     * @param userService serwis odpowiedzialny za operacje na użytkownikach
     */
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Pobiera wszystkich użytkowników z bazy danych.
     *
     * @return lista wszystkich użytkowników
     */
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    /**
     * Pobiera szczegóły użytkownika na podstawie adresu e-mail.
     *
     * @param email adres e-mail użytkownika, którego szczegóły mają zostać pobrane
     * @return mapa zawierająca szczegóły użytkownika
     */
    @GetMapping("/user/details")
    public Map<String, Object> getUserDetails(@RequestParam String email) {
        return userService.getUserDetails(email);
    }
}
