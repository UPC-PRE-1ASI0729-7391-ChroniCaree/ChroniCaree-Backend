package com.chronicare.platform.messages.domain.repository;

import com.chronicare.platform.messages.domain.model.aggregates.Message;
import java.util.List;
import java.util.Optional;

/**
 * Domain Repository for Message aggregate
 */
public interface MessageRepository {
    Message save(Message message);
    Optional<Message> findById(String messageId);
    List<Message> findByConversationId(String conversationId);
    List<Message> findByConversationIdPaginated(String conversationId, int page, int limit);
    List<Message> findByConversationIdAndSenderId(String conversationId, Long senderId);
    void delete(String messageId);
    void softDelete(String messageId);
    long countByConversationId(String conversationId);
}
