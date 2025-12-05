package com.chronicare.platform.medicalRecords.interfaces.rest.controllers;

import com.chronicare.platform.medicalRecords.domain.model.aggregates.MedicalRecord;
import com.chronicare.platform.medicalRecords.domain.model.commands.CreateMedicalRecordCommand;
import com.chronicare.platform.medicalRecords.domain.model.commands.DeleteMedicalRecordCommand;
import com.chronicare.platform.medicalRecords.domain.model.commands.UpdateMedicalRecordCommand;
import com.chronicare.platform.medicalRecords.domain.model.queries.GetAllMedicalRecordsQuery;
import com.chronicare.platform.medicalRecords.domain.model.queries.GetMedicalRecordByIdQuery;
import com.chronicare.platform.medicalRecords.domain.model.queries.GetMedicalRecordsByDoctorIdQuery;
import com.chronicare.platform.medicalRecords.domain.model.queries.GetMedicalRecordsByPatientIdQuery;
import com.chronicare.platform.medicalRecords.domain.services.MedicalRecordCommandService;
import com.chronicare.platform.medicalRecords.domain.services.MedicalRecordQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * MedicalRecordController
 * <p>
 *     This controller is responsible for handling all the requests related to medical records.
 *     It exposes the following endpoints:
 *     <ul>
 *         <li>GET /api/v1/medical-records: Get all medical records</li>
 *         <li>GET /api/v1/medical-records/{id}: Get medical record by ID</li>
 *         <li>GET /api/v1/medical-records/patient/{patientId}: Get medical records by patient ID</li>
 *         <li>GET /api/v1/medical-records/doctor/{doctorId}: Get medical records by doctor ID</li>
 *         <li>POST /api/v1/medical-records: Create new medical record</li>
 *         <li>PUT /api/v1/medical-records/{id}: Update medical record</li>
 *         <li>DELETE /api/v1/medical-records/{id}: Delete medical record</li>
 *     </ul>
 * </p>
 */
@RestController
@RequestMapping(value = "/api/v1/medical-records", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "MedicalRecords", description = "Medical records management API")
public class MedicalRecordController {

    private static final Logger logger = LoggerFactory.getLogger(MedicalRecordController.class);

    private final MedicalRecordCommandService medicalRecordCommandService;
    private final MedicalRecordQueryService medicalRecordQueryService;

    /**
     * Constructor
     * @param medicalRecordCommandService The {@link MedicalRecordCommandService} instance
     * @param medicalRecordQueryService The {@link MedicalRecordQueryService} instance
     */
    public MedicalRecordController(MedicalRecordCommandService medicalRecordCommandService, 
                                   MedicalRecordQueryService medicalRecordQueryService) {
        this.medicalRecordCommandService = medicalRecordCommandService;
        this.medicalRecordQueryService = medicalRecordQueryService;
    }

    /**
     * Get all medical records
     * @return A list of all medical records in the system
     */
    @GetMapping
    @Operation(summary = "List all medical records", description = "Retrieve all medical records")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Medical records retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<List<MedicalRecord>> getAllRecords() {
        return ResponseEntity.ok(medicalRecordQueryService.handle(new GetAllMedicalRecordsQuery()));
    }

    /**
     * Get medical record by ID
     * @param id The medical record ID
     * @return The medical record if found, or a 404 response if not found
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get medical record by ID", description = "Retrieve a specific medical record by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Medical record found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Medical record not found")
    })
    public ResponseEntity<MedicalRecord> getRecordById(@PathVariable Long id) {
        return medicalRecordQueryService.handle(new GetMedicalRecordByIdQuery(id))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Get medical records by patient ID
     * @param patientId The patient ID
     * @return A list of medical records for the patient
     */
    @GetMapping("/patient/{patientId}")
    @Operation(summary = "Get medical records by patient ID", description = "Retrieve all medical records for a specific patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Medical records retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid patient ID"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<MedicalRecord>> getRecordsByPatientId(@PathVariable Long patientId) {
        try {
            if (patientId == null || patientId <= 0) {
                logger.error("Invalid patient ID: {}", patientId);
                return ResponseEntity.badRequest().build();
            }
            logger.info("Fetching medical records for patient ID: {}", patientId);
            List<MedicalRecord> records = medicalRecordQueryService.handle(new GetMedicalRecordsByPatientIdQuery(patientId));
            logger.info("Found {} medical records for patient ID: {}", records.size(), patientId);
            return ResponseEntity.ok(records);
        } catch (Exception e) {
            logger.error("Error fetching medical records for patient ID: {}", patientId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get medical records by doctor ID
     * @param doctorId The doctor ID
     * @return A list of medical records for the doctor
     */
    @GetMapping("/doctor/{doctorId}")
    @Operation(summary = "Get medical records by doctor ID", description = "Retrieve all medical records for a specific doctor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Medical records retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid doctor ID"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<MedicalRecord>> getRecordsByDoctorId(@PathVariable Long doctorId) {
        try {
            if (doctorId == null || doctorId <= 0) {
                logger.error("Invalid doctor ID: {}", doctorId);
                return ResponseEntity.badRequest().build();
            }
            logger.info("Fetching medical records for doctor ID: {}", doctorId);
            List<MedicalRecord> records = medicalRecordQueryService.handle(new GetMedicalRecordsByDoctorIdQuery(doctorId));
            logger.info("Found {} medical records for doctor ID: {}", records.size(), doctorId);
            return ResponseEntity.ok(records);
        } catch (Exception e) {
            logger.error("Error fetching medical records for doctor ID: {}", doctorId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Create a new medical record
     * @param command The {@link CreateMedicalRecordCommand} containing medical record data
     * @return The created medical record with 201 status
     */
    @PostMapping
    @Operation(summary = "Create new medical record", description = "Register a new medical record in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Medical record created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<MedicalRecord> createRecord(@RequestBody CreateMedicalRecordCommand command) {
        MedicalRecord created = medicalRecordCommandService.handle(command);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    /**
     * Update medical record by ID
     * @param id The medical record ID
     * @param command The {@link UpdateMedicalRecordCommand} containing updated medical record data
     * @return The updated medical record, or a 404 response if not found
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update medical record by ID", description = "Update medical record information by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Medical record updated successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request - ID mismatch"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Medical record not found")
    })
    public ResponseEntity<MedicalRecord> updateRecord(@PathVariable Long id, @RequestBody UpdateMedicalRecordCommand command) {
        if (!id.equals(command.medicalRecordId())) {
            return ResponseEntity.badRequest().build();
        }
        try {
            MedicalRecord updated = medicalRecordCommandService.handle(command);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Delete medical record by ID
     * @param id The medical record ID
     * @return A 204 No Content response if deleted successfully, or 404 if not found
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete medical record by ID", description = "Delete a medical record from the system by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Medical record deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Medical record not found")
    })
    public ResponseEntity<Void> deleteRecord(@PathVariable Long id) {
        try {
            medicalRecordCommandService.handle(new DeleteMedicalRecordCommand(id));
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

