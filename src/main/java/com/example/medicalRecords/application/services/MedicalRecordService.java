/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.medicalRecords.application.services;

import com.example.medicalRecords.domain.model.MedicalRecord;
import com.example.medicalRecords.domain.repository.MedicalRecordRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

 
@Service
@Transactional
public class MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;

    public MedicalRecordService(MedicalRecordRepository medicalRecordRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
    }

    public List<MedicalRecord> getAllRecords() {
        return medicalRecordRepository.findAll();
    }

    public Optional<MedicalRecord> getRecordById(Long id) {
        return medicalRecordRepository.findById(id);
    }

    public List<MedicalRecord> getRecordsByPatientId(Long patientId) {
        return medicalRecordRepository.findByPatientId(patientId);
    }

    public List<MedicalRecord> getRecordsByDoctorId(Long doctorId) {
        return medicalRecordRepository.findByDoctorId(doctorId);
    }

    public MedicalRecord createRecord(MedicalRecord record) {
        return medicalRecordRepository.save(record);
    }

    public MedicalRecord updateRecord(Long id, MedicalRecord updatedRecord) {
        return medicalRecordRepository.findById(id)
                .map(existing -> {
                    existing.setType(updatedRecord.getType());
                    existing.setDate(updatedRecord.getDate());
                    existing.setGlucose(updatedRecord.getGlucose());
                    existing.setBloodPressure(updatedRecord.getBloodPressure());
                    existing.setHeartRate(updatedRecord.getHeartRate());
                    existing.setTemperature(updatedRecord.getTemperature());
                    existing.setWeight(updatedRecord.getWeight());
                    existing.setFatigue(updatedRecord.getFatigue());
                    existing.setPain(updatedRecord.getPain());
                    existing.setDizziness(updatedRecord.getDizziness());
                    existing.setDiagnosis(updatedRecord.getDiagnosis());
                    existing.setTreatment(updatedRecord.getTreatment());
                    existing.setNotes(updatedRecord.getNotes());
                    existing.setPatientName(updatedRecord.getPatientName());
                    existing.setReviewStatus(updatedRecord.getReviewStatus());
                    existing.setReviewedAt(updatedRecord.getReviewedAt());
                    existing.setReviewedBy(updatedRecord.getReviewedBy());
                    return medicalRecordRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("MedicalRecord not found with id: " + id));
    }

    public void deleteRecord(Long id) {
        medicalRecordRepository.deleteById(id);
    }
}
