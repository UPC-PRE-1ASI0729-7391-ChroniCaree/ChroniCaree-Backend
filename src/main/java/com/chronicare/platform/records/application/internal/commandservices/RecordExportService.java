package com.chronicare.platform.records.application.internal.commandservices;

import com.chronicare.platform.records.domain.model.aggregates.MedicalRecord;
import com.chronicare.platform.records.infrastructure.persistence.jpa.repositories.MedicalRecordRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RecordExportService {

    private final MedicalRecordRepository medicalRecordRepository;
    private final ObjectMapper objectMapper;

    @Transactional(readOnly = true)
    public String exportPatientRecordsAsJson(Long patientId) {
        List<MedicalRecord> records = medicalRecordRepository.findByPatientIdAndIsDeletedFalse(patientId, null).getContent();

        Map<String, Object> export = new HashMap<>();
        export.put("patientId", patientId);
        export.put("exportDate", java.time.LocalDateTime.now());
        export.put("recordCount", records.size());
        export.put("records", records);

        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(export);
        } catch (Exception e) {
            throw new RuntimeException("Failed to export records as JSON", e);
        }
    }

    @Transactional(readOnly = true)
    public String exportPatientRecordsAsFhir(Long patientId) {
        List<MedicalRecord> records = medicalRecordRepository.findByPatientIdAndIsDeletedFalse(patientId, null).getContent();

        // Simplified FHIR-like format
        Map<String, Object> bundle = new HashMap<>();
        bundle.put("resourceType", "Bundle");
        bundle.put("type", "collection");
        bundle.put("total", records.size());

        List<Map<String, Object>> entries = records.stream()
                .map(this::convertToFhirResource)
                .toList();

        bundle.put("entry", entries);

        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(bundle);
        } catch (Exception e) {
            throw new RuntimeException("Failed to export records as FHIR", e);
        }
    }

    private Map<String, Object> convertToFhirResource(MedicalRecord record) {
        Map<String, Object> resource = new HashMap<>();
        resource.put("resourceType", "DocumentReference");
        resource.put("id", record.getId());
        resource.put("status", "current");
        resource.put("type", Map.of("text", record.getType().name()));
        resource.put("subject", Map.of("reference", "Patient/" + record.getPatientId()));
        resource.put("author", List.of(Map.of("reference", "Practitioner/" + record.getAuthorId())));
        resource.put("date", record.getCreatedAt());

        Map<String, Object> content = new HashMap<>();
        content.put("attachment", Map.of(
                "contentType", "text/plain",
                "data", record.getContent()
        ));
        resource.put("content", List.of(content));

        return Map.of("resource", resource);
    }
}
