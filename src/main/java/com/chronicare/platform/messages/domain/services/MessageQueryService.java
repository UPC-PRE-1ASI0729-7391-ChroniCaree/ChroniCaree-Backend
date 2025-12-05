package com.chronicare.platform.messages.domain.services;

import com.chronicare.platform.messages.domain.model.aggregates.Message;
import com.chronicare.platform.messages.domain.model.queries.*;

import java.util.List;
import java.util.Optional;

/**
 * Message Query Service Interface
 * Handles queries for message operations
 */
public interface MessageQueryService {
    List<Message> handle(GetAllMessagesQuery query);
    Optional<Message> handle(GetMessageByIdQuery query);
    List<Message> handle(GetMessagesByThreadIdQuery query);
}
