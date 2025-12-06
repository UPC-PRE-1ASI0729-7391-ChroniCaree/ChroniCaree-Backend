package com.chronicare.platform.messages.application.service;

import com.chronicare.platform.messages.domain.model.aggregates.Conversation;
import java.util.List;

/**
 * Application Service Interface for Conversation Queries
 */
public interface ConversationQueryService {
    Conversation getConversationById(String conversationId);
    List<Conversation> listConversations(int page, int limit);
    List<Conversation> getConversationsByTenantId(Long tenantId);
    List<Conversation> getConversationsByUserId(Long userId, Long tenantId);
}
