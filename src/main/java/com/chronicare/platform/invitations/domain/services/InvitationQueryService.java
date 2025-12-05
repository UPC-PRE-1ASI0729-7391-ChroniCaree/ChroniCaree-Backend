package com.chronicare.platform.invitations.domain.services;

import com.chronicare.platform.invitations.domain.model.aggregates.Invitation;
import com.chronicare.platform.invitations.domain.model.queries.*;

import java.util.List;
import java.util.Optional;

/**
 * Invitation Query Service interface
 * Handles all read operations for invitations
 */
public interface InvitationQueryService {

    /**
     * Get all invitations
     */
    List<Invitation> handle(GetAllInvitationsQuery query);

    /**
     * Get invitation by ID
     */
    Optional<Invitation> handle(GetInvitationByIdQuery query);

    /**
     * Get invitation by token
     */
    Optional<Invitation> handle(GetInvitationByTokenQuery query);

    /**
     * Get invitations by tenant ID
     */
    List<Invitation> handle(GetInvitationsByTenantIdQuery query);

    /**
     * Get invitations by tenant ID and status
     */
    List<Invitation> handle(GetInvitationsByTenantIdAndStatusQuery query);

    /**
     * Get invitation by email and tenant ID
     */
    Optional<Invitation> handle(GetInvitationByEmailAndTenantIdQuery query);
}
