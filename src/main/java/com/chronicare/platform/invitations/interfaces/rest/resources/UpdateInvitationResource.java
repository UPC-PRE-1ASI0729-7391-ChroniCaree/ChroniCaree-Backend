package com.chronicare.platform.invitations.interfaces.rest.resources;

/**
 * Summary: Resource for updating invitation status
 */
public record UpdateInvitationResource(
    String status,
    String acceptedAt,
    String rejectedAt
) {
}
