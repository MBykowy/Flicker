package com.FlickerDomain.flicker.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Klasa obsługująca globalne wyjątki w aplikacji.
 * Jest używana do przechwytywania określonych wyjątków i zwracania odpowiednich odpowiedzi HTTP
 * w przypadku wystąpienia tych wyjątków w aplikacji.
 * {@link RestControllerAdvice} pozwala na obsługę wyjątków w całej aplikacji,
 * zwracając odpowiednie odpowiedzi HTTP.
 */
@RestControllerAdvice
public class CustomExceptionHandler {

    /**
     * Obsługuje wyjątek {@link UsernameNotFoundException}, który jest zgłaszany, gdy
     * użytkownik nie zostanie znaleziony w bazie danych.
     *
     * @param ex wyjątek {@link UsernameNotFoundException}, który zawiera komunikat o błędzie
     * @return odpowiedź HTTP z kodem statusu 404 (Not Found) i komunikatem błędu
     */
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<String> handleUsernameNotFound(UsernameNotFoundException ex) {
        // Zwraca odpowiedź z kodem 404 i komunikatem z wyjątku
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    /**
     * Obsługuje wyjątek {@link BadCredentialsException}, który jest zgłaszany, gdy
     * dane uwierzytelniające (np. hasło) są niepoprawne.
     *
     * @param ex wyjątek {@link BadCredentialsException}, który zawiera komunikat o błędzie
     * @return odpowiedź HTTP z kodem statusu 401 (Unauthorized) i komunikatem błędu
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleBadCredentials(BadCredentialsException ex) {
        // Zwraca odpowiedź z kodem 401 i komunikatem z wyjątku
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    /**
     * Obsługuje ogólne wyjątki typu {@link Exception}.
     * Jest to metoda domyślna, która przechwytuje wszystkie inne wyjątki, które nie zostały
     * specjalnie obsłużone przez inne metody.
     *
     * @param ex ogólny wyjątek {@link Exception}, który zawiera szczegóły błędu
     * @return odpowiedź HTTP z kodem statusu 500 (Internal Server Error) i komunikatem błędu
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        // Zwraca odpowiedź z kodem 500 i komunikatem ogólnego wyjątku
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
