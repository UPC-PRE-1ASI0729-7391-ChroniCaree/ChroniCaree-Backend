/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.chronicare.platform.patients.infrastructure.persistence;

import com.chronicare.platform.patients.domain.aggregates.Patient;
import com.chronicare.platform.patients.domain.valueobjects.Dni;
import com.chronicare.platform.patients.domain.repository.PatientRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaPatientRepository extends JpaRepository<Patient, Long>, PatientRepository {

    @Override
    Optional<Patient> findByDni(Dni dni);
}
