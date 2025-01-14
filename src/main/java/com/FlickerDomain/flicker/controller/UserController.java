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

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/user/details")
    public Map<String, Object> getUserDetails(@RequestParam String email) {
        return userService.getUserDetails(email);
    }
}