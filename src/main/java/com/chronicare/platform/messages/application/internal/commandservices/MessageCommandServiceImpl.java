package com.chronicare.platform.messages.application.internal.commandservices;

import com.chronicare.platform.messages.domain.model.aggregates.Message;
import com.chronicare.platform.messages.domain.model.commands.CreateMessageCommand;
import com.chronicare.platform.messages.domain.model.commands.DeleteMessageCommand;
import com.chronicare.platform.messages.domain.model.commands.MarkMessageAsReadCommand;
import com.chronicare.platform.messages.domain.model.commands.UpdateMessageCommand;
import com.chronicare.platform.messages.domain.services.MessageCommandService;
import com.chronicare.platform.messages.infrastructure.persistence.jpa.repositories.MessageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Implementation of MessageCommandService
 * Handles command operations for messages
 */
@Service
@Transactional
public class MessageCommandServiceImpl implements MessageCommandService {

    private final MessageRepository messageRepository;

    public MessageCommandServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public Optional<Message> handle(CreateMessageCommand command) {
        var message = new Message(command);
        var savedMessage = messageRepository.save(message);
        return Optional.of(savedMessage);
    }

    @Override
    public Optional<Message> handle(UpdateMessageCommand command) {
        var existingMessage = messageRepository.findById(command.id());
        if (existingMessage.isEmpty()) {
            return Optional.empty();
        }

        var message = existingMessage.get();
        message.updateMessage(command.body(), command.subject(), command.isUrgent());

        var updatedMessage = messageRepository.save(message);
        return Optional.of(updatedMessage);
    }

    @Override
    public Optional<Message> handle(MarkMessageAsReadCommand command) {
        var existingMessage = messageRepository.findById(command.id());
        if (existingMessage.isEmpty()) {
            return Optional.empty();
        }

        var message = existingMessage.get();
        message.markAsRead();

        var updatedMessage = messageRepository.save(message);
        return Optional.of(updatedMessage);
    }

    @Override
    public void handle(DeleteMessageCommand command) {
        messageRepository.deleteById(command.id());
    }
}
