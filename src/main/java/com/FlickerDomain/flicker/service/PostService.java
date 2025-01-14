package com.FlickerDomain.flicker.service;

import com.FlickerDomain.flicker.model.Comment;
import com.FlickerDomain.flicker.model.Post;
import com.FlickerDomain.flicker.model.Follow;
import com.FlickerDomain.flicker.model.User;
import com.FlickerDomain.flicker.repository.CommentRepository;
import com.FlickerDomain.flicker.repository.PostRepository;
import com.FlickerDomain.flicker.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Klasa PostService dostarcza logikę biznesową dla operacji związanych z postami,
 * w tym tworzenie, usuwanie, polubienia oraz zarządzanie komentarzami.
 */
@Service
public class PostService {

    /**
     * Repozytorium do zarządzania postami.
     */
    private final PostRepository postRepository;

    /**
     * Repozytorium do zarządzania użytkownikami.
     */
    private final UserRepository userRepository;

    /**
     * Repozytorium do zarządzania komentarzami.
     */
    private final CommentRepository commentRepository;

    /**
     * Konstruktor klasy PostService.
     *
     * @param postRepository   repozytorium postów
     * @param userRepository   repozytorium użytkowników
     * @param commentRepository repozytorium komentarzy
     */
    public PostService(PostRepository postRepository, UserRepository userRepository, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }

    /**
     * Tworzy nowy post.
     *
     * @param email    email autora posta
     * @param content  treść posta
     * @param mediaUrl adres URL multimediów powiązanych z postem
     * @return utworzony post jako obiekt Post
     * @throws RuntimeException jeśli użytkownik o podanym emailu nie istnieje
     */
    public Post createPost(String email, String content, String mediaUrl) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        Post post = new Post();
        post.setUser(user);
        post.setContent(content);
        post.setMediaUrl(mediaUrl);
        return postRepository.save(post);
    }

    /**
     * Pobiera listę wszystkich postów.
     *
     * @return lista postów jako List<Post>
     */
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    /**
     * Usuwa post i powiązane z nim komentarze.
     *
     * @param postId ID posta do usunięcia
     * @param email  email użytkownika próbującego usunąć post
     * @throws RuntimeException jeśli post nie istnieje lub użytkownik nie jest autorem posta
     */
    @Transactional
    public void deletePost(Long postId, String email) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
        if (!post.getUser().getEmail().equals(email)) {
            throw new RuntimeException("Unauthorized");
        }
        commentRepository.deleteByPostId(postId); // Usuwa powiązane komentarze
        postRepository.delete(post);
    }

    /**
     * Przełącza stan polubienia posta przez użytkownika.
     *
     * @param postId ID posta
     * @param email  email użytkownika
     * @return zaktualizowany post jako obiekt Post
     * @throws RuntimeException jeśli post lub użytkownik nie istnieje
     */
    public Post toggleLikePost(Long postId, String email) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        if (post.getLikedBy().contains(user)) {
            post.getLikedBy().remove(user);
            post.setLikes(post.getLikes() - 1);
        } else {
            post.getLikedBy().add(user);
            post.setLikes(post.getLikes() + 1);
        }

        return postRepository.save(post);
    }

    /**
     * Dodaje komentarz do posta.
     *
     * @param postId  ID posta
     * @param email   email użytkownika dodającego komentarz
     * @param content treść komentarza
     * @return utworzony komentarz jako obiekt Comment
     * @throws RuntimeException jeśli post lub użytkownik nie istnieje
     */
    public Comment addComment(Long postId, String email, String content) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        Comment comment = new Comment();
        comment.setPost(post);
        comment.setUser(user);
        comment.setContent(content);
        return commentRepository.save(comment);
    }

    /**
     * Pobiera listę komentarzy powiązanych z postem.
     *
     * @param postId ID posta
     * @return lista komentarzy jako List<Comment>
     */
    public List<Comment> getComments(Long postId) {
        return commentRepository.findByPostId(postId);
    }

    /**
     * Edytuje post.
     *
     * @param postId       ID posta
     * @param email        email użytkownika próbującego edytować post
     * @param newContent   nowa treść posta
     * @param newMediaUrl  nowy adres URL multimediów
     * @return zaktualizowany post
     * @throws RuntimeException jeśli post nie istnieje lub użytkownik nie jest autorem posta
     */
    @Transactional
    public Post editPost(Long postId, String email, String newContent, String newMediaUrl) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
        if (!post.getUser().getEmail().equals(email)) {
            throw new RuntimeException("Unauthorized");
        }
        post.setContent(newContent);
        post.setMediaUrl(newMediaUrl);
        return postRepository.save(post);
    }

    /**
     * Pobiera listę postów posortowanych malejąco według liczby polubień.
     *
     * @return lista postów posortowanych według liczby polubień
     */
    public List<Post> getPostsSortedByLikesDesc() {
        return postRepository.findAllByOrderByLikesDesc();
    }

    /**
     * Pobiera listę postów posortowanych rosnąco według liczby polubień.
     *
     * @return lista postów posortowanych według liczby polubień
     */
    public List<Post> getPostsSortedByLikesAsc() {
        return postRepository.findAllByOrderByLikesAsc();
    }

    /**
     * Pobiera listę postów posortowanych malejąco według daty utworzenia.
     *
     * @return lista postów posortowanych według daty utworzenia
     */
    public List<Post> getPostsSortedByDateDesc() {
        return postRepository.findAllByOrderByCreatedAtDesc();
    }

    /**
     * Pobiera listę postów posortowanych rosnąco według daty utworzenia.
     *
     * @return lista postów posortowanych według daty utworzenia
     */
    public List<Post> getPostsSortedByDateAsc() {
        return postRepository.findAllByOrderByCreatedAtAsc();
    }

    /**
     * Pobiera posty od użytkowników, których dany użytkownik śledzi.
     *
     * @param email email użytkownika
     * @return lista postów od śledzonych użytkowników
     * @throws RuntimeException jeśli użytkownik nie istnieje
     */
    public List<Post> getPostsFromFollowing(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        Set<User> followedUsers = user.getFollowing().stream().map(Follow::getFollowed).collect(Collectors.toSet());
        return postRepository.findByUserIn(followedUsers);
    }
}
