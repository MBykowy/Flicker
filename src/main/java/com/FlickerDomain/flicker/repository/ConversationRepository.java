package com.FlickerDomain.flicker.repository;

import com.FlickerDomain.flicker.model.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    List<Conversation> findByParticipant1IdOrParticipant2Id(Long participant1Id, Long participant2Id);
    Conversation findByParticipant1IdAndParticipant2Id(Long participant1Id, Long participant2Id);
}