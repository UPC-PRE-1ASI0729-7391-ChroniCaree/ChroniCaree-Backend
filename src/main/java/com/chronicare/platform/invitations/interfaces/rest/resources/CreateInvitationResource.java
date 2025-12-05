package com.chronicare.platform.invitations.interfaces.rest.resources;

/**
 * Resource for creating a new invitation
 */
public record CreateInvitationResource(
    Long tenantId,
    Long invitedBy,
    String email,
    String role,
    Integer expiresInDays
) {
}
