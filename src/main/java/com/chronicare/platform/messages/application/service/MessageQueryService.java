package com.chronicare.platform.messages.application.service;

import com.chronicare.platform.messages.domain.model.aggregates.Message;
import java.util.List;

/**
 * Application Service Interface for Message Queries
 */
public interface MessageQueryService {
    Message getMessageById(String messageId);
    List<Message> listMessages(String conversationId, int page, int limit);
    List<Message> getMessagesByConversation(String conversationId);
    List<Message> getMessagesBySender(String conversationId, Long senderId);
}
