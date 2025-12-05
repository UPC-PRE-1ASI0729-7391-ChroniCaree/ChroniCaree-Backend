package com.chronicare.platform.messages.infrastructure.persistence.jpa.repositories;

import com.chronicare.platform.messages.domain.model.aggregates.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * JPA Repository for Message aggregate
 */
@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByThreadId(Long threadId);
    List<Message> findBySenderId(Long senderId);
    List<Message> findByReceiverId(Long receiverId);
}
