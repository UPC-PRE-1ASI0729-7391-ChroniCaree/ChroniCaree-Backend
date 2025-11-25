/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.chronicare.platform.patients.domain.repository;

import com.chronicare.platform.patients.domain.aggregates.Patient;
import com.chronicare.platform.patients.domain.valueobjects.Dni;

import java.util.List;
import java.util.Optional;

public interface PatientRepository {

    List<Patient> findAll();

    Optional<Patient> findById(Long id);

    Optional<Patient> findByDni(Dni dni);

    Patient save(Patient patient);

    void deleteById(Long id);
}
