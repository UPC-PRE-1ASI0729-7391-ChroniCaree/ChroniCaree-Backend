package com.chronicare.platform.messages.interfaces.rest.resources;

import com.chronicare.platform.messages.domain.model.valueobjects.SenderRole;
import com.fasterxml.jackson.annotation.JsonAlias;

/**
 * Resource for creating a new Message
 */
public record CreateMessageResource(
        Long threadId,
        SenderRole senderRole,
        Long senderId,
        Long receiverId,
        @JsonAlias({"body","content"}) String body,
        String subject,
        boolean isUrgent
) {
}
