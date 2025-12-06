package com.chronicare.platform.messages.domain.commands;

import com.chronicare.platform.messages.domain.model.valueobjects.ConversationType;
import com.chronicare.platform.messages.domain.model.valueobjects.Participant;
import java.util.Set;

public record CreateConversationCommand(
    Long tenantId,
    ConversationType type,
    Set<Participant> participants,
    String subject,
    String metadata
) {
    public CreateConversationCommand {
        if (tenantId == null) throw new IllegalArgumentException("tenantId is required");
        if (type == null) throw new IllegalArgumentException("type is required");
        if (participants == null || participants.isEmpty()) throw new IllegalArgumentException("participants cannot be empty");
    }
}
