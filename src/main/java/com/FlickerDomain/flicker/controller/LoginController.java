package com.FlickerDomain.flicker.controller;

import com.FlickerDomain.flicker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Kontroler odpowiedzialny za obsługę logowania użytkowników.
 * Obsługuje przyjmowanie danych logowania, autentykację użytkownika,
 * oraz przekierowanie na odpowiednią stronę w zależności od wyniku logowania.
 */
@Controller
public class LoginController {

    @Autowired
    private UserService userService; // Serwis odpowiedzialny za autentykację użytkownika

    /**
     * Metoda logowania użytkownika.
     * Sprawdza poprawność danych logowania (nazwę użytkownika i hasło).
     * Jeśli logowanie jest poprawne, użytkownik zostaje przekierowany na stronę "dashboard".
     * W przypadku błędnych danych, użytkownik zostaje ponownie przekierowany na stronę logowania
     * z komunikatem o błędzie.
     *
     * @param username nazwa użytkownika podana przez użytkownika.
     * @param password hasło użytkownika podane przez użytkownika.
     * @param model model, do którego dodawane są atrybuty (np. komunikat o błędzie).
     * @return widok (strona), na którą użytkownik zostanie przekierowany.
     */
    @PostMapping("/login")
    public String loginUser(@RequestParam("username") String username,
                            @RequestParam("password") String password,
                            Model model) {
        boolean isAuthenticated = userService.authenticate(username, password);

        if (isAuthenticated) {
            // Przekierowanie na stronę po pomyślnym logowaniu
            return "redirect:/dashboard";
        } else {
            // Dodanie komunikatu o błędzie do modelu i ponowne załadowanie strony logowania
            model.addAttribute("error", "Nieprawidłowa nazwa użytkownika lub hasło.");
            return "login";
        }
    }
}
