/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.diagnosis.interfaces.rest;

import com.example.diagnosis.application.services.DiagnosisService;
import com.example.diagnosis.domain.model.Diagnosis;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/diagnoses")
@Tag(name = "Diagnoses", description = "Diagnosis management API")
public class DiagnosisController {

    private final DiagnosisService diagnosisService;

    public DiagnosisController(DiagnosisService diagnosisService) {
        this.diagnosisService = diagnosisService;
    }

    @GetMapping
    @Operation(summary = "List all diagnoses")
    public ResponseEntity<List<Diagnosis>> getAllDiagnoses() {
        return ResponseEntity.ok(diagnosisService.getAllDiagnoses());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get diagnosis by ID")
    public ResponseEntity<Diagnosis> getDiagnosisById(@PathVariable Long id) {
        Optional<Diagnosis> oDiagnosis = diagnosisService.getDiagnosisById(id);

        if (oDiagnosis.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(oDiagnosis.get());
    }

    @GetMapping("/patient/{patientId}")
    @Operation(summary = "Get diagnoses by patient ID")
    public ResponseEntity<List<Diagnosis>> getByPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(diagnosisService.getDiagnosesByPatient(patientId));
    }

    @GetMapping("/doctor/{doctorId}")
    @Operation(summary = "Get diagnoses by doctor ID")
    public ResponseEntity<List<Diagnosis>> getByDoctor(@PathVariable Long doctorId) {
        return ResponseEntity.ok(diagnosisService.getDiagnosesByDoctor(doctorId));
    }

    @PostMapping
    @Operation(summary = "Create new diagnosis")
    public ResponseEntity<Diagnosis> createDiagnosis(@RequestBody Diagnosis diagnosis) {
        Diagnosis created = diagnosisService.createDiagnosis(diagnosis);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update diagnosis by ID")
    public ResponseEntity<Diagnosis> updateDiagnosis(@PathVariable Long id, @RequestBody Diagnosis diagnosis) {
        Optional<Diagnosis> oDiagnosis = diagnosisService.getDiagnosisById(id);

        if (oDiagnosis.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(diagnosisService.updateDiagnosis(id, diagnosis));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete diagnosis by ID")
    public ResponseEntity<?> deleteDiagnosis(@PathVariable Long id) {
        Optional<Diagnosis> oDiagnosis = diagnosisService.getDiagnosisById(id);

        if (oDiagnosis.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        diagnosisService.deleteDiagnosis(id);
        return ResponseEntity.noContent().build();
    }
}
