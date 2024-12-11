package com.FlickerDomain.flicker.repository;

import com.FlickerDomain.flicker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repozytorium dla encji {@link User}.
 * Interfejs {@link UserRepository} rozszerza {@link JpaRepository}, co zapewnia dostęp do podstawowych operacji CRUD
 * oraz pozwala na tworzenie zapytań specyficznych dla użytkownika.
 *
 * {@link JpaRepository} zapewnia metody do obsługi baz danych, takie jak zapisywanie, usuwanie, modyfikowanie i
 * wyszukiwanie obiektów. Dodatkowo, ten interfejs umożliwia definiowanie niestandardowych metod wyszukiwania
 * użytkowników według różnych kryteriów.
 *
 * @see JpaRepository
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {  // Changed Integer to Long

    /**
     * Znajduje użytkownika na podstawie adresu e-mail.
     *
     * @param email adres e-mail użytkownika
     * @return opcjonalny obiekt użytkownika, który może być pusty, jeśli użytkownik nie istnieje
     */
    Optional<User> findByEmail(String email);

    /**
     * Znajduje użytkownika na podstawie nazwy użytkownika.
     *
     * @param username nazwa użytkownika
     * @return opcjonalny obiekt użytkownika, który może być pusty, jeśli użytkownik nie istnieje
     */
    Optional<User> findByUsername(String username);

    /**
     * Sprawdza, czy użytkownik istnieje na podstawie adresu e-mail.
     *
     * @param email adres e-mail użytkownika
     * @return true, jeśli użytkownik z danym e-mailem istnieje, w przeciwnym razie false
     */
    boolean existsByEmail(String email);

    /**
     * Sprawdza, czy użytkownik istnieje na podstawie nazwy użytkownika.
     *
     * @param username nazwa użytkownika
     * @return true, jeśli użytkownik z daną nazwą użytkownika istnieje, w przeciwnym razie false
     */
    boolean existsByUsername(String username);
}
