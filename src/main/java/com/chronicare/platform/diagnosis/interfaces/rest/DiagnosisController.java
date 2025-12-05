package com.chronicare.platform.diagnosis.interfaces.rest;

import com.chronicare.platform.diagnosis.domain.model.commands.DeleteDiagnosisCommand;
import com.chronicare.platform.diagnosis.domain.model.queries.*;
import com.chronicare.platform.diagnosis.domain.model.valueobjects.DiagnosisStatus;
import com.chronicare.platform.diagnosis.domain.services.DiagnosisCommandService;
import com.chronicare.platform.diagnosis.domain.services.DiagnosisQueryService;
import com.chronicare.platform.diagnosis.interfaces.rest.resources.CreateDiagnosisResource;
import com.chronicare.platform.diagnosis.interfaces.rest.resources.DiagnosisResource;
import com.chronicare.platform.diagnosis.interfaces.rest.resources.UpdateDiagnosisResource;
import com.chronicare.platform.diagnosis.interfaces.rest.transform.CreateDiagnosisCommandFromResourceAssembler;
import com.chronicare.platform.diagnosis.interfaces.rest.transform.DiagnosisResourceFromEntityAssembler;
import com.chronicare.platform.diagnosis.interfaces.rest.transform.UpdateDiagnosisCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Diagnosis management
 */
@RestController
@RequestMapping(value = "/api/v1/diagnoses", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Diagnoses", description = "Diagnosis Management Endpoints")
public class DiagnosisController {

    private final DiagnosisCommandService diagnosisCommandService;
    private final DiagnosisQueryService diagnosisQueryService;

    public DiagnosisController(
            DiagnosisCommandService diagnosisCommandService,
            DiagnosisQueryService diagnosisQueryService) {
        this.diagnosisCommandService = diagnosisCommandService;
        this.diagnosisQueryService = diagnosisQueryService;
    }

    @GetMapping
    @Operation(summary = "Get all diagnoses")
    public ResponseEntity<List<DiagnosisResource>> getAllDiagnoses() {
        var query = new GetAllDiagnosesQuery();
        var diagnoses = diagnosisQueryService.handle(query);
        var resources = diagnoses.stream()
                .map(DiagnosisResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get diagnosis by ID")
    public ResponseEntity<DiagnosisResource> getDiagnosisById(@PathVariable Long id) {
        var query = new GetDiagnosisByIdQuery(id);
        var diagnosis = diagnosisQueryService.handle(query);
        return diagnosis
                .map(d -> ResponseEntity.ok(DiagnosisResourceFromEntityAssembler.toResourceFromEntity(d)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/patient/{patientId}")
    @Operation(summary = "Get diagnoses by patient ID")
    public ResponseEntity<List<DiagnosisResource>> getDiagnosesByPatient(@PathVariable Long patientId) {
        var query = new GetDiagnosesByPatientIdQuery(patientId);
        var diagnoses = diagnosisQueryService.handle(query);
        var resources = diagnoses.stream()
                .map(DiagnosisResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/doctor/{doctorId}")
    @Operation(summary = "Get diagnoses by doctor ID")
    public ResponseEntity<List<DiagnosisResource>> getDiagnosesByDoctor(@PathVariable Long doctorId) {
        var query = new GetDiagnosesByDoctorIdQuery(doctorId);
        var diagnoses = diagnosisQueryService.handle(query);
        var resources = diagnoses.stream()
                .map(DiagnosisResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get diagnoses by status")
    public ResponseEntity<List<DiagnosisResource>> getDiagnosesByStatus(@PathVariable DiagnosisStatus status) {
        var query = new GetDiagnosesByStatusQuery(status);
        var diagnoses = diagnosisQueryService.handle(query);
        var resources = diagnoses.stream()
                .map(DiagnosisResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(resources);
    }

    @PostMapping
    @Operation(summary = "Create a new diagnosis")
    public ResponseEntity<DiagnosisResource> createDiagnosis(@RequestBody CreateDiagnosisResource resource) {
        var command = CreateDiagnosisCommandFromResourceAssembler.toCommandFromResource(resource);
        var diagnosis = diagnosisCommandService.handle(command);
        return diagnosis
                .map(d -> new ResponseEntity<>(DiagnosisResourceFromEntityAssembler.toResourceFromEntity(d), HttpStatus.CREATED))
                .orElse(ResponseEntity.badRequest().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a diagnosis")
    public ResponseEntity<DiagnosisResource> updateDiagnosis(
            @PathVariable Long id,
            @RequestBody UpdateDiagnosisResource resource) {
        var command = UpdateDiagnosisCommandFromResourceAssembler.toCommandFromResource(id, resource);
        var diagnosis = diagnosisCommandService.handle(command);
        return diagnosis
                .map(d -> ResponseEntity.ok(DiagnosisResourceFromEntityAssembler.toResourceFromEntity(d)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a diagnosis")
    public ResponseEntity<Void> deleteDiagnosis(@PathVariable Long id) {
        var command = new DeleteDiagnosisCommand(id);
        diagnosisCommandService.handle(command);
        return ResponseEntity.noContent().build();
    }
}
