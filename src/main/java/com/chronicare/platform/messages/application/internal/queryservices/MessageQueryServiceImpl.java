package com.chronicare.platform.messages.application.internal.queryservices;

import com.chronicare.platform.messages.domain.model.aggregates.Message;
import com.chronicare.platform.messages.domain.model.queries.*;
import com.chronicare.platform.messages.domain.services.MessageQueryService;
import com.chronicare.platform.messages.infrastructure.persistence.jpa.repositories.MessageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * MessageQueryService Implementation
 *
 * @summary
 * Provides read-only query operations for retrieving Message aggregates.
 * Business rules enforced:
 * - Queries execute in read-only mode to ensure data integrity.
 * - Supports retrieval of messages by ID, thread ID, and complete listing.
 * - Adheres to CQRS principles by exposing only read-side operations.
 *
 */

@Service
@Transactional(readOnly = true)
public class MessageQueryServiceImpl implements MessageQueryService {

    private final MessageRepository messageRepository;

    public MessageQueryServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public List<Message> handle(GetAllMessagesQuery query) {
        return messageRepository.findAll();
    }

    @Override
    public Optional<Message> handle(GetMessageByIdQuery query) {
        return messageRepository.findById(query.id());
    }

    @Override
    public List<Message> handle(GetMessagesByThreadIdQuery query) {
        return messageRepository.findByThreadId(query.threadId());
    }
}
