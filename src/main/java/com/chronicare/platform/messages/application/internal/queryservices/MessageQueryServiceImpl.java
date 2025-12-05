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
 * Implementation of MessageQueryService
 * Handles query operations for messages
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
