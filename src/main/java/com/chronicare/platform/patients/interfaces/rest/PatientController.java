package com.chronicare.platform.patients.interfaces.rest;

import com.chronicare.platform.patients.domain.model.aggregates.Patient;
import com.chronicare.platform.patients.domain.commands.CreatePatientCommand;
import com.chronicare.platform.patients.domain.commands.DeletePatientCommand;
import com.chronicare.platform.patients.domain.commands.UpdatePatientCommand;
import com.chronicare.platform.patients.domain.queries.GetAllPatientsQuery;
import com.chronicare.platform.patients.domain.queries.GetPatientByIdQuery;
import com.chronicare.platform.patients.domain.services.PatientCommandService;
import com.chronicare.platform.patients.domain.services.PatientQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * PatientController
 * <p>
 *     This controller is responsible for handling all the requests related to patients.
 *     It exposes the following endpoints:
 *     <ul>
 *         <li>GET /api/v1/patients: Get all patients</li>
 *         <li>GET /api/v1/patients/{id}: Get patient by ID</li>
 *         <li>POST /api/v1/patients: Create new patient</li>
 *         <li>PUT /api/v1/patients/{id}: Update patient</li>
 *         <li>DELETE /api/v1/patients/{id}: Delete patient</li>
 *     </ul>
 * </p>
 */
@RestController
@RequestMapping(value = "/api/v1/patients", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Patients", description = "Available Patient Endpoints")
public class PatientController {

    private final PatientCommandService patientCommandService;
    private final PatientQueryService patientQueryService;

    /**
     * Constructor
     * @param patientCommandService The {@link PatientCommandService} instance
     * @param patientQueryService The {@link PatientQueryService} instance
     */
    public PatientController(PatientCommandService patientCommandService, PatientQueryService patientQueryService) {
        this.patientCommandService = patientCommandService;
        this.patientQueryService = patientQueryService;
    }

    /**
     * Get all patients
     * @return A list of all patients in the system
     */
    @GetMapping
    @Operation(summary = "Get all patients", description = "Retrieve all registered patients")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patients retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<List<Patient>> getAllPatients() {
        return ResponseEntity.ok(patientQueryService.handle(new GetAllPatientsQuery()));
    }

    /**
     * Get patient by ID
     * @param id The patient ID
     * @return The patient if found, or a 404 response if not found
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get patient by ID", description = "Retrieve a specific patient by their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Patient not found")
    })
    public ResponseEntity<Patient> getPatientById(@PathVariable Long id) {
        return patientQueryService.handle(new GetPatientByIdQuery(id))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Create a new patient
     * @param command The {@link CreatePatientCommand} containing patient data
     * @return The created patient with 201 status
     */
    @PostMapping
    @Operation(summary = "Create new patient", description = "Register a new patient in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Patient created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<Patient> createPatient(@RequestBody CreatePatientCommand command) {
        Patient created = patientCommandService.handle(command);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    /**
     * Update patient by ID
     * @param id The patient ID
     * @param command The {@link UpdatePatientCommand} containing updated patient data
     * @return The updated patient, or a 404 response if not found
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update patient", description = "Update patient information by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient updated successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request - ID mismatch"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Patient not found")
    })
    public ResponseEntity<Patient> updatePatient(@PathVariable Long id, @RequestBody UpdatePatientCommand command) {
        if (!id.equals(command.patientId())) {
            return ResponseEntity.badRequest().build();
        }
        try {
            Patient updated = patientCommandService.handle(command);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Delete patient by ID
     * @param id The patient ID
     * @return A 204 No Content response if deleted successfully, or 404 if not found
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete patient", description = "Delete a patient from the system by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Patient deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Patient not found")
    })
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        try {
            patientCommandService.handle(new DeletePatientCommand(id));
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
