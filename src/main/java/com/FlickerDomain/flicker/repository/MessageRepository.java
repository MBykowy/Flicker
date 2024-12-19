package com.FlickerDomain.flicker.repository;

import com.FlickerDomain.flicker.model.Conversation;
import com.FlickerDomain.flicker.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findBySenderIdAndReceiverId(Long senderId, Long receiverId);
    List<Message> findByReceiverIdAndReadStatusFalse(Long receiverId);
    List<Message> findTop10ByOrderByTimestampDesc();
    List<Message> findByConversationId(Long conversationId);
    Message findTopByConversationOrderByTimestampDesc(Conversation conversation);
}