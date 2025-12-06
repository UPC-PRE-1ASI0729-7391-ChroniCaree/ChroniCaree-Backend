package com.chronicare.platform.messages.domain.commands;

public record DeleteMessageCommand(
    String messageId,
    String conversationId,
    Long tenantId,
    boolean hardDelete
) {
    public DeleteMessageCommand {
        if (messageId == null) throw new IllegalArgumentException("messageId is required");
    }

    public DeleteMessageCommand(String messageId, String conversationId, Long tenantId) {
        this(messageId, conversationId, tenantId, false);
    }
}
