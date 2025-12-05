package com.chronicare.platform.doctors.infrastructure.persistence.jpa.repositories;

import com.chronicare.platform.doctors.domain.model.aggregates.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findByUserId(Long userId);
    Optional<Doctor> findByDni_Value(String dni);
    Optional<Doctor> findByLicenseNumber_Value(String licenseNumber);
    List<Doctor> findByTenantId(Long tenantId);
    List<Doctor> findBySpecialty_Value(String specialty);
    List<Doctor> findByIsVerifiedTrue();
    List<Doctor> findByAcceptingPatientsTrue();
    long countByTenantId(Long tenantId);
}
