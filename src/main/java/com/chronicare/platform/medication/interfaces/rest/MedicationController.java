package com.chronicare.platform.medication.interfaces.rest;

import com.chronicare.platform.medication.domain.model.commands.DeleteMedicationCommand;
import com.chronicare.platform.medication.domain.model.queries.*;
import com.chronicare.platform.medication.domain.services.MedicationCommandService;
import com.chronicare.platform.medication.domain.services.MedicationQueryService;
import com.chronicare.platform.medication.interfaces.rest.resources.CreateMedicationResource;
import com.chronicare.platform.medication.interfaces.rest.resources.MedicationResource;
import com.chronicare.platform.medication.interfaces.rest.resources.UpdateMedicationResource;
import com.chronicare.platform.medication.interfaces.rest.transform.CreateMedicationCommandFromResourceAssembler;
import com.chronicare.platform.medication.interfaces.rest.transform.MedicationResourceFromEntityAssembler;
import com.chronicare.platform.medication.interfaces.rest.transform.UpdateMedicationCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Medication management
 */
@RestController
@RequestMapping(value = "/api/v1/medications", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Medications", description = "Medication Management Endpoints")
public class MedicationController {

    private final MedicationCommandService medicationCommandService;
    private final MedicationQueryService medicationQueryService;

    public MedicationController(
            MedicationCommandService medicationCommandService,
            MedicationQueryService medicationQueryService) {
        this.medicationCommandService = medicationCommandService;
        this.medicationQueryService = medicationQueryService;
    }

    @GetMapping
    @Operation(summary = "Get all medications (optional filter by patientId)")
        @org.springframework.security.access.prepost.PreAuthorize("hasAnyRole('DOCTOR', 'NURSE', 'HOSPITAL_ADMIN', 'SYSTEM', 'PATIENT', 'TENANT_ADMIN')")
        public ResponseEntity<List<MedicationResource>> getAllMedications(
            @RequestParam(required = false) String patientId,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer limit) {
        if (patientId != null && !patientId.isBlank()) {
            var query = new GetMedicationsByPatientIdQuery(patientId);
            var medications = medicationQueryService.handle(query);
            // Optional server-side filtering by status
            if (status != null && !status.isBlank()) {
            medications = medications.stream()
                .filter(m -> m.getStatus() != null && status.equalsIgnoreCase(m.getStatus().name()))
                .toList();
            }
                // Simple controller-level pagination (fallback)
                int fromIndex = Math.max(0, page * limit);
                int toIndex = Math.min(medications.size(), fromIndex + limit);
                var pageContent = medications.subList(fromIndex, toIndex);
                var resources = pageContent.stream()
                    .map(MedicationResourceFromEntityAssembler::toResourceFromEntity)
                    .toList();
            return ResponseEntity.ok(resources);
        }

        var query = new GetAllMedicationsQuery();
        var medications = medicationQueryService.handle(query);
        var resources = medications.stream()
                .map(MedicationResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get medication by ID")
    public ResponseEntity<MedicationResource> getMedicationById(@PathVariable Long id) {
        var query = new GetMedicationByIdQuery(id);
        var medication = medicationQueryService.handle(query);
        return medication
                .map(m -> ResponseEntity.ok(MedicationResourceFromEntityAssembler.toResourceFromEntity(m)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/patient/{patientId}")
    @Operation(summary = "Get medications by patient ID")
    @org.springframework.security.access.prepost.PreAuthorize("hasAnyRole('DOCTOR', 'NURSE', 'HOSPITAL_ADMIN', 'SYSTEM', 'PATIENT', 'TENANT_ADMIN')")
    public ResponseEntity<List<MedicationResource>> getMedicationsByPatient(@PathVariable String patientId,
                                                                           @RequestParam(required = false) String status,
                                                                           @RequestParam(defaultValue = "0") Integer page,
                                                                           @RequestParam(defaultValue = "20") Integer limit) {
        var query = new GetMedicationsByPatientIdQuery(patientId);
        var medications = medicationQueryService.handle(query);
        // Optional status filter
        if (status != null && !status.isBlank()) {
            medications = medications.stream()
                    .filter(m -> m.getStatus() != null && status.equalsIgnoreCase(m.getStatus().name()))
                    .toList();
        }
        int fromIndex = Math.max(0, page * limit);
        int toIndex = Math.min(medications.size(), fromIndex + limit);
        var pageContent = medications.subList(fromIndex, toIndex);
        var resources = pageContent.stream()
                .map(MedicationResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(resources);
    }

    @PostMapping
    @Operation(summary = "Create a new medication")
    public ResponseEntity<MedicationResource> createMedication(@RequestBody CreateMedicationResource resource) {
        var command = CreateMedicationCommandFromResourceAssembler.toCommandFromResource(resource);
        var medication = medicationCommandService.handle(command);
        return medication
                .map(m -> new ResponseEntity<>(MedicationResourceFromEntityAssembler.toResourceFromEntity(m), HttpStatus.CREATED))
                .orElse(ResponseEntity.badRequest().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a medication")
    public ResponseEntity<MedicationResource> updateMedication(
            @PathVariable Long id,
            @RequestBody UpdateMedicationResource resource) {
        var command = UpdateMedicationCommandFromResourceAssembler.toCommandFromResource(id, resource);
        var medication = medicationCommandService.handle(command);
        return medication
                .map(m -> ResponseEntity.ok(MedicationResourceFromEntityAssembler.toResourceFromEntity(m)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a medication")
    public ResponseEntity<Void> deleteMedication(@PathVariable Long id) {
        var command = new DeleteMedicationCommand(id);
        medicationCommandService.handle(command);
        return ResponseEntity.noContent().build();
    }
}
