package com.chronicare.platform.diagnosis.domain.services;

import com.chronicare.platform.diagnosis.domain.model.aggregates.Diagnosis;
import com.chronicare.platform.diagnosis.domain.model.commands.CreateDiagnosisCommand;
import com.chronicare.platform.diagnosis.domain.model.commands.DeleteDiagnosisCommand;
import com.chronicare.platform.diagnosis.domain.model.commands.UpdateDiagnosisCommand;

import java.util.Optional;

/**
 * Diagnosis Command Service Interface
 * Handles commands for diagnosis operations
 */
public interface DiagnosisCommandService {
    Optional<Diagnosis> handle(CreateDiagnosisCommand command);
    Optional<Diagnosis> handle(UpdateDiagnosisCommand command);
    void handle(DeleteDiagnosisCommand command);
}
