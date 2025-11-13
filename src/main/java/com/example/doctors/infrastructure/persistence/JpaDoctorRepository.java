/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.doctors.infrastructure.persistence;

import com.example.doctors.domain.model.Doctor;
import com.example.doctors.domain.repository.DoctorRepository;
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
