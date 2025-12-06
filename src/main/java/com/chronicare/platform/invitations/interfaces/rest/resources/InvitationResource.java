package com.chronicare.platform.invitations.interfaces.rest.resources;

import java.time.LocalDateTime;

/**
 * Summary: Resource for Invitation responses
 */
public record InvitationResource(
    Long id,
    Long tenantId,
    Long invitedBy,
    String email,
    String role,
    String status,
    String token,
    LocalDateTime expiresAt,
    LocalDateTime createdAt,
    LocalDateTime acceptedAt,
    LocalDateTime rejectedAt
) {
}
