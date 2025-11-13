package com.example.doctors.domain.repository;

import com.example.doctors.domain.model.Doctor;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz del repositorio de dominio para la entidad Doctor.
 */
public interface DoctorRepository {

    List<Doctor> findAll();

    Optional<Doctor> findById(Long id);

    Optional<Doctor> findByDni(String dni);

    Optional<Doctor> findByLicenseNumber(String licenseNumber);

    List<Doctor> findBySpecialty(String specialty);

    Doctor save(Doctor doctor);

    void deleteById(Long id);
}
