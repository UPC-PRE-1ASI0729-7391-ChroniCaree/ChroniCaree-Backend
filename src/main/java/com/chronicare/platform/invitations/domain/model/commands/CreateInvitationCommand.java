package com.chronicare.platform.invitations.domain.model.commands;

/**
 * Command to create a new invitation
 */
public record CreateInvitationCommand(
    Long tenantId,
    Long invitedBy,
    String email,
    String role,
    Integer expiresInDays
) {
    public CreateInvitationCommand {
        if (tenantId == null) throw new IllegalArgumentException("tenantId is required");
        if (invitedBy == null) throw new IllegalArgumentException("invitedBy is required");
        if (email == null || email.isBlank()) throw new IllegalArgumentException("email is required");
        if (expiresInDays == null || expiresInDays <= 0) expiresInDays = 7;
    }
}
