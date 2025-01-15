package com.FlickerDomain.flicker.service;

import com.FlickerDomain.flicker.model.Follow;
import com.FlickerDomain.flicker.model.Post;
import com.FlickerDomain.flicker.model.User;
import com.FlickerDomain.flicker.repository.FollowRepository;
import com.FlickerDomain.flicker.repository.PostRepository;
import com.FlickerDomain.flicker.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Klasa {@link FollowService} obsługuje logikę biznesową związaną z obserwowaniem użytkowników.
 * Umożliwia użytkownikom śledzenie innych osób, a także anulowanie śledzenia oraz sprawdzanie statusu obserwowania.
 *
 * {@link FollowService} zapewnia funkcje dodawania i usuwania obserwujących, a także sprawdzania, czy użytkownik
 * obserwuje innego użytkownika. Zawiera także transakcje, które zapewniają integralność danych podczas modyfikacji
 * relacji między użytkownikami.
 */
@Service
public class FollowService {

    /**
     * Repozytorium do zarządzania danymi dotyczącymi relacji "Follow".
     */
    private final FollowRepository followRepository;

    /**
     * Repozytorium do zarządzania danymi użytkowników.
     */
    private final UserRepository userRepository;

    private final PostRepository postRepository;


    /**
     * Konstruktor klasy {@link FollowService}.
     * Inicjalizuje repozytoria niezbędne do zarządzania danymi użytkowników oraz relacjami "Follow".
     *
     * @param followRepository repozytorium dla relacji "Follow".
     * @param userRepository repozytorium dla użytkowników.
     */
    public FollowService(FollowRepository followRepository, UserRepository userRepository, PostRepository postRepository) {
        this.followRepository = followRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    /**
     * Umożliwia użytkownikowi obserwowanie innego użytkownika.
     * Jeśli relacja już istnieje, nic się nie zmienia.
     * Operacja jest wykonywana w ramach transakcji, aby zapewnić integralność danych.
     *
     * @param followerEmail adres e-mail użytkownika, który chce zacząć obserwować.
     * @param followedEmail adres e-mail użytkownika, który ma zostać obserwowany.
     * @throws RuntimeException jeśli któryś z użytkowników nie zostanie znaleziony.
     */
    @Transactional
    public void followUser(String followerEmail, String followedEmail) {
        User follower = userRepository.findByEmail(followerEmail)
                .orElseThrow(() -> new RuntimeException("Follower not found"));
        User followed = userRepository.findByEmail(followedEmail)
                .orElseThrow(() -> new RuntimeException("Followed user not found"));

        // Sprawdzenie, czy użytkownik już nie obserwuje danego użytkownika
        if (!followRepository.existsByFollowerAndFollowed(follower, followed)) {
            Follow follow = new Follow();
            follow.setFollower(follower);
            follow.setFollowed(followed);
            followRepository.save(follow); // Zapisanie nowej relacji "Follow"
        }
    }

    /**
     * Umożliwia użytkownikowi zaprzestanie obserwowania innego użytkownika.
     * Jeśli relacja nie istnieje, nic się nie zmienia.
     * Operacja jest wykonywana w ramach transakcji.
     *
     * @param followerEmail adres e-mail użytkownika, który chce zaprzestać obserwowania.
     * @param followedEmail adres e-mail użytkownika, którego obserwowanie ma zostać zakończone.
     * @throws RuntimeException jeśli któryś z użytkowników nie zostanie znaleziony.
     */
    @Transactional
    public void unfollowUser(String followerEmail, String followedEmail) {
        User follower = userRepository.findByEmail(followerEmail)
                .orElseThrow(() -> new RuntimeException("Follower not found"));
        User followed = userRepository.findByEmail(followedEmail)
                .orElseThrow(() -> new RuntimeException("Followed user not found"));

        // Usunięcie relacji "Follow" jeśli istnieje
        followRepository.deleteByFollowerAndFollowed(follower, followed);
    }

    /**
     * Sprawdza, czy dany użytkownik obserwuje innego użytkownika.
     *
     * @param followerEmail adres e-mail użytkownika, który może obserwować.
     * @param followedEmail adres e-mail użytkownika, który może być obserwowany.
     * @return {@code true} jeśli użytkownik obserwuje drugiego użytkownika, {@code false} w przeciwnym razie.
     * @throws RuntimeException jeśli któryś z użytkowników nie zostanie znaleziony.
     */
    public boolean checkFollowStatus(String followerEmail, String followedEmail) {
        User follower = userRepository.findByEmail(followerEmail)
                .orElseThrow(() -> new RuntimeException("Follower not found"));
        User followed = userRepository.findByEmail(followedEmail)
                .orElseThrow(() -> new RuntimeException("Followed user not found"));

        return followRepository.existsByFollowerAndFollowed(follower, followed); // Sprawdzenie statusu relacji
    }

    // PostService.java
    public List<Post> getPostsFromFollowing(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        Set<User> followedUsers = user.getFollowing().stream().map(Follow::getFollowed).collect(Collectors.toSet());
        List<Post> posts = postRepository.findByUserIn(followedUsers);

// Ensure followedBy is populated
        posts.forEach(post -> {
            User postUser = post.getUser();
            postUser.setFollowedBy(followRepository.findByFollowed(postUser).stream()
                    .map(follow -> follow.getFollower().getEmail())
                    .collect(Collectors.toList()));
        });

        return posts;
    }
}
