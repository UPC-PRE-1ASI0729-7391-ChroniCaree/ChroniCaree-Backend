package com.chronicare.platform.alerts.domain.model.aggregates;

import com.chronicare.platform.alerts.domain.model.commands.CreateAlertCommand;
import com.chronicare.platform.alerts.domain.model.commands.AcknowledgeAlertCommand;
import com.chronicare.platform.alerts.domain.model.commands.ResolveAlertCommand;
import com.chronicare.platform.alerts.domain.model.commands.EscalateAlertCommand;
import com.chronicare.platform.alerts.domain.model.valueobjects.AlertCategory;
import com.chronicare.platform.alerts.domain.model.valueobjects.AlertSeverity;
import com.chronicare.platform.alerts.domain.model.valueobjects.AlertStatus;
import com.chronicare.platform.alerts.domain.model.valueobjects.AlertType;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Summary: Alert Aggregate Root
 * Represents an alert in the system following Learning Center pattern
 */
@Entity
@Table(name = "alerts", indexes = {
    @Index(name = "idx_patient_status", columnList = "patient_id, status"),
    @Index(name = "idx_doctor_status", columnList = "doctor_id, status"),
    @Index(name = "idx_tenant_status", columnList = "tenant_id, status"),
    @Index(name = "idx_severity", columnList = "severity"),
    @Index(name = "idx_type", columnList = "type"),
    @Index(name = "idx_created_at", columnList = "created_at"),
    @Index(name = "idx_status_priority", columnList = "status, priority")
})
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relationships
    @Column(name = "patient_id", nullable = false)
    private Long patientId;

    @Column(name = "doctor_id")
    private Long doctorId;

    @Column(name = "tenant_id")
    private Long tenantId;

    // Alert Classification
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private AlertType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AlertSeverity severity;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private AlertCategory category;

    // Alert Content
    @Column(nullable = false, length = 255)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String message;

    @Column(columnDefinition = "TEXT")
    private String description;

    // Alert Data
    @Column(name = "source_type", length = 50)
    private String sourceType;

    @Column(name = "source_id")
    private Long sourceId;

    @Column(columnDefinition = "JSON")
    private String metadata;

    // Alert Status Management
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AlertStatus status = AlertStatus.ACTIVE;

    @Column(nullable = false)
    private Integer priority = 0;

    // Acknowledgment Tracking
    @Column(name = "acknowledged_by")
    private Long acknowledgedBy;

    @Column(name = "acknowledged_at")
    private LocalDateTime acknowledgedAt;

    @Column(name = "acknowledged_notes", columnDefinition = "TEXT")
    private String acknowledgedNotes;

    // Resolution Tracking
    @Column(name = "resolved_by")
    private Long resolvedBy;

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

    @Column(name = "resolution_notes", columnDefinition = "TEXT")
    private String resolutionNotes;

    @Column(name = "resolution_action", length = 100)
    private String resolutionAction;

    // Escalation
    @Column(nullable = false)
    private Boolean escalated = false;

    @Column(name = "escalated_to")
    private Long escalatedTo;

    @Column(name = "escalated_at")
    private LocalDateTime escalatedAt;

    @Column(name = "escalation_level")
    private Integer escalationLevel = 0;

    // Timestamps
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    protected Alert() {
        // Required by JPA
    }

    public Alert(CreateAlertCommand command) {
        this.patientId = command.patientId();
        this.doctorId = command.doctorId();
        this.tenantId = command.tenantId();
        this.type = AlertType.fromCode(command.type());
        this.severity = AlertSeverity.fromCode(command.severity());
        this.category = command.category() != null ? AlertCategory.fromCode(command.category()) : null;
        this.title = command.title();
        this.message = command.message();
        this.description = command.description();
        this.sourceType = command.sourceType();
        this.sourceId = command.sourceId();
        this.metadata = command.metadata();
        this.priority = command.priority() != null ? command.priority() : severity.getPriority();
        this.status = AlertStatus.ACTIVE;
        this.escalated = false;
        this.escalationLevel = 0;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (command.expiresAt() != null) {
            this.expiresAt = LocalDateTime.parse(command.expiresAt().replace("Z", ""));
        }
    }

    // Domain Methods
    public void acknowledge(AcknowledgeAlertCommand command) {
        if (!status.canTransitionTo(AlertStatus.ACKNOWLEDGED)) {
            throw new IllegalStateException("Cannot acknowledge alert with status: " + status);
        }
        this.status = AlertStatus.ACKNOWLEDGED;
        this.acknowledgedBy = command.acknowledgedBy();
        this.acknowledgedAt = LocalDateTime.now();
        this.acknowledgedNotes = command.notes();
        this.updatedAt = LocalDateTime.now();
    }

    public void resolve(ResolveAlertCommand command) {
        if (!status.canTransitionTo(AlertStatus.RESOLVED)) {
            throw new IllegalStateException("Cannot resolve alert with status: " + status);
        }
        this.status = AlertStatus.RESOLVED;
        this.resolvedBy = command.resolvedBy();
        this.resolvedAt = LocalDateTime.now();
        this.resolutionNotes = command.resolutionNotes();
        this.resolutionAction = command.resolutionAction();
        this.updatedAt = LocalDateTime.now();
    }

    public void escalate(EscalateAlertCommand command) {
        if (!status.canTransitionTo(AlertStatus.ESCALATED)) {
            throw new IllegalStateException("Cannot escalate alert with status: " + status);
        }
        this.status = AlertStatus.ESCALATED;
        this.escalated = true;
        this.escalatedTo = command.escalatedTo();
        this.escalatedAt = LocalDateTime.now();
        this.escalationLevel = command.escalationLevel() != null ? command.escalationLevel() : this.escalationLevel + 1;
        this.updatedAt = LocalDateTime.now();
    }

    public void dismiss(Long dismissedBy, String notes) {
        if (!status.canTransitionTo(AlertStatus.DISMISSED)) {
            throw new IllegalStateException("Cannot dismiss alert with status: " + status);
        }
        this.status = AlertStatus.DISMISSED;
        this.resolvedBy = dismissedBy;
        this.resolvedAt = LocalDateTime.now();
        this.resolutionNotes = notes;
        this.resolutionAction = "dismissed";
        this.updatedAt = LocalDateTime.now();
    }

    public void expire() {
        if (status == AlertStatus.ACTIVE) {
            this.status = AlertStatus.EXPIRED;
            this.updatedAt = LocalDateTime.now();
        }
    }

    public boolean isExpired() {
        return expiresAt != null && LocalDateTime.now().isAfter(expiresAt) && status == AlertStatus.ACTIVE;
    }

    public boolean isCritical() {
        return severity == AlertSeverity.CRITICAL;
    }

    public boolean needsEscalation(int minutesSinceCreation) {
        if (status != AlertStatus.ACTIVE) return false;
        if (severity == AlertSeverity.CRITICAL && minutesSinceCreation >= 30) return true;
        if (severity == AlertSeverity.HIGH && minutesSinceCreation >= 120) return true;
        return false;
    }

    // Getters
    public Long getId() { return id; }
    public Long getPatientId() { return patientId; }
    public Long getDoctorId() { return doctorId; }
    public Long getTenantId() { return tenantId; }
    public AlertType getType() { return type; }
    public AlertSeverity getSeverity() { return severity; }
    public AlertCategory getCategory() { return category; }
    public String getTitle() { return title; }
    public String getMessage() { return message; }
    public String getDescription() { return description; }
    public String getSourceType() { return sourceType; }
    public Long getSourceId() { return sourceId; }
    public String getMetadata() { return metadata; }
    public AlertStatus getStatus() { return status; }
    public Integer getPriority() { return priority; }
    public Long getAcknowledgedBy() { return acknowledgedBy; }
    public LocalDateTime getAcknowledgedAt() { return acknowledgedAt; }
    public String getAcknowledgedNotes() { return acknowledgedNotes; }
    public Long getResolvedBy() { return resolvedBy; }
    public LocalDateTime getResolvedAt() { return resolvedAt; }
    public String getResolutionNotes() { return resolutionNotes; }
    public String getResolutionAction() { return resolutionAction; }
    public Boolean getEscalated() { return escalated; }
    public Long getEscalatedTo() { return escalatedTo; }
    public LocalDateTime getEscalatedAt() { return escalatedAt; }
    public Integer getEscalationLevel() { return escalationLevel; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public LocalDateTime getExpiresAt() { return expiresAt; }
    public LocalDateTime getDeletedAt() { return deletedAt; }

    // Setters for JPA (minimal)
    public void setId(Long id) { this.id = id; }
}
