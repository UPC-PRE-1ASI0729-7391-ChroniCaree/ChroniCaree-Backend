package com.chronicare.platform.messages.domain.queries;

public record GetMessageByIdQuery(
    String messageId,
    String conversationId,
    Long tenantId
) {
    public GetMessageByIdQuery {
        if (messageId == null) throw new IllegalArgumentException("messageId is required");
    }
}
