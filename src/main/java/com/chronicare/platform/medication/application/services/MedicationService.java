/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.chronicare.platform.medication.application.services;

import com.chronicare.platform.medication.domain.aggregate.Medication;
import com.chronicare.platform.medication.domain.command.UpdateMedicationCommand;
import com.chronicare.platform.medication.domain.event.MedicationCreatedEvent;
import com.chronicare.platform.medication.domain.event.MedicationUpdatedEvent;
import com.chronicare.platform.medication.domain.queries.GetAllMedicationsQuery;
import com.chronicare.platform.medication.domain.queries.GetMedicationsByPatientIdQuery;
import com.chronicare.platform.medication.domain.repository.MedicationRepository;
import com.chronicare.platform.patients.domain.model.aggregates.Patient;
import com.chronicare.platform.patients.domain.repository.PatientRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MedicationService {

    private final MedicationRepository medicationRepository;
    private final PatientRepository patientRepository;
    private final ApplicationEventPublisher eventPublisher;

    // ==========================================================
    // QUERY HANDLERS (FALTABAN → YA ESTÁN IMPLEMENTADOS)
    // ==========================================================
    public List<Medication> handle(GetAllMedicationsQuery query) {
        return medicationRepository.findAll();
    }

    public Optional<Medication> handle(GetMedicationByIdQuery query) {
        return medicationRepository.findById(query.medicationId());
    }

    public List<Medication> handle(GetMedicationsByPatientIdQuery query) {
        return medicationRepository.findByPatientId(query.patientId());
    }

    public void handleDeleteMedication(Long id) {
        medicationRepository.deleteById(id);
    }

    // ==========================================================
    // COMMAND HANDLERS
    // ==========================================================
    @Transactional
    public Medication handle(CreateMedicationCommand command) {

        Patient patient = patientRepository.findById(command.patientId())
                .orElseThrow(() -> new IllegalArgumentException(
                "Patient not found with id: " + command.patientId()));

        Medication medication = new Medication();
        medication.setPatient(patient);
        medication.updateFrom(command);

        medication = medicationRepository.save(medication);

        MedicationCreatedEvent event = new MedicationCreatedEvent(
                this,
                medication.getId(),
                patient,
                medication.getName(),
                medication.getType(),
                medication.getDosage(),
                medication.getSchedule(),
                medication.getPrescribedBy(),
                medication.getPrescribedDate(),
                medication.getStatus()
        );

        eventPublisher.publishEvent(event);

        return medication;
    }

    @Transactional
    public Medication handle(UpdateMedicationCommand command) {

        Medication medication = medicationRepository.findById(command.id())
                .orElseThrow(() -> new IllegalArgumentException(
                "Medication not found with id: " + command.id()));

        Patient patient = patientRepository.findById(command.patientId())
                .orElseThrow(() -> new IllegalArgumentException(
                "Patient not found with id: " + command.patientId()));

        medication.setPatient(patient);
        medication.updateFrom(command);

        medication = medicationRepository.save(medication);

        MedicationUpdatedEvent event = new MedicationUpdatedEvent(
                this,
                medication.getId(),
                patient,
                medication.getName(),
                medication.getType(),
                medication.getDosage(),
                medication.getSchedule(),
                medication.getPrescribedBy(),
                medication.getPrescribedDate(),
                medication.getStatus()
        );

        eventPublisher.publishEvent(event);

        return medication;
    }
}
