package com.chronicare.platform.invitations.domain.model.aggregates;

import com.chronicare.platform.invitations.domain.model.valueobjects.InvitationStatus;
import com.chronicare.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Invitation Aggregate Root
 * Represents an invitation for a doctor to join a hospital/tenant
 */
@Entity
@Table(name = "invitations", indexes = {
    @Index(name = "idx_invitation_tenant_status", columnList = "tenant_id, status"),
    @Index(name = "idx_invitation_token", columnList = "token"),
    @Index(name = "idx_invitation_email", columnList = "email")
})
@Getter
@Setter
@NoArgsConstructor
public class Invitation extends AuditableAbstractAggregateRoot<Invitation> {

    @Column(name = "tenant_id", nullable = false)
    private Long tenantId;

    @Column(name = "invited_by", nullable = false)
    private Long invitedBy;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "role", nullable = false)
    private String role;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private InvitationStatus status;

    @Column(name = "token", nullable = false, unique = true)
    private String token;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "accepted_at")
    private LocalDateTime acceptedAt;

    @Column(name = "rejected_at")
    private LocalDateTime rejectedAt;

    /**
     * Create a new invitation
     */
    public Invitation(Long tenantId, Long invitedBy, String email, String role, int expiresInDays) {
        this.tenantId = tenantId;
        this.invitedBy = invitedBy;
        this.email = email;
        this.role = role != null ? role : "doctor";
        this.status = InvitationStatus.PENDING;
        this.token = UUID.randomUUID().toString();
        this.expiresAt = LocalDateTime.now().plusDays(expiresInDays > 0 ? expiresInDays : 7);
    }

    /**
     * Accept the invitation
     */
    public void accept() {
        if (this.status != InvitationStatus.PENDING) {
            throw new IllegalStateException("Invitation is not pending");
        }
        if (this.expiresAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Invitation has expired");
        }
        this.status = InvitationStatus.ACCEPTED;
        this.acceptedAt = LocalDateTime.now();
    }

    /**
     * Reject the invitation
     */
    public void reject() {
        if (this.status != InvitationStatus.PENDING) {
            throw new IllegalStateException("Invitation is not pending");
        }
        this.status = InvitationStatus.REJECTED;
        this.rejectedAt = LocalDateTime.now();
    }

    /**
     * Cancel/expire the invitation
     */
    public void cancel() {
        if (this.status != InvitationStatus.PENDING) {
            throw new IllegalStateException("Invitation is not pending");
        }
        this.status = InvitationStatus.EXPIRED;
    }

    /**
     * Check if the invitation is valid
     */
    public boolean isValid() {
        return this.status == InvitationStatus.PENDING && 
               this.expiresAt.isAfter(LocalDateTime.now());
    }

    /**
     * Check if the invitation is expired
     */
    public boolean isExpired() {
        return this.expiresAt.isBefore(LocalDateTime.now());
    }
}
