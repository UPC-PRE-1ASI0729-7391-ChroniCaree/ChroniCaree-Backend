package com.chronicare.platform.records.domain.model.entities;

import com.chronicare.platform.shared.domain.model.entities.AuditableEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "record_consents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecordConsent extends AuditableEntity {

    @Column(nullable = false, name = "record_id")
    private Long recordId;

    @Column(nullable = false, name = "patient_id")
    private Long patientId;

    @Column(nullable = false, name = "granted_to_user_id")
    private Long grantedToUserId;

    @Column(nullable = false, name = "granted_at")
    @Builder.Default
    private LocalDateTime grantedAt = LocalDateTime.now();

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @Column(name = "revoked")
    @Builder.Default
    private Boolean revoked = false;

    @Column(name = "revoked_at")
    private LocalDateTime revokedAt;

    @Column(name = "purpose")
    private String purpose;

    public boolean isValid() {
        if (revoked) {
            return false;
        }
        if (expiresAt != null && LocalDateTime.now().isAfter(expiresAt)) {
            return false;
        }
        return true;
    }

    public void revoke() {
        this.revoked = true;
        this.revokedAt = LocalDateTime.now();
    }
}
