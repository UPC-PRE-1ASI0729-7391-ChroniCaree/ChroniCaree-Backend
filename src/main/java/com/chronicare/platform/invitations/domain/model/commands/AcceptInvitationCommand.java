package com.chronicare.platform.invitations.domain.model.commands;

/**
 * Summary: Command to accept an invitation and create a doctor account
 */
public record AcceptInvitationCommand(
    Long invitationId,
    String token,
    String firstName,
    String lastName,
    String password,
    String dni,
    String licenseNumber,
    String specialty,
    String phone
) {
    public AcceptInvitationCommand {
        if (invitationId == null) throw new IllegalArgumentException("invitationId is required");
        if (token == null || token.isBlank()) throw new IllegalArgumentException("token is required");
        if (firstName == null || firstName.isBlank()) throw new IllegalArgumentException("firstName is required");
        if (lastName == null || lastName.isBlank()) throw new IllegalArgumentException("lastName is required");
        if (password == null || password.isBlank()) throw new IllegalArgumentException("password is required");
        if (licenseNumber == null || licenseNumber.isBlank()) throw new IllegalArgumentException("licenseNumber is required");
        if (specialty == null || specialty.isBlank()) throw new IllegalArgumentException("specialty is required");
    }
}
