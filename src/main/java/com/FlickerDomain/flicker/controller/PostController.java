package com.FlickerDomain.flicker.controller;

import com.FlickerDomain.flicker.model.Comment;
import com.FlickerDomain.flicker.model.Post;
import com.FlickerDomain.flicker.service.FileStorageService;
import com.FlickerDomain.flicker.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Klasa PostController obsługuje żądania HTTP związane z postami, w tym ich tworzenie,
 * pobieranie, usuwanie, polubienia, komentarze oraz przesyłanie plików.
 */
@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final FileStorageService fileStorageService;

    /**
     * Tworzy obiekt PostController z podanym PostService i FileStorageService.
     *
     * @param postService        serwis obsługujący operacje na postach
     * @param fileStorageService serwis obsługujący przechowywanie plików
     */
    public PostController(PostService postService, FileStorageService fileStorageService) {
        this.postService = postService;
        this.fileStorageService = fileStorageService;
    }

    /**
     * Tworzy nowy post.
     *
     * @param postRequest żądanie zawierające dane posta (email, treść i URL multimediów)
     * @return utworzony post w obiekcie ResponseEntity
     */
    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody PostRequest postRequest) {
        Post post = postService.createPost(postRequest.getEmail(), postRequest.getContent(), postRequest.getMediaUrl());
        return ResponseEntity.ok(post);
    }
    /**
     * Pobiera wszystkie posty.
     *
     * @return lista wszystkich postów w obiekcie ResponseEntity
     */
    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts() {
        List<Post> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    /**
     * Usuwa post na podstawie jego ID.
     *
     * @param postId ID posta do usunięcia
     * @param email  email użytkownika usuwającego post
     * @return pusty ResponseEntity z kodem 204 (No Content)
     */
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId, @RequestParam String email) {
        postService.deletePost(postId, email);
        return ResponseEntity.noContent().build();
    }

    /**
     * Dodaje lub usuwa polubienie dla posta.
     *
     * @param postId ID posta do polubienia/odpolubienia
     * @param email  email użytkownika, który polubił/odpolubił post
     * @return zaktualizowany post w obiekcie ResponseEntity
     */
    @PostMapping("/{postId}/like")
    public ResponseEntity<Post> toggleLikePost(@PathVariable Long postId, @RequestParam String email) {
        Post post = postService.toggleLikePost(postId, email);
        return ResponseEntity.ok(post);
    }

    /**
     * Dodaje komentarz do posta.
     *
     * @param postId  ID posta, do którego dodawany jest komentarz
     * @param email   email użytkownika dodającego komentarz
     * @param content treść komentarza
     * @return dodany komentarz w obiekcie ResponseEntity
     */
    @PostMapping("/{postId}/comment")
    public ResponseEntity<Comment> addComment(@PathVariable Long postId, @RequestParam String email, @RequestBody String content) {
        Comment comment = postService.addComment(postId, email, content);
        return ResponseEntity.ok(comment);
    }
    /**
     * Pobiera wszystkie komentarze do posta.
     *
     * @param postId ID posta, dla którego pobierane są komentarze
     * @return lista komentarzy w obiekcie ResponseEntity
     */
    @GetMapping("/{postId}/comments")
    public ResponseEntity<List<Comment>> getComments(@PathVariable Long postId) {
        List<Comment> comments = postService.getComments(postId);
        return ResponseEntity.ok(comments);
    }

    /**
     * Przesyła plik i zapisuje go na serwerze.
     *
     * @param file plik do przesłania
     * @return URL zapisanej lokalizacji pliku w obiekcie ResponseEntity
     */
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        String fileUrl = fileStorageService.storeFile(file);
        return ResponseEntity.ok(fileUrl);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<Post> editPost(@PathVariable Long postId, @RequestParam String email, @RequestParam String content, @RequestParam String mediaUrl) {
        Post post = postService.editPost(postId, email, content, mediaUrl);
        return ResponseEntity.ok(post);
    }
    @GetMapping("/sorted/likes/desc")
    public ResponseEntity<List<Post>> getPostsSortedByLikesDesc() {
        List<Post> posts = postService.getPostsSortedByLikesDesc();
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/sorted/likes/asc")
    public ResponseEntity<List<Post>> getPostsSortedByLikesAsc() {
        List<Post> posts = postService.getPostsSortedByLikesAsc();
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/sorted/date/desc")
    public ResponseEntity<List<Post>> getPostsSortedByDateDesc() {
        List<Post> posts = postService.getPostsSortedByDateDesc();
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/sorted/date/asc")
    public ResponseEntity<List<Post>> getPostsSortedByDateAsc() {
        List<Post> posts = postService.getPostsSortedByDateAsc();
        return ResponseEntity.ok(posts);
    }
    @GetMapping("/following")
    public ResponseEntity<List<Post>> getPostsFromFollowing(@RequestParam String email) {
        List<Post> posts = postService.getPostsFromFollowing(email);
        return ResponseEntity.ok(posts);
    }
}
