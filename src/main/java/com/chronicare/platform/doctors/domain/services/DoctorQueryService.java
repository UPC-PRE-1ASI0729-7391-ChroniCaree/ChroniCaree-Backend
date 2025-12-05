package com.chronicare.platform.doctors.domain.services;

import com.chronicare.platform.doctors.domain.model.aggregates.Doctor;
import com.chronicare.platform.doctors.domain.model.aggregates.DoctorDashboard;
import com.chronicare.platform.doctors.domain.model.queries.*;
import com.chronicare.platform.patients.domain.model.aggregates.Patient;

import java.util.List;
import java.util.Optional;

/**
 * Doctor Query Service
 * Handles doctor-related queries
 */
public interface DoctorQueryService {
    Optional<DoctorDashboard> handle(GetDoctorDashboardQuery query);
    List<Doctor> handle(GetAllDoctorsQuery query);
    Optional<Doctor> handle(GetDoctorByIdQuery query);
    Optional<Doctor> handle(GetDoctorByUserIdQuery query);
    List<Doctor> handle(GetDoctorsByTenantQuery query);
    List<Doctor> handle(GetDoctorsBySpecialtyQuery query);
    List<Patient> handle(GetAssignedPatientsQuery query);
}
