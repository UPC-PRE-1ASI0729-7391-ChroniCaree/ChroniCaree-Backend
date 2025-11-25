/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.chronicare.platform.doctors.infrastructure.persistence;

import com.chronicare.platform.doctors.domain.model.Doctor;
import com.chronicare.platform.doctors.domain.repository.DoctorRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Implementación JPA del repositorio de Doctor.
 */
@Repository
public interface JpaDoctorRepository extends JpaRepository<Doctor, Long>, DoctorRepository {

    @Override
    Optional<Doctor> findByDni(String dni);

    @Override
    Optional<Doctor> findByLicenseNumber(String licenseNumber);

    @Override
    List<Doctor> findBySpecialty(String specialty);
}
