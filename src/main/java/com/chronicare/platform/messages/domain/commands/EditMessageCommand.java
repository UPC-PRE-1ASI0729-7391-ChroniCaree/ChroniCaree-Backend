package com.chronicare.platform.messages.domain.commands;

public record EditMessageCommand(
    String messageId,
    String conversationId,
    Long tenantId,
    Long senderId,
    String newBody
) {
    public EditMessageCommand {
        if (messageId == null) throw new IllegalArgumentException("messageId is required");
        if (newBody == null || newBody.isBlank()) throw new IllegalArgumentException("newBody is required");
    }
}
