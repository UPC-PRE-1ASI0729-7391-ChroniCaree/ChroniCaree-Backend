package com.chronicare.platform.records.domain.model.entities;

import com.chronicare.platform.shared.domain.model.entities.AuditableEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "record_audit_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecordAuditLog extends AuditableEntity {

    @Column(nullable = false, name = "record_id")
    private Long recordId;

    @Column(nullable = false, name = "user_id")
    private Long userId;

    @Column(nullable = false)
    private String action; // CREATED, UPDATED, DELETED, VIEWED, EXPORTED

    @Column(columnDefinition = "TEXT")
    private String details; // JSON with change details

    @Column(nullable = false, name = "ip_address")
    private String ipAddress;

    @Column(name = "user_agent")
    private String userAgent;

    @Column(nullable = false, name = "timestamp")
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();
}
