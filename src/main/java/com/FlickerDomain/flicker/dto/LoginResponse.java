// LoginResponse.java
package com.FlickerDomain.flicker.dto;

/**
 * Klasa reprezentująca odpowiedź logowania użytkownika.
 * Zawiera informacje o wyniku logowania oraz komunikacie informacyjnym.
 */
public class LoginResponse {

    private final boolean success; // Wynik logowania (czy logowanie się powiodło)
    private final String message; // Komunikat zwrotny dotyczący logowania

    /**
     * Konstruktor klasy LoginResponse.
     * Inicjalizuje obiekt odpowiedzi logowania z wynikiem oraz komunikatem.
     *
     * @param success wynik logowania (true, jeśli logowanie powiodło się, false w przeciwnym razie)
     * @param message komunikat zwrotny informujący o statusie logowania (np. "Logowanie udane" lub "Błędne dane logowania")
     */
    public LoginResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    /**
     * Zwraca wynik logowania.
     *
     * @return true, jeśli logowanie powiodło się, false w przeciwnym razie.
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Zwraca komunikat związany z logowaniem.
     *
     * @return komunikat informujący o statusie logowania.
     */
    public String getMessage() {
        return message;
    }
}
