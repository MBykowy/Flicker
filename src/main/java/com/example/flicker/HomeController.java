package com.example.flicker;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/home")
    public String home() {
        return "index";
    }
    @GetMapping("/home-login")
    public String login() {
        return "login";
    }

    @GetMapping("/home-register")
    public String register() {
        return "register";
    }
}
