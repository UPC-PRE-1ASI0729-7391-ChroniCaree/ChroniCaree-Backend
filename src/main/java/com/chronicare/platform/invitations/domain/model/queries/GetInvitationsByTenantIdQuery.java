package com.chronicare.platform.invitations.domain.model.queries;

/**
 * Query to get invitations by tenant ID
 */
public record GetInvitationsByTenantIdQuery(Long tenantId) {
    public GetInvitationsByTenantIdQuery {
        if (tenantId == null) throw new IllegalArgumentException("tenantId is required");
    }
}
