package com.example.flicker;

import com.FlickerDomain.flicker.service.FileStorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FileStorageServiceTest {

    private FileStorageService fileStorageService;

    @BeforeEach
    void setUp() throws Exception {
        // Przygotowanie przed każdym testem
        fileStorageService = new FileStorageService();
        Files.createDirectories(Path.of("uploads")); // Upewnienie się, że katalog istnieje
    }

    @Test
    void testConstructorCreatesDirectory() {
        // Sprawdzanie, czy konstruktor tworzy katalog
        assertTrue(Files.exists(Path.of("uploads")), "Katalog 'uploads' powinien zostać utworzony.");
    }

    @Test
    void testStoreFileSuccessfully() throws Exception {
        // Przygotowanie mocka MultipartFile
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getOriginalFilename()).thenReturn("testfile.txt");

        byte[] fileContent = "Test content".getBytes();
        InputStream fileStream = new ByteArrayInputStream(fileContent);
        when(mockFile.getInputStream()).thenReturn(fileStream);

        // Akcja
        String filePath = fileStorageService.storeFile(mockFile);

        // Walidacja
        assertEquals("/uploads/testfile.txt", filePath, "Zwrócona ścieżka powinna być poprawna.");
        assertTrue(Files.exists(Path.of("uploads/testfile.txt")), "Plik powinien zostać zapisany w katalogu 'uploads'.");

        // Czyszczenie
        Files.deleteIfExists(Path.of("uploads/testfile.txt"));
    }

    @Test
    void testStoreFileThrowsExceptionWhenErrorOccurs() throws Exception {
        // Przygotowanie mocka MultipartFile
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getOriginalFilename()).thenReturn("testfile.txt");
        when(mockFile.getInputStream()).thenThrow(new RuntimeException("Błąd podczas odczytu pliku."));

        // Walidacja
        Exception exception = assertThrows(RuntimeException.class, () -> fileStorageService.storeFile(mockFile));
        assertTrue(exception.getMessage().contains("Nie można zapisać pliku testfile.txt"), "Komunikat wyjątku powinien zawierać nazwę pliku.");
    }
}

