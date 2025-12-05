package com.chronicare.platform.invitations.domain.model.queries;

/**
 * Query to get invitation by email and tenant ID
 */
public record GetInvitationByEmailAndTenantIdQuery(String email, Long tenantId) {
    public GetInvitationByEmailAndTenantIdQuery {
        if (email == null || email.isBlank()) throw new IllegalArgumentException("email is required");
        if (tenantId == null) throw new IllegalArgumentException("tenantId is required");
    }
}
