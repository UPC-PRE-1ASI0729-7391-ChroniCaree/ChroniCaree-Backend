package com.chronicare.platform.invitations.interfaces.rest.transform;

import com.chronicare.platform.invitations.domain.model.aggregates.Invitation;
import com.chronicare.platform.invitations.interfaces.rest.resources.InvitationResource;

import java.time.ZoneId;

/**
 * Summary: Assembler to transform Invitation entity to InvitationResource
 */
public class InvitationResourceFromEntityAssembler {

    public static InvitationResource toResourceFromEntity(Invitation invitation) {
        return new InvitationResource(
            invitation.getId(),
            invitation.getTenantId(),
            invitation.getInvitedBy(),
            invitation.getEmail(),
            invitation.getRole(),
            invitation.getStatus().name().toLowerCase(),
            invitation.getToken(),
            invitation.getExpiresAt(),
            invitation.getCreatedAt(),
            invitation.getAcceptedAt(),
            invitation.getRejectedAt()
        );
    }
}
