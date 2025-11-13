/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.patients.domain.repository;

import com.example.patients.domain.model.Patient;
import java.util.List;
import java.util.Optional;

public interface PatientRepository {

    List<Patient> findAll();

    Optional<Patient> findById(Long id);

    Optional<Patient> findByDni(String dni);

    Patient save(Patient patient);

    void deleteById(Long id);
}
