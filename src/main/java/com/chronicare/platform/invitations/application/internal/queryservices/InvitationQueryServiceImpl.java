package com.chronicare.platform.invitations.application.internal.queryservices;

import com.chronicare.platform.invitations.domain.model.aggregates.Invitation;
import com.chronicare.platform.invitations.domain.model.queries.*;
import com.chronicare.platform.invitations.domain.model.valueobjects.InvitationStatus;
import com.chronicare.platform.invitations.domain.services.InvitationQueryService;
import com.chronicare.platform.invitations.infrastructure.persistence.jpa.repositories.InvitationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Summary: Implementation of Invitation Query Service
 */
@Service
public class InvitationQueryServiceImpl implements InvitationQueryService {

    private final InvitationRepository invitationRepository;

    public InvitationQueryServiceImpl(InvitationRepository invitationRepository) {
        this.invitationRepository = invitationRepository;
    }

    @Override
    public List<Invitation> handle(GetAllInvitationsQuery query) {
        return invitationRepository.findAll();
    }

    @Override
    public Optional<Invitation> handle(GetInvitationByIdQuery query) {
        return invitationRepository.findById(query.invitationId());
    }

    @Override
    public Optional<Invitation> handle(GetInvitationByTokenQuery query) {
        return invitationRepository.findByToken(query.token());
    }

    @Override
    public List<Invitation> handle(GetInvitationsByTenantIdQuery query) {
        return invitationRepository.findByTenantId(query.tenantId());
    }

    @Override
    public List<Invitation> handle(GetInvitationsByTenantIdAndStatusQuery query) {
        InvitationStatus status = parseStatus(query.status());
        return invitationRepository.findByTenantIdAndStatus(query.tenantId(), status);
    }

    @Override
    public Optional<Invitation> handle(GetInvitationByEmailAndTenantIdQuery query) {
        return invitationRepository.findByEmailAndTenantId(query.email(), query.tenantId());
    }

    private InvitationStatus parseStatus(String status) {
        if (status == null) return InvitationStatus.PENDING;
        return switch (status.toLowerCase()) {
            case "pending" -> InvitationStatus.PENDING;
            case "accepted" -> InvitationStatus.ACCEPTED;
            case "rejected" -> InvitationStatus.REJECTED;
            case "expired" -> InvitationStatus.EXPIRED;
            default -> InvitationStatus.PENDING;
        };
    }
}
