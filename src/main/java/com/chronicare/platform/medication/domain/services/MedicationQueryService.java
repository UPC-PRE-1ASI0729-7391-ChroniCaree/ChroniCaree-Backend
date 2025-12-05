package com.chronicare.platform.medication.domain.services;

import com.chronicare.platform.medication.domain.model.aggregates.Medication;
import com.chronicare.platform.medication.domain.model.queries.*;

import java.util.List;
import java.util.Optional;

/**
 * Medication Query Service Interface
 * Handles queries for medication operations
 */
public interface MedicationQueryService {
    List<Medication> handle(GetAllMedicationsQuery query);
    Optional<Medication> handle(GetMedicationByIdQuery query);
    List<Medication> handle(GetMedicationsByPatientIdQuery query);
}
