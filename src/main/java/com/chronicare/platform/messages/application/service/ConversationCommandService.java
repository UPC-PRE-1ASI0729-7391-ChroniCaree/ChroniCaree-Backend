package com.chronicare.platform.messages.application.service;

import com.chronicare.platform.messages.domain.model.aggregates.Conversation;
import java.util.List;

/**
 * Application Service Interface for Conversation Commands
 */
public interface ConversationCommandService {
    Conversation createConversation(Long tenantId, String type, List<Long> participantUserIds);
    Conversation updateConversation(String conversationId, String title, String description);
    void deleteConversation(String conversationId);
}
