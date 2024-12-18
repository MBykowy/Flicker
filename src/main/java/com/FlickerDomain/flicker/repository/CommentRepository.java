package com.FlickerDomain.flicker.repository;

import com.FlickerDomain.flicker.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    void deleteByPostId(Long postId); // Method to delete comments by post ID
    List<Comment> findByPostId(Long postId); // Method to find comments by post ID
}