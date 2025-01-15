package com.example.flicker;

import com.FlickerDomain.flicker.controller.PostController;
import com.FlickerDomain.flicker.controller.PostRequest;
import com.FlickerDomain.flicker.model.Post;
import com.FlickerDomain.flicker.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class PostControllerTest {

    private PostService postService;
    private PostController postController;

    @BeforeEach
    void setUp() {
        postService = mock(PostService.class);
        postController = new PostController(postService, null); // FileStorageService not needed for these tests
    }

    @Test
    void testCreatePost() {
        PostRequest request = new PostRequest();
        request.setEmail("user@example.com");
        request.setContent("Test content");
        request.setMediaUrl("http://example.com/media");

        Post mockPost = new Post();
        mockPost.setId(1L);
        mockPost.setContent(request.getContent());
        mockPost.setMediaUrl(request.getMediaUrl());

        when(postService.createPost(anyString(), anyString(), anyString())).thenReturn(mockPost);

        ResponseEntity<Post> response = postController.createPost(request);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Test content", response.getBody().getContent());
        verify(postService, times(1)).createPost("user@example.com", "Test content", "http://example.com/media");
    }

    @Test
    void testEditPost() {
        Long postId = 1L;
        String email = "user@example.com";
        String newContent = "Updated content";
        String newMediaUrl = "http://example.com/updated-media";

        Post mockPost = new Post();
        mockPost.setId(postId);
        mockPost.setContent(newContent);
        mockPost.setMediaUrl(newMediaUrl);

        when(postService.editPost(eq(postId), eq(email), eq(newContent), eq(newMediaUrl))).thenReturn(mockPost);

        ResponseEntity<Post> response = postController.editPost(postId, email, newContent, newMediaUrl);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(newContent, response.getBody().getContent());
        assertEquals(newMediaUrl, response.getBody().getMediaUrl());
        verify(postService, times(1)).editPost(postId, email, newContent, newMediaUrl);
    }

    @Test
    void testDeletePost() {
        Long postId = 1L;
        String email = "user@example.com";

        doNothing().when(postService).deletePost(eq(postId), eq(email));

        ResponseEntity<Void> response = postController.deletePost(postId, email);

        assertEquals(204, response.getStatusCodeValue());
        verify(postService, times(1)).deletePost(postId, email);
    }
}
