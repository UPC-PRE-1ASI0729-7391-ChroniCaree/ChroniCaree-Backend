package com.chronicare.platform.messages.infrastructure.persistence;

import com.chronicare.platform.messages.domain.model.aggregates.Message;
import com.chronicare.platform.messages.domain.repository.MessageRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * JPA Repository for Message persistence
 */
@Repository
public interface JpaMessageRepository extends JpaRepository<Message, String>, MessageRepository {
    
    List<Message> findByConversationIdOrderByCreatedAtAsc(String conversationId);
    
    Page<Message> findByConversationIdOrderByCreatedAtDesc(String conversationId, Pageable pageable);
    
    List<Message> findByConversationIdAndSenderIdOrderByCreatedAtDesc(String conversationId, Long senderId);
    
    long countByConversationId(String conversationId);

    @Override
    default List<Message> findByConversationId(String conversationId) {
        return findByConversationIdOrderByCreatedAtAsc(conversationId);
    }

    @Override
    default List<Message> findByConversationIdPaginated(String conversationId, int page, int limit) {
        Pageable pageable = org.springframework.data.domain.PageRequest.of(page, limit);
        return findByConversationIdOrderByCreatedAtDesc(conversationId, pageable).getContent();
    }

    @Override
    default List<Message> findByConversationIdAndSenderId(String conversationId, Long senderId) {
        return findByConversationIdAndSenderIdOrderByCreatedAtDesc(conversationId, senderId);
    }
    
    @Override
    default void delete(String messageId) {
        deleteById(messageId);
    }
    
    @Override
    default void softDelete(String messageId) {
        // Implement soft delete if needed; for now this is a no-op
    }
}
