package com.chronicare.platform.messages.domain.queries;

public record ListMessagesQuery(
    String conversationId,
    Long tenantId,
    int page,
    int limit,
    String before,
    String after
) {
    public ListMessagesQuery(String conversationId, Long tenantId, int page, int limit) {
        this(conversationId, tenantId, page, limit, null, null);
    }
}
