package com.FlickerDomain.flicker.repository;

import com.FlickerDomain.flicker.model.Follow;
import com.FlickerDomain.flicker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
    boolean existsByFollowerAndFollowed(User follower, User followed);
    void deleteByFollowerAndFollowed(User follower, User followed);
    List<Follow> findByFollowed(User followed);
}