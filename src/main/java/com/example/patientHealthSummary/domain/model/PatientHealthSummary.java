/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.patientHealthSummary.domain.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "patient_health_summaries")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientHealthSummary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Long assignedDoctorId;

    private String firstName;

    private String lastName;

    @Column(unique = true)
    private String dni;

    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String phone;

    @Enumerated(EnumType.STRING)
    private PatientHealthStatus healthStatus;

    private Integer criticalAlertsCount;
    private Integer activeAlertsCount;
    private Integer activeMedicationsCount;
    private Integer activeDiagnosesCount;

    @Embedded
    private LastVitalSigns lastVitalSigns;

    private LocalDateTime lastVisit;
    private LocalDateTime nextAppointment;

    private LocalDate assignedSince;

    private Long tenantId;

    private String hospitalName;
}
