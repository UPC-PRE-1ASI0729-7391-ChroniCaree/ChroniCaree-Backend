package com.chronicare.platform.medicalRecords.application.internal.queryservices;

import com.chronicare.platform.medicalRecords.domain.model.aggregates.MedicalRecord;
import com.chronicare.platform.medicalRecords.domain.model.queries.GetAllMedicalRecordsQuery;
import com.chronicare.platform.medicalRecords.domain.model.queries.GetMedicalRecordByIdQuery;
import com.chronicare.platform.medicalRecords.domain.model.queries.GetMedicalRecordsByDoctorIdQuery;
import com.chronicare.platform.medicalRecords.domain.model.queries.GetMedicalRecordsByPatientIdQuery;
import com.chronicare.platform.medicalRecords.domain.repository.MedicalRecordRepository;
import com.chronicare.platform.medicalRecords.domain.services.MedicalRecordQueryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * MedicalRecordQueryService Implementation
 *
 * @summary
 * Handles Medical Record query operations and provides read-optimized access
 * to MedicalRecord aggregates.
 * Business rules enforced:
 * - Queries operate in read-only mode to ensure data integrity.
 * - Supports retrieval of medical records by ID, patient ID, doctor ID, and full listing.
 * - Ensures query-side separation aligned with CQRS principles.
 *
 */


@Service
public class MedicalRecordQueryServiceImpl implements MedicalRecordQueryService {
    private final MedicalRecordRepository medicalRecordRepository;

    public MedicalRecordQueryServiceImpl(MedicalRecordRepository medicalRecordRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicalRecord> handle(GetAllMedicalRecordsQuery query) {
        return medicalRecordRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MedicalRecord> handle(GetMedicalRecordByIdQuery query) {
        return medicalRecordRepository.findById(query.medicalRecordId());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicalRecord> handle(GetMedicalRecordsByPatientIdQuery query) {
        return medicalRecordRepository.findByPatientId(query.patientId());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicalRecord> handle(GetMedicalRecordsByDoctorIdQuery query) {
        return medicalRecordRepository.findByDoctorId(query.doctorId());
    }
}

