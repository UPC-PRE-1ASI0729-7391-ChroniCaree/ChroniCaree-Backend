package com.chronicare.platform.messages.domain.model.commands;

/**
 * Command to update an existing message
 */
public record UpdateMessageCommand(
        Long id,
        String body,
        String subject,
        boolean isUrgent
) {
}
