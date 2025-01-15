// BlockService.java
package com.FlickerDomain.flicker.service;

import com.FlickerDomain.flicker.model.Block;
import com.FlickerDomain.flicker.model.User;
import com.FlickerDomain.flicker.repository.BlockRepository;
import com.FlickerDomain.flicker.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BlockService {

    private final BlockRepository blockRepository;
    private final UserRepository userRepository;

    public BlockService(BlockRepository blockRepository, UserRepository userRepository) {
        this.blockRepository = blockRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void blockUser(String blockerEmail, String blockedEmail) {
        User blocker = userRepository.findByEmail(blockerEmail)
                .orElseThrow(() -> new RuntimeException("Blocker not found"));
        User blocked = userRepository.findByEmail(blockedEmail)
                .orElseThrow(() -> new RuntimeException("Blocked user not found"));

        if (!blockRepository.existsByBlockerAndBlocked(blocker, blocked)) {
            Block block = new Block();
            block.setBlocker(blocker);
            block.setBlocked(blocked);
            blockRepository.save(block);
        }
    }

    @Transactional
    public void unblockUser(String blockerEmail, String blockedEmail) {
        User blocker = userRepository.findByEmail(blockerEmail)
                .orElseThrow(() -> new RuntimeException("Blocker not found"));
        User blocked = userRepository.findByEmail(blockedEmail)
                .orElseThrow(() -> new RuntimeException("Blocked user not found"));

        blockRepository.deleteByBlockerAndBlocked(blocker, blocked);
    }

    public List<Block> getBlockedUsers(String blockerEmail) {
        User blocker = userRepository.findByEmail(blockerEmail)
                .orElseThrow(() -> new RuntimeException("Blocker not found"));
        return blockRepository.findByBlocker(blocker);
    }

    public List<Block> getBlockedByUsers(String blockedEmail) {
        User blocked = userRepository.findByEmail(blockedEmail)
                .orElseThrow(() -> new RuntimeException("Blocked user not found"));
        return blockRepository.findByBlocked(blocked);
    }
}