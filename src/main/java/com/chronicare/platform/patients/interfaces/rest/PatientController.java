package com.chronicare.platform.patients.interfaces.rest;

import com.chronicare.platform.patients.domain.model.aggregates.Patient;
import com.chronicare.platform.patients.domain.commands.CreatePatientCommand;
import com.chronicare.platform.patients.domain.commands.DeletePatientCommand;
import com.chronicare.platform.patients.domain.commands.UpdatePatientCommand;
import com.chronicare.platform.patients.domain.queries.GetAllPatientsQuery;
import com.chronicare.platform.patients.domain.queries.GetPatientByIdQuery;
import com.chronicare.platform.patients.domain.queries.GetPatientByUserIdQuery;
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
     * Get patient by User ID
     * @param userId The user ID
     * @return The patient if found, or a 404 response if not found
     */
    @GetMapping("/by-user/{userId}")
    @Operation(summary = "Get patient by User ID", description = "Retrieve a specific patient by their User ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Patient not found")
    })
    public ResponseEntity<Patient> getPatientByUserId(@PathVariable Long userId) {
        return patientQueryService.handle(new GetPatientByUserIdQuery(userId))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Get patients by Tenant ID
     * @param tenantId The tenant ID
     * @param page Page number (0-indexed)
     * @param limit Number of items per page
     * @return A paginated list of patients belonging to the tenant
     */
    @GetMapping("/by-tenant/{tenantId}")
    @Operation(summary = "Get patients by Tenant ID", description = "Retrieve all patients belonging to a specific tenant (hospital)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patients retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<?> getPatientsByTenantId(
            @PathVariable Long tenantId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int limit) {
        var patients = patientQueryService.handleByTenantId(tenantId, page, limit);
        return ResponseEntity.ok(patients);
    }

    record CreatePatientRequest(
        Long userId,
        Long tenantId,      // Hospital/Clinic that manages this patient
        String firstName,
        String lastName,
        String email,
        String dni,
        String birthDate,
        String gender,
        String phone,
        String address,
        String photoUrl,
        Double weight,
        Double height
    ) {}

    /**
     * Create a new patient
     * @param request The {@link CreatePatientRequest} containing patient data
     * @return The created patient with 201 status
     */
    @PostMapping
    @Operation(summary = "Create new patient", description = "Register a new patient in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Patient created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<Patient> createPatient(@RequestBody CreatePatientRequest request) {
        CreatePatientCommand command = new CreatePatientCommand(
            request.userId(),
            request.tenantId(),
            request.firstName(),
            request.lastName(),
            request.email(),
            new com.chronicare.platform.patients.domain.valueobjects.Dni(request.dni()),
            request.birthDate(),
            request.gender(),
            request.phone(),
            request.address(),
            request.photoUrl(),
            request.weight(),
            request.height()
        );
        Patient created = patientCommandService.handle(command);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    /**
     * Assign a doctor to a patient
     */
    record AssignDoctorRequest(Long doctorId) {}

    @PutMapping("/{patientId}/assign-doctor")
    @Operation(summary = "Assign doctor to patient", description = "Assigns a doctor to manage a specific patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Doctor assigned successfully"),
            @ApiResponse(responseCode = "404", description = "Patient not found"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<Patient> assignDoctorToPatient(
            @PathVariable Long patientId,
            @RequestBody AssignDoctorRequest request) {
        var patient = patientQueryService.handle(new GetPatientByIdQuery(patientId));
        if (patient.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Patient p = patient.get();
        p.assignDoctor(request.doctorId());
        Patient saved = patientCommandService.handleAssignDoctor(patientId, request.doctorId());
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{patientId}/assign-doctor")
    @Operation(summary = "Unassign doctor from patient", description = "Removes the assigned doctor from a patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Doctor unassigned successfully"),
            @ApiResponse(responseCode = "404", description = "Patient not found")
    })
    public ResponseEntity<Patient> unassignDoctorFromPatient(@PathVariable Long patientId) {
        var patient = patientQueryService.handle(new GetPatientByIdQuery(patientId));
        if (patient.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Patient saved = patientCommandService.handleUnassignDoctor(patientId);
        return ResponseEntity.ok(saved);
    }

    record UpdatePatientRequest(
        String firstName,
        String lastName,
        String email,
        String dni,
        String birthDate,
        String gender,
        String phone,
        String address,
        String photoUrl,
        Double weight,
        Double height
    ) {}

    /**
     * Update patient by ID
     * @param id The patient ID
     * @param request The {@link UpdatePatientRequest} containing updated patient data
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
    public ResponseEntity<Patient> updatePatient(@PathVariable Long id, @RequestBody UpdatePatientRequest request) {
        UpdatePatientCommand command = new UpdatePatientCommand(
            id,
            request.firstName(),
            request.lastName(),
            request.email(),
            new com.chronicare.platform.patients.domain.valueobjects.Dni(request.dni()),
            request.birthDate(),
            request.gender(),
            request.phone(),
            request.address(),
            request.photoUrl(),
            request.weight(),
            request.height()
        );
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
