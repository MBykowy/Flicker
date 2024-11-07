package com.FlickerDomain.flicker.controller;

import com.FlickerDomain.flicker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegistrationController {

    @Autowired
    private UserService userService; // Zakładamy, że masz serwis do obsługi rejestracji

    @RequestMapping("/register")
    public String showRegistrationForm() {
        return "register"; // Nazwa pliku HTML (register.html)
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam("username") String username,
                               @RequestParam("email") String email,
                               @RequestParam("password") String password,
                               Model model) {
        try {
            userService.registerNewUser(username, email, password);
            return "redirect:/success"; // Po rejestracji przekierowanie na stronę sukcesu
        } catch (Exception e) {
            model.addAttribute("error", "Rejestracja nie powiodła się: " + e.getMessage());
            return "register";
        }
    }
}
