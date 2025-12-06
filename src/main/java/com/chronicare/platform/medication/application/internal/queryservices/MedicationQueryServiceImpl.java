package com.chronicare.platform.medication.application.internal.queryservices;

import com.chronicare.platform.medication.domain.model.aggregates.Medication;
import com.chronicare.platform.medication.domain.model.queries.*;
import com.chronicare.platform.medication.domain.services.MedicationQueryService;
import com.chronicare.platform.medication.infrastructure.persistence.jpa.repositories.MedicationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * MedicationQueryService Implementation
 *
 * @summary
 * Provides read-only query operations for retrieving Medication aggregates.
 * Business rules enforced:
 * - Queries run in read-only mode to ensure consistency.
 * - Supports retrieval of medications by ID, patient ID, and complete listing.
 * - Respects CQRS separation by exposing only read-side operations.
 *
 */

@Service
@Transactional(readOnly = true)
public class MedicationQueryServiceImpl implements MedicationQueryService {

    private final MedicationRepository medicationRepository;

    public MedicationQueryServiceImpl(MedicationRepository medicationRepository) {
        this.medicationRepository = medicationRepository;
    }

    @Override
    public List<Medication> handle(GetAllMedicationsQuery query) {
        return medicationRepository.findAll();
    }

    @Override
    public Optional<Medication> handle(GetMedicationByIdQuery query) {
        return medicationRepository.findById(query.id());
    }

    @Override
    public List<Medication> handle(GetMedicationsByPatientIdQuery query) {
        return medicationRepository.findByPatientId(query.patientId());
    }
}
