/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.alerts.domain.repository;

import com.example.alerts.domain.model.Alert;
import java.util.List;
import java.util.Optional;

public interface AlertRepository {

    List<Alert> findAll();

    Optional<Alert> findById(Long id);

    List<Alert> findByPatientId(String patientId);

    Alert save(Alert alert);

    void deleteById(Long id);
}
