package com.chronicare.platform.invitations.interfaces.rest.transform;

import com.chronicare.platform.invitations.domain.model.commands.AcceptInvitationCommand;
import com.chronicare.platform.invitations.interfaces.rest.resources.AcceptInvitationResource;

/**
 * Summary: Assembler to transform AcceptInvitationResource to AcceptInvitationCommand
 */
public class AcceptInvitationCommandFromResourceAssembler {

    public static AcceptInvitationCommand toCommandFromResource(Long invitationId, AcceptInvitationResource resource) {
        return new AcceptInvitationCommand(
            invitationId,
            resource.token(),
            resource.firstName(),
            resource.lastName(),
            resource.password(),
            resource.dni(),
            resource.licenseNumber(),
            resource.specialty(),
            resource.phone()
        );
    }
}
