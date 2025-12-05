package com.chronicare.platform.invitations.interfaces.rest.resources;

/**
 * Resource for accepting an invitation
 */
public record AcceptInvitationResource(
    String token,
    String firstName,
    String lastName,
    String password,
    String dni,
    String licenseNumber,
    String specialty,
    String phone
) {
}
