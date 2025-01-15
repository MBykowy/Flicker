package com.example.flicker;

import com.FlickerDomain.flicker.controller.PostController;
import com.FlickerDomain.flicker.model.Post;
import com.FlickerDomain.flicker.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PostFillterTest {

    private PostService postService;
    private PostController postController;

    @BeforeEach
    void setUp() {
        postService = mock(PostService.class);
        postController = new PostController(postService, null);
    }

    @Test
    void testGetPostsSortedByLikesDesc() {
        // Arrange
        List<Post> posts = new ArrayList<>();
        Post post1 = new Post();
        post1.setId(1L);
        post1.setLikes(10);
        posts.add(post1);

        Post post2 = new Post();
        post2.setId(2L);
        post2.setLikes(5);
        posts.add(post2);

        when(postService.getPostsSortedByLikesDesc()).thenReturn(posts);

        // Act
        ResponseEntity<List<Post>> response = postController.getPostsSortedByLikesDesc();

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
        assertEquals(10, response.getBody().get(0).getLikes());
        verify(postService, times(1)).getPostsSortedByLikesDesc();
    }


    @Test
    void testGetPostsSortedByDateAsc() throws ParseException {
        // Arrange
        List<Post> posts = new ArrayList<>();
        Post post1 = new Post();
        post1.setId(1L);
        post1.setCreatedAt(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse("2023-01-01T10:00:00"));
        posts.add(post1);

        Post post2 = new Post();
        post2.setId(2L);
        post2.setCreatedAt(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse("2023-01-02T10:00:00"));
        posts.add(post2);

        when(postService.getPostsSortedByDateAsc()).thenReturn(posts);

        // Act
        ResponseEntity<List<Post>> response = postController.getPostsSortedByDateAsc();

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
        assertEquals(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse("2023-01-01T10:00:00"), response.getBody().get(0).getCreatedAt());
        verify(postService, times(1)).getPostsSortedByDateAsc();
    }
}
