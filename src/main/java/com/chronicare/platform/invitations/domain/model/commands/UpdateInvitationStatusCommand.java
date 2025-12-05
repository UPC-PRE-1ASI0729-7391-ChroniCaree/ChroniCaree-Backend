package com.chronicare.platform.invitations.domain.model.commands;

/**
 * Command to update invitation status
 */
public record UpdateInvitationStatusCommand(
    Long invitationId,
    String status
) {
    public UpdateInvitationStatusCommand {
        if (invitationId == null) throw new IllegalArgumentException("invitationId is required");
        if (status == null || status.isBlank()) throw new IllegalArgumentException("status is required");
    }
}
