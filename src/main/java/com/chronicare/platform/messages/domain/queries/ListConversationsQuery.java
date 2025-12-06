package com.chronicare.platform.messages.domain.queries;

public record ListConversationsQuery(
    Long userId,
    Long tenantId,
    int page,
    int limit,
    boolean unreadOnly
) {
    public ListConversationsQuery(Long userId, Long tenantId, int page, int limit) {
        this(userId, tenantId, page, limit, false);
    }
}
