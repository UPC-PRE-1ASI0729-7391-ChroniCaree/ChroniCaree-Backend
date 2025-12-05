package com.chronicare.platform.invitations.infrastructure.persistence.jpa.repositories;

import com.chronicare.platform.invitations.domain.model.aggregates.Invitation;
import com.chronicare.platform.invitations.domain.model.valueobjects.InvitationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Invitation entities
 */
@Repository
public interface InvitationRepository extends JpaRepository<Invitation, Long> {

    /**
     * Find all invitations by tenant ID
     */
    List<Invitation> findByTenantId(Long tenantId);

    /**
     * Find all invitations by tenant ID and status
     */
    List<Invitation> findByTenantIdAndStatus(Long tenantId, InvitationStatus status);

    /**
     * Find invitation by token
     */
    Optional<Invitation> findByToken(String token);

    /**
     * Find invitation by email and tenant ID
     */
    Optional<Invitation> findByEmailAndTenantId(String email, Long tenantId);

    /**
     * Find pending invitation by email and tenant ID
     */
    Optional<Invitation> findByEmailAndTenantIdAndStatus(String email, Long tenantId, InvitationStatus status);

    /**
     * Check if invitation exists by email and tenant ID with pending status
     */
    boolean existsByEmailAndTenantIdAndStatus(String email, Long tenantId, InvitationStatus status);

    /**
     * Find all invitations by status
     */
    List<Invitation> findByStatus(InvitationStatus status);

    /**
     * Count invitations by tenant ID and status
     */
    long countByTenantIdAndStatus(Long tenantId, InvitationStatus status);
}
