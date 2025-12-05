/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.chronicare.platform.patients.domain.repository;

import com.chronicare.platform.patients.domain.model.aggregates.Patient;
import com.chronicare.platform.patients.domain.valueobjects.Dni;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByDni(Dni dni);
    boolean existsByDni(Dni dni);
    Optional<Patient> findByUserId(Long userId);
    long countByTenantId(Long tenantId);
    List<Patient> findByTenantId(Long tenantId);
    Page<Patient> findByTenantId(Long tenantId, Pageable pageable);
}
