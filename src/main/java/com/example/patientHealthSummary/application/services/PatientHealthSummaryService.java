/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.patientHealthSummary.application.services;

import com.example.patientHealthSummary.domain.model.PatientHealthSummary;
import com.example.patientHealthSummary.domain.repository.PatientHealthSummaryRepository;
import com.example.patientHealthSummary.infrastructure.persistence.JpaPatientHealthSummaryRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PatientHealthSummaryService {

    private final PatientHealthSummaryRepository repository;

    public PatientHealthSummaryService(PatientHealthSummaryRepository repository) {
        this.repository = repository;
    }

    public List<PatientHealthSummary> getAllSummaries() {
        return repository.findAll();
    }

    public Optional<PatientHealthSummary> getSummaryById(Long id) {
        return repository.findById(id);
    }

    public PatientHealthSummary getSummaryByUserId(Long userId) {
        return ((JpaPatientHealthSummaryRepository) repository).findByUserId(userId);
    }

    public List<PatientHealthSummary> getSummariesByDoctorId(Long doctorId) {
        return ((JpaPatientHealthSummaryRepository) repository).findByAssignedDoctorId(doctorId);
    }

    public PatientHealthSummary createSummary(PatientHealthSummary summary) {
        return repository.save(summary);
    }

    public PatientHealthSummary updateSummary(Long id, PatientHealthSummary updated) {
        return repository.findById(id)
                .map(existing -> {
                    existing.setFirstName(updated.getFirstName());
                    existing.setLastName(updated.getLastName());
                    existing.setDni(updated.getDni());
                    existing.setBirthDate(updated.getBirthDate());
                    existing.setGender(updated.getGender());
                    existing.setPhone(updated.getPhone());
                    existing.setHealthStatus(updated.getHealthStatus());
                    existing.setCriticalAlertsCount(updated.getCriticalAlertsCount());
                    existing.setActiveAlertsCount(updated.getActiveAlertsCount());
                    existing.setActiveMedicationsCount(updated.getActiveMedicationsCount());
                    existing.setActiveDiagnosesCount(updated.getActiveDiagnosesCount());
                    existing.setLastVitalSigns(updated.getLastVitalSigns());
                    existing.setLastVisit(updated.getLastVisit());
                    existing.setNextAppointment(updated.getNextAppointment());
                    existing.setAssignedSince(updated.getAssignedSince());
                    existing.setTenantId(updated.getTenantId());
                    existing.setHospitalName(updated.getHospitalName());
                    return repository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Patient summary not found with id: " + id));
    }

    public void deleteSummary(Long id) {
        repository.deleteById(id);
    }
}
