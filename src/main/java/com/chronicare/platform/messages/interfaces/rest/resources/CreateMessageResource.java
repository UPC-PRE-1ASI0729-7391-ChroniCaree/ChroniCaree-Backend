package com.chronicare.platform.messages.interfaces.rest.resources;

import com.chronicare.platform.messages.domain.model.valueobjects.SenderRole;

/**
 * Resource for creating a new Message
 */
public record CreateMessageResource(
        Long threadId,
        SenderRole senderRole,
        Long senderId,
        Long receiverId,
        String body,
        String subject,
        boolean isUrgent
) {
}
