package com.chronicare.platform.messages.domain.queries;

public record GetConversationByIdQuery(
    String conversationId,
    Long tenantId
) {
    public GetConversationByIdQuery {
        if (conversationId == null) throw new IllegalArgumentException("conversationId is required");
    }
}
