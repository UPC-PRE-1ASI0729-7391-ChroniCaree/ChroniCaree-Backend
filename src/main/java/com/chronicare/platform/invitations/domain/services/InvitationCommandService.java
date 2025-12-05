package com.chronicare.platform.invitations.domain.services;

import com.chronicare.platform.invitations.domain.model.aggregates.Invitation;
import com.chronicare.platform.invitations.domain.model.commands.AcceptInvitationCommand;
import com.chronicare.platform.invitations.domain.model.commands.CreateInvitationCommand;
import com.chronicare.platform.invitations.domain.model.commands.DeleteInvitationCommand;
import com.chronicare.platform.invitations.domain.model.commands.UpdateInvitationStatusCommand;
import com.chronicare.platform.invitations.interfaces.rest.resources.AcceptInvitationResponse;

import java.util.Optional;

/**
 * Invitation Command Service interface
 * Handles all write operations for invitations
 */
public interface InvitationCommandService {

    /**
     * Create a new invitation
     */
    Optional<Invitation> handle(CreateInvitationCommand command);

    /**
     * Accept an invitation and create doctor account
     */
    Optional<AcceptInvitationResponse> handle(AcceptInvitationCommand command);

    /**
     * Update invitation status
     */
    Optional<Invitation> handle(UpdateInvitationStatusCommand command);

    /**
     * Delete/cancel an invitation
     */
    void handle(DeleteInvitationCommand command);
}
