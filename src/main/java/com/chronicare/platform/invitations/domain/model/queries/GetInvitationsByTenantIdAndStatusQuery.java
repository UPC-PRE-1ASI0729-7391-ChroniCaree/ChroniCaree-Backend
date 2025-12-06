package com.chronicare.platform.invitations.domain.model.queries;

/**
 * Summary: Query to get invitations by tenant ID and status
 */
public record GetInvitationsByTenantIdAndStatusQuery(Long tenantId, String status) {
    public GetInvitationsByTenantIdAndStatusQuery {
        if (tenantId == null) throw new IllegalArgumentException("tenantId is required");
        if (status == null || status.isBlank()) throw new IllegalArgumentException("status is required");
    }
}
