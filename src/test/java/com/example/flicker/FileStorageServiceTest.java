package com.example.flicker;

import com.FlickerDomain.flicker.service.FileStorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FileStorageServiceTest {

    private FileStorageService fileStorageService;

    @Mock
    private MultipartFile multipartFile;

    @BeforeEach
    void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        // Tworzymy instancję klasy FileStorageService
        fileStorageService = new FileStorageService();

        // Tworzymy katalog "uploads" do testów
        Path uploadsDir = Path.of("uploads").toAbsolutePath().normalize();
        Files.createDirectories(uploadsDir);
    }

    @Test
    void shouldStoreFileSuccessfully() throws IOException {
        // Przygotowanie danych testowych
        String fileName = "testfile.txt";
        byte[] fileContent = "Test file content".getBytes();

        when(multipartFile.getOriginalFilename()).thenReturn(fileName);
        when(multipartFile.getInputStream()).thenReturn(new ByteArrayInputStream(fileContent));

        // Wywołanie metody
        String storedFilePath = fileStorageService.storeFile(multipartFile);

        // Sprawdzenie wyników
        assertEquals("/uploads/" + fileName, storedFilePath);
        Path expectedPath = Path.of("uploads").toAbsolutePath().resolve(fileName);
        assertTrue(Files.exists(expectedPath), "Plik powinien zostać zapisany na dysku");

        // Usunięcie pliku po teście
        Files.deleteIfExists(expectedPath);
    }

    @Test
    void shouldThrowExceptionWhenFileStorageFails() throws IOException {
        // Przygotowanie danych testowych
        String fileName = "testfile.txt";
        when(multipartFile.getOriginalFilename()).thenReturn(fileName);
        when(multipartFile.getInputStream()).thenThrow(new IOException("Mocked IOException"));

        // Sprawdzenie, czy wyjątek został zgłoszony
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            fileStorageService.storeFile(multipartFile);
        });

        assertTrue(exception.getMessage().contains("Nie można zapisać pliku"));
    }

    @Test
    void shouldThrowExceptionWhenCreatingDirectoryFails() {
        // Przygotowanie
        Path invalidPath = Path.of("/invalid/uploads").toAbsolutePath().normalize();

        // Wymuszenie wyjątku, gdy klasa próbuje utworzyć katalog w niepoprawnej lokalizacji
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            // Użycie refleksji, aby ustawić nieprawidłową lokalizację katalogu
            Field field = FileStorageService.class.getDeclaredField("fileStorageLocation");
            field.setAccessible(true); // Umożliwia dostęp do prywatnego pola
            field.set(fileStorageService, invalidPath); // Ustawia nieprawidłową ścieżkę

            // Teraz wywołujemy metodę, która próbuje utworzyć katalog
            new FileStorageService();
        });

        assertTrue(exception.getMessage().contains("Nie można utworzyć katalogu"));
    }

}
