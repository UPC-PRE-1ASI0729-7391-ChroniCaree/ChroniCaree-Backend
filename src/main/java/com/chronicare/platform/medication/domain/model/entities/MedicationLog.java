package com.chronicare.platform.medication.domain.model.entities;

import com.chronicare.platform.medication.domain.model.aggregates.Medication;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * MedicationLog entity - tracks medication actions
 */
@Entity
@Table(name = "medication_logs")
@Getter
@Setter
public class MedicationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime timestamp;

    private String action; // e.g., "TAKEN", "MISSED", "REFILLED"

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medication_id")
    private Medication medication;

    public MedicationLog() {
    }

    public MedicationLog(LocalDateTime timestamp, String action) {
        this.timestamp = timestamp;
        this.action = action;
    }
}
