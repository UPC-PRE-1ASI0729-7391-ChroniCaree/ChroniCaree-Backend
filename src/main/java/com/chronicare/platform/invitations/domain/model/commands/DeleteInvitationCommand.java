package com.chronicare.platform.invitations.domain.model.commands;

/**
 * Command to delete/cancel an invitation
 */
public record DeleteInvitationCommand(Long invitationId) {
    public DeleteInvitationCommand {
        if (invitationId == null) throw new IllegalArgumentException("invitationId is required");
    }
}
