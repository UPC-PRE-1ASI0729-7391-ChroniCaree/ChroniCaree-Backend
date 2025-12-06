package com.chronicare.platform.messages.domain.services;

import com.chronicare.platform.messages.domain.model.aggregates.Conversation;
import com.chronicare.platform.messages.domain.commands.CreateConversationCommand;

/**
 * Domain Command Service for Conversation aggregate
 */
public interface ConversationCommandService {
    Conversation handle(CreateConversationCommand command);
}
