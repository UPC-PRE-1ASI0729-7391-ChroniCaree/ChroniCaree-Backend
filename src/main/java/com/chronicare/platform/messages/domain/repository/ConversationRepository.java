package com.chronicare.platform.messages.domain.repository;

import com.chronicare.platform.messages.domain.model.aggregates.Conversation;
import java.util.List;
import java.util.Optional;

/**
 * Domain Repository for Conversation aggregate
 */
public interface ConversationRepository {
    Conversation save(Conversation conversation);
    Optional<Conversation> findById(String conversationId);
    List<Conversation> findByParticipantUserId(Long userId, Long tenantId);
    List<Conversation> findByTenantId(Long tenantId);
    void delete(String conversationId);
    Optional<Conversation> findOneToOneConversation(Long tenantId, Long userId1, Long userId2);
}
