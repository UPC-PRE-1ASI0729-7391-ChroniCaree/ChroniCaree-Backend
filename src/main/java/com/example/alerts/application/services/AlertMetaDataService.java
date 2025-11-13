/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.alerts.application.services;

import com.example.alerts.domain.model.AlertMetaData;
import com.example.alerts.domain.repository.AlertMetaDataRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AlertMetaDataService {

    private final AlertMetaDataRepository alertMetaDataRepository;

    public AlertMetaDataService(AlertMetaDataRepository alertMetadataRepository) {
        this.alertMetaDataRepository = alertMetadataRepository;
    }

    public List<AlertMetaData> getAllAlertMetadata() {
        return alertMetaDataRepository.findAll();
    }

    public Optional<AlertMetaData> getAlertMetadataById(Long id) {
        return alertMetaDataRepository.findById(id);
    }

    public List<AlertMetaData> getAlertMetadataByVitalSign(String vitalSign) {
        return alertMetaDataRepository.findByVitalSign(vitalSign);
    }

    public AlertMetaData createAlertMetadata(AlertMetaData alertMetadata) {
        return alertMetaDataRepository.save(alertMetadata);
    }

    public AlertMetaData updateAlertMetadata(Long id, AlertMetaData updatedMetadata) {
        return alertMetaDataRepository.findById(id)
                .map(existing -> {
                    existing.setVitalSign(updatedMetadata.getVitalSign());
                    existing.setValue(updatedMetadata.getValue());
                    existing.setUnit(updatedMetadata.getUnit());
                    existing.setThreshold(updatedMetadata.getThreshold());
                    existing.setMeasurement(updatedMetadata.getMeasurement());
                    existing.setMedicationId(updatedMetadata.getMedicationId());
                    existing.setSymptomId(updatedMetadata.getSymptomId());
                    existing.setAppointmentId(updatedMetadata.getAppointmentId());
                    existing.setAdditionalData(updatedMetadata.getAdditionalData());
                    return alertMetaDataRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("AlertMetadata not found with id: " + id));
    }

    public void deleteAlertMetadata(Long id) {
        alertMetaDataRepository.deleteById(id);
    }
}
