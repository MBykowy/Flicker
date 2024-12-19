package com.FlickerDomain.flicker.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * Klasa FileStorageService zapewnia funkcjonalności związane z przechowywaniem plików.
 * Tworzy katalog na pliki i umożliwia zapis przesłanych plików.
 */
@Service
public class FileStorageService {

    private final Path fileStorageLocation = Paths.get("uploads").toAbsolutePath().normalize();

    /**
     * Konstruktor klasy FileStorageService.
     * Tworzy katalog, w którym będą przechowywane przesłane pliki.
     * W przypadku błędu podczas tworzenia katalogu, zgłasza wyjątek RuntimeException.
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
     * @param file plik przesłany jako obiekt MultipartFile
     * @return ścieżka URL do zapisanego pliku
     * @throws RuntimeException w przypadku błędu podczas zapisu pliku
     */
    public String storeFile(MultipartFile file) {
        try {
            Path targetLocation = this.fileStorageLocation.resolve(file.getOriginalFilename());
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return "/uploads/" + file.getOriginalFilename();
        } catch (Exception ex) {
            throw new RuntimeException("Nie można zapisać pliku " + file.getOriginalFilename() + ". Spróbuj ponownie!", ex);
        }
    }
}
