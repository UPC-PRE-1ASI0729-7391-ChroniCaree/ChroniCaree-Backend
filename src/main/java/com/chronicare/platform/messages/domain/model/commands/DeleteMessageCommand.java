package com.chronicare.platform.messages.domain.model.commands;

/**
 * DeleteMessageCommand
 *
 * @summary
 * Represents the intent to delete an existing message.
 * Responsibilities:
 * - Encapsulates the identifier of the message targeted for deletion
 * Notes:
 * - Validation should be handled in the application layer
 */

public record DeleteMessageCommand(Long id) {
}
