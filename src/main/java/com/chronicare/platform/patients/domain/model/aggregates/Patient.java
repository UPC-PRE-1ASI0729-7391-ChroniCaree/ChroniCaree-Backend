package com.chronicare.platform.patients.domain.model.aggregates;

import com.chronicare.platform.patients.domain.commands.CreatePatientCommand;
import com.chronicare.platform.patients.domain.commands.UpdatePatientCommand;
import com.chronicare.platform.patients.domain.valueobjects.Dni;
import com.chronicare.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "patients")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
public class Patient extends AuditableAbstractAggregateRoot<Patient> {

    @Embedded
    private Dni dni;

    private Long userId;
    private Long tenantId;
    private Long assignedDoctorId;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate birthDate;
    private String gender;
    private String phone;
    private String address;
    private String photoUrl;
    private Double weight;
    private Double height;
    private Double bmi;

    public Patient(CreatePatientCommand command) {
        this.userId = command.userId();
        this.assignedDoctorId = command.assignedDoctorId(); // Assign doctor (optional)
        this.tenantId = command.tenantId(); // Assign to hospital/clinic (optional)
        this.firstName = command.firstName();
        this.lastName = command.lastName();
        this.email = command.email();
        this.dni = command.dni();
        this.birthDate = parseBirthDate(command.birthDate());
        this.gender = command.gender();
        this.phone = command.phone();
        this.address = command.address();
        this.photoUrl = command.photoUrl();
        this.weight = command.weight();
        this.height = command.height();
        this.bmi = calculateBMI(command.weight(), command.height());
    }

    /**
     * Assigns a doctor to this patient
     * @param doctorId The doctor ID to assign
     */
    public void assignDoctor(Long doctorId) {
        this.assignedDoctorId = doctorId;
    }

    /**
     * Removes the assigned doctor from this patient
     */
    public void unassignDoctor() {
        this.assignedDoctorId = null;
    }

    public void update(UpdatePatientCommand command) {
        this.firstName = command.firstName();
        this.lastName = command.lastName();
        this.email = command.email();
        this.dni = command.dni();
        this.birthDate = parseBirthDate(command.birthDate());
        this.gender = command.gender();
        this.phone = command.phone();
        this.address = command.address();
        this.photoUrl = command.photoUrl();
        this.weight = command.weight();
        this.height = command.height();
        this.bmi = calculateBMI(command.weight(), command.height());
    }

    private Double calculateBMI(Double weight, Double height) {
        if (weight != null && height != null && weight > 0 && height > 0) {
            return weight / (height * height);
        }
        return null;
    }

    private LocalDate parseBirthDate(String rawDate) {
        if (rawDate == null || rawDate.isBlank()) {
            return null;
        }
        try {
            return LocalDate.parse(rawDate);
        } catch (java.time.format.DateTimeParseException _) {
            // Try trimming time portion if comes as ISO datetime
            try {
                return java.time.OffsetDateTime.parse(rawDate).toLocalDate();
            } catch (java.time.format.DateTimeParseException _) {
                throw new IllegalArgumentException("Invalid birthDate format. Expected ISO date (yyyy-MM-dd)");
            }
        }
    }
}
