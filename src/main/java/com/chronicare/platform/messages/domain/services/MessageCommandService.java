package com.chronicare.platform.messages.domain.services;

import com.chronicare.platform.messages.domain.model.aggregates.Message;
import com.chronicare.platform.messages.domain.model.commands.CreateMessageCommand;
import com.chronicare.platform.messages.domain.model.commands.DeleteMessageCommand;
import com.chronicare.platform.messages.domain.model.commands.MarkMessageAsReadCommand;
import com.chronicare.platform.messages.domain.model.commands.UpdateMessageCommand;

import java.util.Optional;

/**
 * Message Command Service Interface
 * Handles commands for message operations
 */
public interface MessageCommandService {
    Optional<Message> handle(CreateMessageCommand command);
    Optional<Message> handle(UpdateMessageCommand command);
    Optional<Message> handle(MarkMessageAsReadCommand command);
    void handle(DeleteMessageCommand command);
}
