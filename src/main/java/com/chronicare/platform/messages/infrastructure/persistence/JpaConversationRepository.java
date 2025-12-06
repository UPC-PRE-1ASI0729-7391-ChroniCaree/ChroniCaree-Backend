package com.chronicare.platform.messages.infrastructure.persistence;

import com.chronicare.platform.messages.domain.model.aggregates.Conversation;
import com.chronicare.platform.messages.domain.repository.ConversationRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * JPA Repository for Conversation persistence
 */
@Repository
public interface JpaConversationRepository extends JpaRepository<Conversation, String>, ConversationRepository {
    
    @Query("SELECT c FROM Conversation c JOIN c.participants p WHERE p.userId = :userId AND c.tenantId = :tenantId ORDER BY c.lastMessageAt DESC NULLS LAST")
    List<Conversation> findByParticipantUserId(@Param("userId") Long userId, @Param("tenantId") Long tenantId);
    
    List<Conversation> findByTenantIdOrderByLastMessageAtDesc(Long tenantId);

    @Override
    default List<Conversation> findByTenantId(Long tenantId) {
        return findByTenantIdOrderByLastMessageAtDesc(tenantId);
    }

    @Query("SELECT c FROM Conversation c JOIN c.participants p1 JOIN c.participants p2 " +
           "WHERE c.tenantId = :tenantId AND c.type = 'ONE_TO_ONE' " +
           "AND p1.userId = :userId1 AND p2.userId = :userId2")
    Optional<Conversation> findOneToOne(@Param("tenantId") Long tenantId, @Param("userId1") Long userId1, @Param("userId2") Long userId2);

    @Override
    default Optional<Conversation> findOneToOneConversation(Long tenantId, Long userId1, Long userId2) {
        return findOneToOne(tenantId, userId1, userId2);
    }
    
    @Override
    default void delete(String conversationId) {
        deleteById(conversationId);
    }
}
