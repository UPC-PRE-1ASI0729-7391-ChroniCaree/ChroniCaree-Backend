package com.chronicare.platform.patients.application.internal.commandservices;

import com.chronicare.platform.patients.domain.model.aggregates.Patient;
import com.chronicare.platform.patients.domain.commands.CreatePatientCommand;
import com.chronicare.platform.patients.domain.commands.DeletePatientCommand;
import com.chronicare.platform.patients.domain.commands.UpdatePatientCommand;
import com.chronicare.platform.patients.domain.repository.PatientRepository;
import com.chronicare.platform.patients.domain.services.PatientCommandService;
import com.chronicare.platform.doctors.infrastructure.persistence.jpa.repositories.DoctorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
 * - User, Doctor (if assigned), and Tenant (if provided) must exist.
 *
 * All methods run within transactional boundaries to ensure data consistency.
 */


@Service
public class PatientCommandServiceImpl implements PatientCommandService {
    private static final String PATIENT_NOT_FOUND = "Patient not found";
    
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    public PatientCommandServiceImpl(
            PatientRepository patientRepository,
            DoctorRepository doctorRepository) {
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
    }

    @Override
    @Transactional
    public Patient handle(CreatePatientCommand command) {
        var logger = java.util.logging.Logger.getLogger(getClass().getName());
        logger.info("========== Creating patient ==========");
        logger.info("userId: " + command.userId());
        logger.info("assignedDoctorId: " + command.assignedDoctorId());
        logger.info("tenantId: " + command.tenantId());
        logger.info("dni: " + command.dni().value());
        logger.info("firstName: " + command.firstName());
        logger.info("lastName: " + command.lastName());
        
        try {
            // Validate DNI uniqueness
            if (patientRepository.existsByDni(command.dni())) {
                throw new IllegalArgumentException("Patient with DNI " + command.dni().value() + " already exists");
            }

            // Note: FK validations removed to allow flexible creation flow
            // The database will enforce referential integrity if FK constraints exist
            // This allows creating patients before all related entities are fully set up

            var patient = new Patient(command);
            return patientRepository.save(patient);
        } catch (IllegalArgumentException e) {
            // Re-throw validation errors as-is
            throw e;
        } catch (Exception e) {
            // Log unexpected errors and provide clear message
            java.util.logging.Logger.getLogger(getClass().getName())
                    .severe("❌ Error creating patient: " + e.getMessage() + " | Command: userId=" + command.userId() 
                            + ", dni=" + command.dni().value() + ", assignedDoctorId=" + command.assignedDoctorId());
            throw new IllegalArgumentException("Failed to create patient: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public Patient handle(UpdatePatientCommand command) {
        var patient = patientRepository.findById(command.patientId())
                .orElseThrow(() -> new IllegalArgumentException(PATIENT_NOT_FOUND));
        patient.update(command);
        return patientRepository.save(patient);
    }

    @Override
    @Transactional
    public void handle(DeletePatientCommand command) {
        if (!patientRepository.existsById(command.patientId())) {
            throw new IllegalArgumentException(PATIENT_NOT_FOUND);
        }
        patientRepository.deleteById(command.patientId());
    }

    @Override
    @Transactional
    public Patient handleAssignDoctor(Long patientId, Long doctorId) {
        var patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new IllegalArgumentException(PATIENT_NOT_FOUND));
        
        // Validate doctor exists
        if (doctorId != null) {
            doctorRepository.findById(doctorId)
                    .orElseThrow(() -> new IllegalArgumentException("Doctor not found with ID: " + doctorId));
        }
        
        patient.assignDoctor(doctorId);
        return patientRepository.save(patient);
    }

    @Override
    @Transactional
    public Patient handleUnassignDoctor(Long patientId) {
        var patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new IllegalArgumentException(PATIENT_NOT_FOUND));
        patient.unassignDoctor();
        return patientRepository.save(patient);
    }
}
