package com.chronicare.platform.patients.application.internal.commandservices;

import com.chronicare.platform.patients.domain.model.aggregates.Patient;
import com.chronicare.platform.patients.domain.commands.CreatePatientCommand;
import com.chronicare.platform.patients.domain.commands.DeletePatientCommand;
import com.chronicare.platform.patients.domain.commands.UpdatePatientCommand;
import com.chronicare.platform.patients.domain.repository.PatientRepository;
import com.chronicare.platform.patients.domain.services.PatientCommandService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

/**
 * Implementation of the PatientCommandService.
 *
 * @summary
 * Handles all command-side operations for Patient aggregates, enforcing domain rules
 * and delegating persistence to the PatientRepository.
 * Responsibilities:
 * - Create a new patient while preventing duplicate DNI values.
 * - Update existing patient data using domain-level update logic.
 * - Delete patients safely after validating existence.
 * - Assign or unassign a doctor to a patient.
 * Business rules enforced:
 * - No two patients can share the same DNI.
 * - Patient must exist before being updated, deleted, or assigned/unassigned to a doctor.
 *
 * All methods run within transactional boundaries to ensure data consistency.
 */


@Service
public class PatientCommandServiceImpl implements PatientCommandService {
    private final PatientRepository patientRepository;

    public PatientCommandServiceImpl(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    @Transactional
    public Patient handle(CreatePatientCommand command) {
        if (patientRepository.existsByDni(command.dni())) {
            throw new IllegalArgumentException("Patient with DNI " + command.dni().value() + " already exists");
        }
        var patient = new Patient(command);
        return patientRepository.save(patient);
    }

    @Override
    @Transactional
    public Patient handle(UpdatePatientCommand command) {
        var patient = patientRepository.findById(command.patientId())
                .orElseThrow(() -> new IllegalArgumentException("Patient not found"));
        patient.update(command);
        return patientRepository.save(patient);
    }

    @Override
    @Transactional
    public void handle(DeletePatientCommand command) {
        if (!patientRepository.existsById(command.patientId())) {
            throw new IllegalArgumentException("Patient not found");
        }
        patientRepository.deleteById(command.patientId());
    }

    @Override
    @Transactional
    public Patient handleAssignDoctor(Long patientId, Long doctorId) {
        var patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found"));
        patient.assignDoctor(doctorId);
        return patientRepository.save(patient);
    }

    @Override
    @Transactional
    public Patient handleUnassignDoctor(Long patientId) {
        var patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found"));
        patient.unassignDoctor();
        return patientRepository.save(patient);
    }
}
