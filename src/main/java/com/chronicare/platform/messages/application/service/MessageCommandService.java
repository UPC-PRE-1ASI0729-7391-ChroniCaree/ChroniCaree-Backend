package com.chronicare.platform.messages.application.service;

import com.chronicare.platform.messages.domain.model.aggregates.Message;

/**
 * Application Service Interface for Message Commands
 */
public interface MessageCommandService {
    Message createMessage(String conversationId, Long senderId, Long tenantId, String body, String contentType);
    Message editMessage(String messageId, String newBody);
    Message updateMessageStatus(String messageId, String status);
    void deleteMessage(String messageId);
}
