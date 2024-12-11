package com.FlickerDomain.flicker.dto;

/**
 * Klasa reprezentująca dane rejestracyjne użytkownika.
 * Zawiera informacje o nazwie użytkownika, adresie e-mail i haśle,
 * które są używane podczas procesu rejestracji użytkownika.
 */
public class RegisterRequest {

    private String username; // Nazwa użytkownika
    private String email; // Adres e-mail użytkownika
    private String password; // Hasło użytkownika

    /**
     * Zwraca nazwę użytkownika.
     *
     * @return nazwa użytkownika.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Ustawia nazwę użytkownika.
     *
     * @param username nazwa użytkownika, która ma zostać ustawiona.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Zwraca adres e-mail użytkownika.
     *
     * @return adres e-mail użytkownika.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Ustawia adres e-mail użytkownika.
     *
     * @param email adres e-mail użytkownika, który ma zostać ustawiony.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Zwraca hasło użytkownika.
     *
     * @return hasło użytkownika.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Ustawia hasło użytkownika.
     *
     * @param password hasło użytkownika, które ma zostać ustawione.
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
