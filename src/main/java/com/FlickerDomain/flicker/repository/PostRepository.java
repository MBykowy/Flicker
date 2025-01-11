// PostRepository.java
package com.FlickerDomain.flicker.repository;

import com.FlickerDomain.flicker.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUserId(Long userId);
    List<Post> findAllByOrderByLikesDesc();
    List<Post> findAllByOrderByLikesAsc();
    List<Post> findAllByOrderByCreatedAtDesc();
    List<Post> findAllByOrderByCreatedAtAsc();
}