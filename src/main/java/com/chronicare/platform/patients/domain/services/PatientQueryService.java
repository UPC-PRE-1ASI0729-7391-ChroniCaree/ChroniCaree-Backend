package com.chronicare.platform.patients.domain.services;

import com.chronicare.platform.patients.domain.model.aggregates.Patient;
import com.chronicare.platform.patients.domain.model.aggregates.PatientDashboard;
import com.chronicare.platform.patients.domain.model.queries.GetPatientDashboardQuery;
import com.chronicare.platform.patients.domain.queries.GetAllPatientsQuery;
import com.chronicare.platform.patients.domain.queries.GetPatientByIdQuery;
import com.chronicare.platform.patients.domain.queries.GetPatientByUserIdQuery;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface PatientQueryService {
    List<Patient> handle(GetAllPatientsQuery query);
    Optional<Patient> handle(GetPatientByIdQuery query);
    Optional<Patient> handle(GetPatientByUserIdQuery query);
    Page<Patient> handleByTenantId(Long tenantId, int page, int limit);
    Optional<PatientDashboard> handle(GetPatientDashboardQuery query);
}
