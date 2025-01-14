// LoginRequest.java
package com.FlickerDomain.flicker.dto;

/**
 * Klasa reprezentująca dane logowania użytkownika.
 * Zawiera informacje o adresie e-mail, haśle oraz odpowiedzi CAPTCHA użytkownika.
 */
public class LoginRequest {

    private String email; // Adres e-mail użytkownika
    private String password; // Hasło użytkownika
    private String captchaResponse; // Odpowiedź użytkownika na CAPTCHA

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

    /**
     * Zwraca odpowiedź CAPTCHA użytkownika.
     *
     * @return odpowiedź CAPTCHA użytkownika.
     */
    public String getCaptchaResponse() {
        return captchaResponse;
    }

    /**
     * Ustawia odpowiedź CAPTCHA użytkownika.
     *
     * @param captchaResponse odpowiedź CAPTCHA użytkownika, która ma zostać ustawiona.
     */
    public void setCaptchaResponse(String captchaResponse) {
        this.captchaResponse = captchaResponse;
    }
}
