package com.chronicare.platform.invitations.interfaces.rest.transform;

import com.chronicare.platform.invitations.domain.model.commands.CreateInvitationCommand;
import com.chronicare.platform.invitations.interfaces.rest.resources.CreateInvitationResource;

/**
 * Assembler to transform CreateInvitationResource to CreateInvitationCommand
 */
public class CreateInvitationCommandFromResourceAssembler {

    public static CreateInvitationCommand toCommandFromResource(CreateInvitationResource resource) {
        return new CreateInvitationCommand(
            resource.tenantId(),
            resource.invitedBy(),
            resource.email(),
            resource.role() != null ? resource.role() : "doctor",
            resource.expiresInDays() != null ? resource.expiresInDays() : 7
        );
    }
}
