package com.chronicare.platform.invitations.domain.model.queries;

/**
 * Summary: Query to get invitation by ID
 */
public record GetInvitationByIdQuery(Long invitationId) {
    public GetInvitationByIdQuery {
        if (invitationId == null) throw new IllegalArgumentException("invitationId is required");
    }
}
