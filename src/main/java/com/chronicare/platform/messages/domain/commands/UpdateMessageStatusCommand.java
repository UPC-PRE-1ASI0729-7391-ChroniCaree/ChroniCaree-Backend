package com.chronicare.platform.messages.domain.commands;

public record UpdateMessageStatusCommand(
    String messageId,
    String conversationId,
    Long tenantId,
    Long recipientId,
    String status,
    String timestamp
) {
    public UpdateMessageStatusCommand {
        if (messageId == null) throw new IllegalArgumentException("messageId is required");
        if (recipientId == null) throw new IllegalArgumentException("recipientId is required");
        if (status == null) throw new IllegalArgumentException("status is required");
    }
}
