package com.chronicare.platform.patients.domain.services;

import com.chronicare.platform.patients.domain.model.aggregates.Patient;
import com.chronicare.platform.patients.domain.queries.GetAllPatientsQuery;
import com.chronicare.platform.patients.domain.queries.GetPatientByIdQuery;

import java.util.List;
import java.util.Optional;

public interface PatientQueryService {
    List<Patient> handle(GetAllPatientsQuery query);
    Optional<Patient> handle(GetPatientByIdQuery query);
}
