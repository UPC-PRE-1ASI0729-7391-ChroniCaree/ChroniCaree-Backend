package com.chronicare.platform.messages.domain.services;

import com.chronicare.platform.messages.domain.model.aggregates.Conversation;
import com.chronicare.platform.messages.domain.commands.CreateConversationCommand;
import com.chronicare.platform.messages.domain.queries.GetConversationByIdQuery;
import com.chronicare.platform.messages.domain.queries.ListConversationsQuery;
import org.springframework.data.domain.Page;
import java.util.List;
import java.util.Optional;

/**
 * Domain Service for Conversation aggregate
 */
public interface ConversationQueryService {
    Optional<Conversation> handle(GetConversationByIdQuery query);
    Page<Conversation> handle(ListConversationsQuery query);
}
