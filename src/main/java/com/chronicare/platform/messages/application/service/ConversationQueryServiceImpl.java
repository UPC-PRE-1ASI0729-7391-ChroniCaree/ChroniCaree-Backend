package com.chronicare.platform.messages.application.service;

import com.chronicare.platform.messages.domain.model.aggregates.Conversation;
import com.chronicare.platform.messages.domain.repository.ConversationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * Application Service for Conversation Queries (DDD Implementation)
 */
@Service
public class ConversationQueryServiceImpl implements ConversationQueryService {

    private final ConversationRepository conversationRepository;

    public ConversationQueryServiceImpl(ConversationRepository conversationRepository) {
        this.conversationRepository = conversationRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Conversation getConversationById(String conversationId) {
        return conversationRepository.findById(conversationId)
                .orElseThrow(() -> new IllegalArgumentException("Conversation not found with id: " + conversationId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Conversation> listConversations(int page, int limit) {
        // For JPA, we would need a pageable repository method
        // This is a basic implementation
        return List.of();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Conversation> getConversationsByTenantId(Long tenantId) {
        return conversationRepository.findByTenantId(tenantId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Conversation> getConversationsByUserId(Long userId, Long tenantId) {
        // This would require a custom query in the repository
        return conversationRepository.findByTenantId(tenantId);
    }
}
