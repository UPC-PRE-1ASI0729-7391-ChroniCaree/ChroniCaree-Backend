package com.chronicare.platform.messages.domain.commands;

public record CreateMessageCommand(
    String conversationId,
    Long tenantId,
    Long senderId,
    String body,
    String contentType,
    java.util.List<String> attachmentIds,
    String metadata,
    // Legacy thread-based fields
    Long threadId,
    com.chronicare.platform.messages.domain.model.valueobjects.SenderRole senderRole,
    Long receiverId,
    String subject,
    boolean isUrgent
) {
    public CreateMessageCommand(String conversationId, Long tenantId, Long senderId, String body) {
        this(conversationId, tenantId, senderId, body, "TEXT", null, null, null, null, null, null, false);
    }
}
