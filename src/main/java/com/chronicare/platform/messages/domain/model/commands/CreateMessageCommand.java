package com.chronicare.platform.messages.domain.model.commands;

import com.chronicare.platform.messages.domain.model.valueobjects.SenderRole;

/**
 * Command to create a new message
 */
public record CreateMessageCommand(
        Long threadId,
        SenderRole senderRole,
        Long senderId,
        Long receiverId,
        String body,
        String subject,
        boolean isUrgent
) {
}
