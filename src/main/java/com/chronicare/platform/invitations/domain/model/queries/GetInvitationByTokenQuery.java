package com.chronicare.platform.invitations.domain.model.queries;

/**
 * Query to get invitation by token
 */
public record GetInvitationByTokenQuery(String token) {
    public GetInvitationByTokenQuery {
        if (token == null || token.isBlank()) throw new IllegalArgumentException("token is required");
    }
}
