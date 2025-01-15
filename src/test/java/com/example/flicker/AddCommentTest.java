package com.example.flicker;

import com.FlickerDomain.flicker.model.Comment;
import com.FlickerDomain.flicker.model.Post;
import com.FlickerDomain.flicker.model.User;
import com.FlickerDomain.flicker.repository.CommentRepository;
import com.FlickerDomain.flicker.repository.FollowRepository;
import com.FlickerDomain.flicker.repository.PostRepository;
import com.FlickerDomain.flicker.repository.UserRepository;
import com.FlickerDomain.flicker.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AddCommentTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CommentRepository commentRepository;

    private PostService postService;
    @Mock
    private FollowRepository followRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        postService = new PostService(postRepository, userRepository, commentRepository, followRepository);
    }

    @Test
    void testAddComment_Success() {
        // Arrange
        Long postId = 1L;
        String email = "test@example.com";
        String content = "Test comment";

        Post post = new Post();
        post.setId(postId);

        User user = new User();
        user.setEmail(email);

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        Comment comment = new Comment();
        comment.setPost(post);
        comment.setUser(user);
        comment.setContent(content);

        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        // Act
        Comment result = postService.addComment(postId, email, content);

        // Assert
        assertNotNull(result);
        assertEquals(content, result.getContent());
        assertEquals(post, result.getPost());
        assertEquals(user, result.getUser());

        verify(postRepository, times(1)).findById(postId);
        verify(userRepository, times(1)).findByEmail(email);
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void testAddComment_PostNotFound() {
        // Arrange
        Long postId = 1L;
        String email = "test@example.com";
        String content = "Test comment";

        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            postService.addComment(postId, email, content);
        });
        assertEquals("Post not found", exception.getMessage());

        verify(postRepository, times(1)).findById(postId);
        verify(userRepository, never()).findByEmail(anyString());
        verify(commentRepository, never()).save(any(Comment.class));
    }

    @Test
    void testAddComment_UserNotFound() {
        // Arrange
        Long postId = 1L;
        String email = "test@example.com";
        String content = "Test comment";

        Post post = new Post();
        post.setId(postId);

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            postService.addComment(postId, email, content);
        });
        assertEquals("User not found", exception.getMessage());

        verify(postRepository, times(1)).findById(postId);
        verify(userRepository, times(1)).findByEmail(email);
        verify(commentRepository, never()).save(any(Comment.class));
    }
}