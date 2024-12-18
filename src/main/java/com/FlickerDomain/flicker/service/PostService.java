// PostService.java
package com.FlickerDomain.flicker.service;

import com.FlickerDomain.flicker.model.Comment;
import com.FlickerDomain.flicker.model.Post;
import com.FlickerDomain.flicker.model.User;
import com.FlickerDomain.flicker.repository.CommentRepository;
import com.FlickerDomain.flicker.repository.PostRepository;
import com.FlickerDomain.flicker.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }

    public Post createPost(String email, String content, String mediaUrl) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        Post post = new Post();
        post.setUser(user);
        post.setContent(content);
        post.setMediaUrl(mediaUrl);
        return postRepository.save(post);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @Transactional
    public void deletePost(Long postId, String email) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
        if (!post.getUser().getEmail().equals(email)) {
            throw new RuntimeException("Unauthorized");
        }
        commentRepository.deleteByPostId(postId); // Delete associated comments
        postRepository.delete(post);
    }

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

    public Comment addComment(Long postId, String email, String content) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        Comment comment = new Comment();
        comment.setPost(post);
        comment.setUser(user);
        comment.setContent(content);
        return commentRepository.save(comment);
    }

    public List<Comment> getComments(Long postId) {
        return commentRepository.findByPostId(postId);
    }
}