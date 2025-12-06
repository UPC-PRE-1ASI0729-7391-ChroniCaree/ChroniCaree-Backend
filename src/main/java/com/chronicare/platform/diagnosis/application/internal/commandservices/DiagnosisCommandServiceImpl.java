package com.chronicare.platform.diagnosis.application.internal.commandservices;

import com.chronicare.platform.diagnosis.domain.model.aggregates.Diagnosis;
import com.chronicare.platform.diagnosis.domain.model.commands.CreateDiagnosisCommand;
import com.chronicare.platform.diagnosis.domain.model.commands.DeleteDiagnosisCommand;
import com.chronicare.platform.diagnosis.domain.model.commands.UpdateDiagnosisCommand;
import com.chronicare.platform.diagnosis.domain.services.DiagnosisCommandService;
import com.chronicare.platform.diagnosis.infrastructure.persistence.jpa.repositories.DiagnosisRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Summary: Implementation of DiagnosisCommandService
 * Handles command operations for diagnoses
 */
@Service
@Transactional
public class DiagnosisCommandServiceImpl implements DiagnosisCommandService {

    private final DiagnosisRepository diagnosisRepository;

    public DiagnosisCommandServiceImpl(DiagnosisRepository diagnosisRepository) {
        this.diagnosisRepository = diagnosisRepository;
    }

    @Override
    public Optional<Diagnosis> handle(CreateDiagnosisCommand command) {
        var diagnosis = new Diagnosis(command);
        var savedDiagnosis = diagnosisRepository.save(diagnosis);
        return Optional.of(savedDiagnosis);
    }

    @Override
    public Optional<Diagnosis> handle(UpdateDiagnosisCommand command) {
        var existingDiagnosis = diagnosisRepository.findById(command.id());
        if (existingDiagnosis.isEmpty()) {
            return Optional.empty();
        }

        var diagnosis = existingDiagnosis.get();
        diagnosis.updateDiagnosis(
                command.icd10Code(),
                command.diagnosisName(),
                command.status(),
                command.severity(),
                command.resolvedDate(),
                command.notes(),
                command.treatment(),
                command.followUpRequired(),
                command.lastReviewDate()
        );

        var updatedDiagnosis = diagnosisRepository.save(diagnosis);
        return Optional.of(updatedDiagnosis);
    }

    @Override
    public void handle(DeleteDiagnosisCommand command) {
        diagnosisRepository.deleteById(command.id());
    }
}
