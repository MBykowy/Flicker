// PostController.java
package com.FlickerDomain.flicker.controller;

import com.FlickerDomain.flicker.model.Comment;
import com.FlickerDomain.flicker.model.Post;
import com.FlickerDomain.flicker.service.FileStorageService;
import com.FlickerDomain.flicker.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.FlickerDomain.flicker.service.BlockService;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Klasa PostController obsługuje żądania HTTP związane z postami, w tym ich tworzenie,
 * pobieranie, usuwanie, polubienia, komentarze oraz przesyłanie plików.
 */
@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final FileStorageService fileStorageService;
    private final BlockService blockService;

    /**
     * Tworzy obiekt PostController z podanym PostService i FileStorageService.
     *
     * @param postService        serwis obsługujący operacje na postach
     * @param fileStorageService serwis obsługujący przechowywanie plików
     */
    public PostController(PostService postService, FileStorageService fileStorageService, BlockService blockService) {
        this.postService = postService;
        this.fileStorageService = fileStorageService;
        this.blockService = blockService;
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
    private String getCurrentUserEmail() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }
    /**
     * Pobiera wszystkie posty.
     *
     * @return lista wszystkich postów w obiekcie ResponseEntity
     */
    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts() {
        List<Post> posts = postService.getAllPosts(getCurrentUserEmail());
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

    /**
     * Edytuje istniejący post.
     *
     * @param postId   ID posta do edycji
     * @param email    email użytkownika edytującego post
     * @param content  nowa treść posta
     * @param mediaUrl nowy URL multimediów do posta
     * @return zaktualizowany post w obiekcie ResponseEntity
     */
    @PutMapping("/{postId}")
    public ResponseEntity<Post> editPost(@PathVariable Long postId, @RequestParam String email, @RequestParam String content, @RequestParam String mediaUrl) {
        Post post = postService.editPost(postId, email, content, mediaUrl);
        return ResponseEntity.ok(post);
    }

    /**
     * Pobiera posty posortowane malejąco po liczbie polubień.
     *
     * @return lista postów posortowanych po liczbie polubień w obiekcie ResponseEntity
     */
    @GetMapping("/sorted/likes/desc")
    public ResponseEntity<List<Post>> getPostsSortedByLikesDesc() {
        List<Post> posts = postService.getPostsSortedByLikesDesc();
        return ResponseEntity.ok(posts);
    }

    /**
     * Pobiera posty posortowane rosnąco po liczbie polubień.
     *
     * @return lista postów posortowanych po liczbie polubień w obiekcie ResponseEntity
     */
    @GetMapping("/sorted/likes/asc")
    public ResponseEntity<List<Post>> getPostsSortedByLikesAsc() {
        List<Post> posts = postService.getPostsSortedByLikesAsc();
        return ResponseEntity.ok(posts);
    }

    /**
     * Pobiera posty posortowane malejąco po dacie.
     *
     * @return lista postów posortowanych po dacie w obiekcie ResponseEntity
     */
    @GetMapping("/sorted/date/desc")
    public ResponseEntity<List<Post>> getPostsSortedByDateDesc() {
        List<Post> posts = postService.getPostsSortedByDateDesc();
        return ResponseEntity.ok(posts);
    }

    /**
     * Pobiera posty posortowane rosnąco po dacie.
     *
     * @return lista postów posortowanych po dacie w obiekcie ResponseEntity
     */
    @GetMapping("/sorted/date/asc")
    public ResponseEntity<List<Post>> getPostsSortedByDateAsc() {
        List<Post> posts = postService.getPostsSortedByDateAsc();
        return ResponseEntity.ok(posts);
    }

    /**
     * Pobiera posty od osób, które użytkownik śledzi.
     *
     * @param email email użytkownika
     * @return lista postów od osób, które użytkownik śledzi
     */
    @GetMapping("/following")
    public ResponseEntity<List<Post>> getPostsFromFollowing(@RequestParam String email) {
        List<Post> posts = postService.getPostsFromFollowing(email);
        return ResponseEntity.ok(posts);
    }


    @PostMapping("/unblock")
    public ResponseEntity<Void> unblockUser(@RequestParam String blockerEmail, @RequestParam String blockedEmail) {
        blockService.unblockUser(blockerEmail, blockedEmail);
        return ResponseEntity.noContent().build();
    }
    @PostMapping("/block")
    public ResponseEntity<?> blockUser(@RequestBody BlockUserRequest request) {
        try {
            blockService.blockUser(request.getBlockerEmail(), request.getBlockedEmail());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/blocked")
    public ResponseEntity<List<String>> getBlockedUsers(@RequestParam String email) {
        List<String> blockedEmails = blockService.getBlockedUsers(email).stream()
                .map(block -> block.getBlocked().getEmail())
                .collect(Collectors.toList());
        return ResponseEntity.ok(blockedEmails);
    }
}




