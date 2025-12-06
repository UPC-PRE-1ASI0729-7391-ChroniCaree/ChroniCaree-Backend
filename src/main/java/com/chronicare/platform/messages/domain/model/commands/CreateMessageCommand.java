package com.chronicare.platform.messages.domain.model.commands;

import com.chronicare.platform.messages.domain.model.valueobjects.SenderRole;

/**
 * CreateMessageCommand
 *
 * @summary
 * Represents the intent to create a new message inside a conversation thread.
 * Responsibilities:
 * - Encapsulates all information required for message creation
 * - Defines sender/receiver roles and urgency state
 * Notes:
 * - Validation is delegated to the application layer or domain constructors
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
