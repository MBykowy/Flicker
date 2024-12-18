// PostService.java
package com.FlickerDomain.flicker.service;

import com.FlickerDomain.flicker.model.Post;
import com.FlickerDomain.flicker.model.User;
import com.FlickerDomain.flicker.repository.PostRepository;
import com.FlickerDomain.flicker.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public Post createPost(String email, String content, String mediaUrl) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        Post post = new Post();
        post.setUser(user);
        post.setContent(content);
        post.setMediaUrl(mediaUrl); // Dodaj to
        return postRepository.save(post);
    }

    public List<Post> getPostsByUser(Long userId) {
        return postRepository.findByUserId(userId);
    }
    public void deletePost(Long postId, String email) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
        if (!post.getUser().getEmail().equals(email)) {
            throw new RuntimeException("Unauthorized");
        }
        postRepository.delete(post);
    }
}