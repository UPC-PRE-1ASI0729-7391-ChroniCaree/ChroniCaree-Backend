package com.chronicare.platform.diagnosis.domain.services;

import com.chronicare.platform.diagnosis.domain.model.aggregates.Diagnosis;
import com.chronicare.platform.diagnosis.domain.model.queries.*;

import java.util.List;
import java.util.Optional;

/**
 * Diagnosis Query Service Interface
 * Handles queries for diagnosis operations
 */
public interface DiagnosisQueryService {
    List<Diagnosis> handle(GetAllDiagnosesQuery query);
    Optional<Diagnosis> handle(GetDiagnosisByIdQuery query);
    List<Diagnosis> handle(GetDiagnosesByPatientIdQuery query);
    List<Diagnosis> handle(GetDiagnosesByDoctorIdQuery query);
    List<Diagnosis> handle(GetDiagnosesByStatusQuery query);
}
