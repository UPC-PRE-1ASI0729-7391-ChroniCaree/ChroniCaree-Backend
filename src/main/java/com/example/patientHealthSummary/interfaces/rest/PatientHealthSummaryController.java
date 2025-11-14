/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.patientHealthSummary.interfaces.rest;

import com.example.patientHealthSummary.application.services.PatientHealthSummaryService;
import com.example.patientHealthSummary.domain.model.PatientHealthSummary;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/patient-health-summaries")
@Tag(name = "Patient Health Summaries", description = "API para gestionar resúmenes de pacientes")
public class PatientHealthSummaryController {

    private final PatientHealthSummaryService service;

    public PatientHealthSummaryController(PatientHealthSummaryService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Listar todos los resúmenes de pacientes")
    public ResponseEntity<List<PatientHealthSummary>> getAllSummaries() {
        return ResponseEntity.ok(service.getAllSummaries());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener resumen de paciente por ID")
    public ResponseEntity<PatientHealthSummary> getSummaryById(@PathVariable Long id) {
        Optional<PatientHealthSummary> oPatientHealthSummary = service.getSummaryById(id);
        if (oPatientHealthSummary.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(oPatientHealthSummary.get());
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Obtener resumen de paciente por User ID")
    public ResponseEntity<PatientHealthSummary> getSummaryByUserId(@PathVariable Long userId) {
        PatientHealthSummary summary = service.getSummaryByUserId(userId);
        if (summary == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/doctor/{doctorId}")
    @Operation(summary = "Obtener resúmenes de pacientes asignados a un doctor")
    public ResponseEntity<List<PatientHealthSummary>> getSummariesByDoctorId(@PathVariable Long doctorId) {
        List<PatientHealthSummary> summaries = service.getSummariesByDoctorId(doctorId);
        return ResponseEntity.ok(summaries);
    }

    @PostMapping
    @Operation(summary = "Crear nuevo resumen de paciente")
    public ResponseEntity<PatientHealthSummary> createSummary(@RequestBody PatientHealthSummary summary) {
        PatientHealthSummary created = service.createSummary(summary);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar resumen de paciente por ID")
    public ResponseEntity<PatientHealthSummary> updateSummary(@PathVariable Long id, @RequestBody PatientHealthSummary summary) {
        Optional<PatientHealthSummary> oPatientHealthSummary = service.getSummaryById(id);
        if (oPatientHealthSummary.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(service.updateSummary(id, summary));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar resumen de paciente por ID")
    public ResponseEntity<Void> deleteSummary(@PathVariable Long id) {
        Optional<PatientHealthSummary> oPatientHealthSummary = service.getSummaryById(id);
        if (oPatientHealthSummary.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        service.deleteSummary(id);
        return ResponseEntity.noContent().build();
    }
}
