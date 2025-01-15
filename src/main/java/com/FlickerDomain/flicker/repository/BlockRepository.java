// BlockRepository.java
package com.FlickerDomain.flicker.repository;

import com.FlickerDomain.flicker.model.Block;
import com.FlickerDomain.flicker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlockRepository extends JpaRepository<Block, Long> {
    List<Block> findByBlocker(User blocker);
    List<Block> findByBlocked(User blocked);
    boolean existsByBlockerAndBlocked(User blocker, User blocked);
    void deleteByBlockerAndBlocked(User blocker, User blocked);
}