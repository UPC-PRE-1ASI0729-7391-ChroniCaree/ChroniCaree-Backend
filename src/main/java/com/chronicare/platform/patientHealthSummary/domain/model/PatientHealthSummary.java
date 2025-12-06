/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.chronicare.platform.patientHealthSummary.domain.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * PatientHealthSummary Entity
 *
 * @summary
 * Represents the aggregated health summary of a patient within the system.
 * This entity consolidates key clinical and administrative information:
 * - Personal and identification data
 * - Assigned medical staff
 * - Current health status and alert indicators
 * - Recent vital signs and visit history
 * - Tenant and hospital context for multi-tenant environments
 * Serves as a read-optimized model used across various clinical dashboards
 * and patient monitoring features.
 */


@Entity
@Table(name = "patient_health_summaries")
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

    public PatientHealthSummary() {
    }

    public PatientHealthSummary(Long id, Long userId, Long assignedDoctorId, String firstName, String lastName, String dni, LocalDate birthDate, Gender gender, String phone, PatientHealthStatus healthStatus, Integer criticalAlertsCount, Integer activeAlertsCount, Integer activeMedicationsCount, Integer activeDiagnosesCount, LastVitalSigns lastVitalSigns, LocalDateTime lastVisit, LocalDateTime nextAppointment, LocalDate assignedSince, Long tenantId, String hospitalName) {
        this.id = id;
        this.userId = userId;
        this.assignedDoctorId = assignedDoctorId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dni = dni;
        this.birthDate = birthDate;
        this.gender = gender;
        this.phone = phone;
        this.healthStatus = healthStatus;
        this.criticalAlertsCount = criticalAlertsCount;
        this.activeAlertsCount = activeAlertsCount;
        this.activeMedicationsCount = activeMedicationsCount;
        this.activeDiagnosesCount = activeDiagnosesCount;
        this.lastVitalSigns = lastVitalSigns;
        this.lastVisit = lastVisit;
        this.nextAppointment = nextAppointment;
        this.assignedSince = assignedSince;
        this.tenantId = tenantId;
        this.hospitalName = hospitalName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getAssignedDoctorId() {
        return assignedDoctorId;
    }

    public void setAssignedDoctorId(Long assignedDoctorId) {
        this.assignedDoctorId = assignedDoctorId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public PatientHealthStatus getHealthStatus() {
        return healthStatus;
    }

    public void setHealthStatus(PatientHealthStatus healthStatus) {
        this.healthStatus = healthStatus;
    }

    public Integer getCriticalAlertsCount() {
        return criticalAlertsCount;
    }

    public void setCriticalAlertsCount(Integer criticalAlertsCount) {
        this.criticalAlertsCount = criticalAlertsCount;
    }

    public Integer getActiveAlertsCount() {
        return activeAlertsCount;
    }

    public void setActiveAlertsCount(Integer activeAlertsCount) {
        this.activeAlertsCount = activeAlertsCount;
    }

    public Integer getActiveMedicationsCount() {
        return activeMedicationsCount;
    }

    public void setActiveMedicationsCount(Integer activeMedicationsCount) {
        this.activeMedicationsCount = activeMedicationsCount;
    }

    public Integer getActiveDiagnosesCount() {
        return activeDiagnosesCount;
    }

    public void setActiveDiagnosesCount(Integer activeDiagnosesCount) {
        this.activeDiagnosesCount = activeDiagnosesCount;
    }

    public LastVitalSigns getLastVitalSigns() {
        return lastVitalSigns;
    }

    public void setLastVitalSigns(LastVitalSigns lastVitalSigns) {
        this.lastVitalSigns = lastVitalSigns;
    }

    public LocalDateTime getLastVisit() {
        return lastVisit;
    }

    public void setLastVisit(LocalDateTime lastVisit) {
        this.lastVisit = lastVisit;
    }

    public LocalDateTime getNextAppointment() {
        return nextAppointment;
    }

    public void setNextAppointment(LocalDateTime nextAppointment) {
        this.nextAppointment = nextAppointment;
    }

    public LocalDate getAssignedSince() {
        return assignedSince;
    }

    public void setAssignedSince(LocalDate assignedSince) {
        this.assignedSince = assignedSince;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }
}
