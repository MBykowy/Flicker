// FollowController.java
package com.FlickerDomain.flicker.controller;

import com.FlickerDomain.flicker.dto.FollowerRequest;
import com.FlickerDomain.flicker.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Kontroler obsługujący operacje związane z funkcjonalnością śledzenia użytkowników.
 * Obejmuje funkcje śledzenia użytkowników, przestawania ich śledzenia oraz sprawdzania statusu śledzenia.
 */
@RestController
@RequestMapping("/api/follow")
public class FollowController {

    @Autowired
    private FollowService followService;

    /**
     * Konstruktor kontrolera.
     *
     * @param followService serwis odpowiedzialny za operacje związane ze śledzeniem użytkowników
     */
    public FollowController(FollowService followService) {
        this.followService = followService;
    }

    /**
     * Śledzi użytkownika.
     * Po otrzymaniu żądania z e-mailem użytkownika, który chce śledzić (followerEmail),
     * oraz e-mailem użytkownika, który ma zostać śledzony (followedEmail),
     * użytkownik zostaje dodany do listy obserwowanych.
     *
     * @param followerRequest obiekt zawierający dane o e-mailu użytkownika śledzącego oraz śledzonego
     * @return odpowiedź z komunikatem potwierdzającym, że użytkownik został pomyślnie śledzony
     */
    @PostMapping("/follow")
    public ResponseEntity<Map<String, String>> followUser(@RequestBody FollowerRequest followerRequest) {
        followService.followUser(followerRequest.getFollowerEmail(), followerRequest.getFollowedEmail());
        Map<String, String> response = new HashMap<>();
        response.put("message", "Followed successfully");
        return ResponseEntity.ok(response);
    }

    /**
     * Przestaje śledzić użytkownika.
     * Po otrzymaniu żądania z e-mailem użytkownika, który chce przestać śledzić (followerEmail),
     * oraz e-mailem użytkownika, który ma zostać usunięty z listy obserwowanych (followedEmail),
     * użytkownik zostaje usunięty z listy obserwowanych.
     *
     * @param followerRequest obiekt zawierający dane o e-mailu użytkownika, który przestaje śledzić
     *                        oraz użytkownika, którego przestaje śledzić
     * @return odpowiedź z komunikatem potwierdzającym, że użytkownik został pomyślnie przestał śledzić
     */
    @PostMapping("/unfollow")
    public ResponseEntity<Map<String, String>> unfollowUser(@RequestBody FollowerRequest followerRequest) {
        followService.unfollowUser(followerRequest.getFollowerEmail(), followerRequest.getFollowedEmail());
        Map<String, String> response = new HashMap<>();
        response.put("message", "Unfollowed successfully");
        return ResponseEntity.ok(response);
    }

    /**
     * Sprawdza, czy użytkownik śledzi innego użytkownika.
     * Po otrzymaniu żądania z e-mailem użytkownika, który chce sprawdzić status śledzenia
     * oraz e-mailem użytkownika, którego status śledzenia ma zostać sprawdzony,
     * metoda zwraca wartość `true` lub `false` w zależności od tego, czy użytkownik śledzi drugiego użytkownika.
     *
     * @param followerEmail e-mail użytkownika sprawdzającego status śledzenia
     * @param followedEmail e-mail użytkownika, którego status śledzenia jest sprawdzany
     * @return odpowiedź zawierająca wynik sprawdzenia statusu śledzenia użytkownika
     */
    @GetMapping("/check")
    public ResponseEntity<Boolean> checkFollowStatus(@RequestParam("followerEmail") String followerEmail,
                                                     @RequestParam("followedEmail") String followedEmail) {
        boolean isFollowing = followService.checkFollowStatus(followerEmail, followedEmail);
        return ResponseEntity.ok(isFollowing);
    }
}
