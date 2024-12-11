package com.FlickerDomain.flicker.controller;

import com.FlickerDomain.flicker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Kontroler odpowiedzialny za obsługę rejestracji użytkowników.
 * Obsługuje wyświetlanie formularza rejestracji oraz przetwarzanie danych rejestracyjnych.
 */
@Controller
public class RegistrationController {

    @Autowired
    private UserService userService; // Serwis odpowiedzialny za obsługę rejestracji użytkowników

    /**
     * Wyświetla formularz rejestracji użytkownika.
     *
     * @return nazwa widoku (plik HTML), który ma zostać wyświetlony (formularz rejestracji).
     */
    @RequestMapping("/register")
    public String showRegistrationForm() {
        return "register"; // Nazwa pliku HTML (register.html), zawierającego formularz rejestracji
    }

    /**
     * Przetwarza dane rejestracyjne użytkownika.
     * Zawiera logikę rejestracji nowego użytkownika, w tym zapisywanie danych w bazie.
     * Jeśli rejestracja jest udana, użytkownik zostaje przekierowany na stronę sukcesu.
     * W przypadku błędu, wyświetlany jest formularz rejestracji z komunikatem o błędzie.
     *
     * @param username nazwa użytkownika podana przez użytkownika.
     * @param email adres e-mail użytkownika podany podczas rejestracji.
     * @param password hasło użytkownika podane podczas rejestracji.
     * @param model model, który służy do przekazywania danych i komunikatów (np. o błędach).
     * @return nazwę widoku, który ma zostać wyświetlony po próbie rejestracji.
     */
    @PostMapping("/register")
    public String registerUser(@RequestParam("username") String username,
                               @RequestParam("email") String email,
                               @RequestParam("password") String password,
                               Model model) {
        try {
            // Rejestracja nowego użytkownika przy użyciu serwisu
            userService.registerNewUser(username, email, password);
            return "redirect:/success"; // Po rejestracji użytkownik jest przekierowywany na stronę sukcesu
        } catch (Exception e) {
            // Jeśli wystąpił błąd, dodajemy komunikat o błędzie i ponownie wyświetlamy formularz rejestracji
            model.addAttribute("error", "Rejestracja nie powiodła się: " + e.getMessage());
            return "register"; // Przekierowanie na stronę rejestracji z komunikatem o błędzie
        }
    }
}
