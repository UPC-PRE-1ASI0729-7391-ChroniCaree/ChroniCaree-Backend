/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.alerts.application.services;

import com.example.alerts.domain.model.Alert;
import com.example.alerts.domain.repository.AlertRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AlertService {

    private final AlertRepository alertRepository;

    public AlertService(AlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    public List<Alert> getAllAlerts() {
        return alertRepository.findAll();
    }

    public Optional<Alert> getAlertById(String id) {
        return alertRepository.findById(id);
    }

    public List<Alert> getAlertsByPatientId(String patientId) {
        return alertRepository.findByPatientId(patientId);
    }

    public Alert createAlert(Alert alert) {
        return alertRepository.save(alert);
    }

    public Alert updateAlert(String id, Alert updatedAlert) {
        updatedAlert.setId(id);
        return alertRepository.save(updatedAlert);
    }

    public void deleteAlert(String id) {
        alertRepository.deleteById(id);
    }
}
