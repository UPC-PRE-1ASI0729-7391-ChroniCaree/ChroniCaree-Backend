package com.chronicare.platform.records.interfaces.rest.controller;

import com.chronicare.platform.records.application.internal.commandservices.RecordExportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/records/export")
@RequiredArgsConstructor
@Tag(name = "Record Export", description = "Medical Record Export API")
public class RecordExportController {

    private final RecordExportService exportService;

    @GetMapping("/patients/{patientId}")
    @Operation(summary = "Export all patient records in JSON or FHIR format")
    public ResponseEntity<String> exportPatientRecords(
            @PathVariable Long patientId,
            @RequestParam(defaultValue = "json") String format,
            Authentication authentication) {

        String exportData;
        String filename;

        if ("fhir".equalsIgnoreCase(format)) {
            exportData = exportService.exportPatientRecordsAsFhir(patientId);
            filename = "patient-" + patientId + "-records-fhir.json";
        } else {
            exportData = exportService.exportPatientRecordsAsJson(patientId);
            filename = "patient-" + patientId + "-records.json";
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setContentDispositionFormData("attachment", filename);

        return ResponseEntity.ok()
                .headers(headers)
                .body(exportData);
    }
}
