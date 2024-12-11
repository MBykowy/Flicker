package com.FlickerDomain.flicker.dto;

/**
 * Klasa reprezentująca odpowiedź autentykacyjną, zawierającą token.
 * Ta klasa jest używana do przesyłania tokenu autentykacyjnego (np. JWT) w odpowiedzi po pomyślnym zalogowaniu użytkownika.
 */
public class AuthResponse {

    private String token; // Token autentykacyjny, np. JWT

    /**
     * Konstruktor klasy AuthResponse.
     * Inicjalizuje obiekt AuthResponse z podanym tokenem.
     *
     * @param token token autentykacyjny (np. JWT), który ma być przekazany w odpowiedzi.
     */
    public AuthResponse(String token) {
        this.token = token;
    }

    /**
     * Zwraca token autentykacyjny.
     *
     * @return token autentykacyjny (np. JWT).
     */
    public String getToken() {
        return token;
    }

    /**
     * Ustawia token autentykacyjny.
     *
     * @param token token autentykacyjny (np. JWT), który ma zostać ustawiony.
     */
    public void setToken(String token) {
        this.token = token;
    }
}
