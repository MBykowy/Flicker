package com.FlickerDomain.flicker;

import com.FlickerDomain.flicker.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Znajdź użytkownika po adresie e-mail
    Optional<User> findByEmail(String email);

    // Znajdź użytkownika po nazwie użytkownika
    Optional<User> findByUsername(String username);

    // Sprawdź, czy użytkownik istnieje po adresie e-mail
    boolean existsByEmail(String email);

    // Sprawdź, czy użytkownik istnieje po nazwie użytkownika
    boolean existsByUsername(String username);
}
