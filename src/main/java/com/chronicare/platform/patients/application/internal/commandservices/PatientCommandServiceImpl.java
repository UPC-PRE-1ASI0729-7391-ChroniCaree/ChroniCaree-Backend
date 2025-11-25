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
}
