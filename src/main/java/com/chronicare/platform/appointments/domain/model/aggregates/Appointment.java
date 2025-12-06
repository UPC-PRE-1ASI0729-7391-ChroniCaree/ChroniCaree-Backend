package com.chronicare.platform.appointments.domain.model.aggregates;

import com.chronicare.platform.appointments.domain.model.valueobjects.AppointmentStatus;
import com.chronicare.platform.appointments.domain.model.valueobjects.AppointmentType;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "appointments")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID appointmentId;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String patientId;

    @Column(nullable = false)
    private String doctorId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentStatus status;

    @Column(nullable = false)
    private LocalDateTime startAt;

    @Column(nullable = false)
    private LocalDateTime endAt;

    @Column(nullable = false)
    private Integer durationMinutes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentType type;

    @Column(nullable = false)
    private String createdBy;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    private LocalDateTime cancelledAt;
    private String notes;

    @Version
    private Long version;

    public Appointment() {}

    public Appointment(String tenantId, String patientId, String doctorId,
                     LocalDateTime startAt, LocalDateTime endAt, Integer durationMinutes,
                     AppointmentType type, String createdBy) {
        this.appointmentId = UUID.randomUUID();
        this.tenantId = tenantId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.startAt = startAt;
        this.endAt = endAt;
        this.durationMinutes = durationMinutes;
        this.type = type;
        this.status = AppointmentStatus.SCHEDULED;
        this.createdBy = createdBy;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public UUID getAppointmentId() { return appointmentId; }
    public void setAppointmentId(UUID appointmentId) { this.appointmentId = appointmentId; }

    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }

    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }

    public String getDoctorId() { return doctorId; }
    public void setDoctorId(String doctorId) { this.doctorId = doctorId; }

    public AppointmentStatus getStatus() { return status; }
    public void setStatus(AppointmentStatus status) { this.status = status; this.updatedAt = LocalDateTime.now(); }

    public LocalDateTime getStartAt() { return startAt; }
    public void setStartAt(LocalDateTime startAt) { this.startAt = startAt; }

    public LocalDateTime getEndAt() { return endAt; }
    public void setEndAt(LocalDateTime endAt) { this.endAt = endAt; }

    public Integer getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; }

    public AppointmentType getType() { return type; }
    public void setType(AppointmentType type) { this.type = type; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public LocalDateTime getCancelledAt() { return cancelledAt; }
    public void setCancelledAt(LocalDateTime cancelledAt) { this.cancelledAt = cancelledAt; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }

    public void confirm() {
        if (this.status != AppointmentStatus.SCHEDULED) {
            throw new IllegalStateException("Only SCHEDULED appointments can be confirmed");
        }
        this.status = AppointmentStatus.CONFIRMED;
        this.updatedAt = LocalDateTime.now();
    }

    public void complete() {
        if (this.status != AppointmentStatus.CONFIRMED) {
            throw new IllegalStateException("Only CONFIRMED appointments can be completed");
        }
        this.status = AppointmentStatus.COMPLETED;
        this.updatedAt = LocalDateTime.now();
    }

    public void cancel(String reason) {
        this.status = AppointmentStatus.CANCELLED;
        this.cancelledAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (reason != null) {
            this.notes = reason;
        }
    }

    public boolean canBeCancelled() {
        return this.status != AppointmentStatus.COMPLETED && this.status != AppointmentStatus.CANCELLED;
    }
}

