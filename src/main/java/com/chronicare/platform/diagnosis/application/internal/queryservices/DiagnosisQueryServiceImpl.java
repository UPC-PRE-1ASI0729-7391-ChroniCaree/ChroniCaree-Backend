package com.chronicare.platform.diagnosis.application.internal.queryservices;

import com.chronicare.platform.diagnosis.domain.model.aggregates.Diagnosis;
import com.chronicare.platform.diagnosis.domain.model.queries.*;
import com.chronicare.platform.diagnosis.domain.services.DiagnosisQueryService;
import com.chronicare.platform.diagnosis.infrastructure.persistence.jpa.repositories.DiagnosisRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Summary: Implementation of DiagnosisQueryService
 * Handles query operations for diagnoses
 */
@Service
@Transactional(readOnly = true)
public class DiagnosisQueryServiceImpl implements DiagnosisQueryService {

    private final DiagnosisRepository diagnosisRepository;

    public DiagnosisQueryServiceImpl(DiagnosisRepository diagnosisRepository) {
        this.diagnosisRepository = diagnosisRepository;
    }

    @Override
    public List<Diagnosis> handle(GetAllDiagnosesQuery query) {
        return diagnosisRepository.findAll();
    }

    @Override
    public Optional<Diagnosis> handle(GetDiagnosisByIdQuery query) {
        return diagnosisRepository.findById(query.id());
    }

    @Override
    public List<Diagnosis> handle(GetDiagnosesByPatientIdQuery query) {
        return diagnosisRepository.findByPatientId(query.patientId());
    }

    @Override
    public List<Diagnosis> handle(GetDiagnosesByDoctorIdQuery query) {
        return diagnosisRepository.findByDoctorId(query.doctorId());
    }

    @Override
    public List<Diagnosis> handle(GetDiagnosesByStatusQuery query) {
        return diagnosisRepository.findByStatus(query.status());
    }
}
