/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.alerts.infrastructure.persistence;

import com.example.alerts.domain.model.Alert;
import com.example.alerts.domain.repository.AlertRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface JpaAlertRepository extends JpaRepository<Alert, String>, AlertRepository {
    
    @Override
    List<Alert> findByPatientId(String patientId);

}
