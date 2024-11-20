package com.FlickerDomain.flicker.controller;

import com.FlickerDomain.flicker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @Autowired
    private UserService userService; // Wcześniej zdefiniowany serwis

    @PostMapping("/login")
    public String loginUser(@RequestParam("username") String username,
                            @RequestParam("password") String password,
                            Model model) {
        boolean isAuthenticated = userService.authenticate(username, password);

        if (isAuthenticated) {
            return "redirect:/dashboard"; // Przekierowanie na stronę po zalogowaniu
        } else {
            model.addAttribute("error", "Nieprawidłowa nazwa użytkownika lub hasło.");
            return "login";
        }
    }
}

