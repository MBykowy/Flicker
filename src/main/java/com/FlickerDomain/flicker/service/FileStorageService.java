package com.FlickerDomain.flicker.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * Klasa {@link FileStorageService} zapewnia funkcjonalności związane z przechowywaniem plików.
 * Tworzy katalog na pliki i umożliwia zapis przesłanych plików.
 *
 * {@link FileStorageService} jest odpowiedzialna za zapisanie pliku przesłanego przez użytkownika
 * w lokalnym systemie plików. Zapewnia także ścieżkę URL do zapisanych plików, które mogą być później
 * dostępne przez aplikację.
 */
@Service
public class FileStorageService {

    /**
     * Ścieżka katalogu, w którym będą przechowywane pliki.
     * Jest to lokalizacja oparte na katalogu "uploads".
     */
    private final Path fileStorageLocation = Paths.get("uploads").toAbsolutePath().normalize();

    /**
     * Konstruktor klasy {@link FileStorageService}.
     * Tworzy katalog, w którym będą przechowywane przesłane pliki.
     * W przypadku błędu podczas tworzenia katalogu, zgłasza wyjątek {@link RuntimeException}.
     *
     * @throws RuntimeException jeżeli nie uda się utworzyć katalogu do przechowywania plików.
     */
    public FileStorageService() {
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Nie można utworzyć katalogu, w którym będą przechowywane przesłane pliki.", ex);
        }
    }

    /**
     * Przechowuje przesłany plik w lokalnym systemie plików.
     *
     * Zapisuje plik w katalogu "uploads" i zwraca URL do pliku, który może być użyty do uzyskania dostępu do pliku.
     *
     * @param file plik przesłany jako obiekt {@link MultipartFile}.
     * @return ścieżka URL do zapisanego pliku (np. "/uploads/filename").
     * @throws RuntimeException jeżeli wystąpi błąd podczas zapisywania pliku.
     */
    public String storeFile(MultipartFile file) {
        try {
            // Określenie ścieżki do docelowego pliku w katalogu
            Path targetLocation = this.fileStorageLocation.resolve(file.getOriginalFilename());

            // Zapisanie pliku w lokalizacji
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // Zwrócenie ścieżki URL do zapisanego pliku
            return "/uploads/" + file.getOriginalFilename();
        } catch (Exception ex) {
            throw new RuntimeException("Nie można zapisać pliku " + file.getOriginalFilename() + ". Spróbuj ponownie!", ex);
        }
    }
}
