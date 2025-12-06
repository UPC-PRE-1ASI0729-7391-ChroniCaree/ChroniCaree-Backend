package com.chronicare.platform.records.interfaces.rest.controller;

import com.chronicare.platform.records.application.internal.commandservices.MedicalRecordCommandService;
import com.chronicare.platform.records.application.internal.commandservices.RecordExportService;
import com.chronicare.platform.records.application.internal.queryservices.MedicalRecordQueryService;
import com.chronicare.platform.records.domain.model.aggregates.MedicalRecord;
import com.chronicare.platform.records.domain.model.entities.RecordAuditLog;
import com.chronicare.platform.records.domain.model.valueobjects.RecordType;
import com.chronicare.platform.records.interfaces.rest.resources.*;
import com.chronicare.platform.records.interfaces.rest.transform.RecordResourceAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController("recordsMedicalRecordController")
@RequestMapping("/api/v1/records")
@RequiredArgsConstructor
@Tag(name = "Medical Records", description = "Medical Records Management API")
public class MedicalRecordController {

    private final MedicalRecordCommandService commandService;
    private final MedicalRecordQueryService queryService;
    private final RecordExportService exportService;

    @PostMapping
    @Operation(summary = "Create a new medical record")
    public ResponseEntity<MedicalRecordResource> createRecord(
            @RequestBody CreateRecordResource resource,
            Authentication authentication,
            HttpServletRequest request) {

        MedicalRecordCommandService.CreateRecordCommand command =
                new MedicalRecordCommandService.CreateRecordCommand(
                        resource.tenantId(),
                        resource.patientId(),
                        resource.authorId(),
                        resource.type(),
                        resource.title(),
                        resource.content(),
                        resource.structuredData(),
                        resource.attachments(),
                        resource.tags(),
                        resource.visibility(),
                        request.getRemoteAddr(),
                        request.getHeader("User-Agent")
                );

        MedicalRecord record = commandService.createRecord(command);
        MedicalRecordResource response = RecordResourceAssembler.toResourceFromEntity(record);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{recordId}")
    @Operation(summary = "Get a medical record by ID")
    public ResponseEntity<MedicalRecordResource> getRecord(
            @PathVariable Long recordId,
            Authentication authentication) {

        Long userId = getUserId(authentication);
        String userRole = getUserRole(authentication);

        return queryService.getRecordById(recordId, userId, userRole)
                .map(RecordResourceAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{recordId}")
    @Operation(summary = "Update a medical record")
    public ResponseEntity<MedicalRecordResource> updateRecord(
            @PathVariable Long recordId,
            @RequestBody UpdateRecordResource resource,
            Authentication authentication,
            HttpServletRequest request) {

        Long userId = getUserId(authentication);
        String userRole = getUserRole(authentication);

        MedicalRecordCommandService.UpdateRecordCommand command =
                new MedicalRecordCommandService.UpdateRecordCommand(
                        userId,
                        userRole,
                        resource.title(),
                        resource.content(),
                        resource.structuredData(),
                        resource.visibility(),
                        request.getRemoteAddr(),
                        request.getHeader("User-Agent")
                );

        MedicalRecord updated = commandService.updateRecord(recordId, command);
        return ResponseEntity.ok(RecordResourceAssembler.toResourceFromEntity(updated));
    }

    @PostMapping("/{recordId}/versions")
    @Operation(summary = "Create a new version of a medical record")
    public ResponseEntity<MedicalRecordResource> createVersion(
            @PathVariable Long recordId,
            @RequestBody CreateVersionResource resource,
            Authentication authentication,
            HttpServletRequest request) {

        Long userId = getUserId(authentication);
        String userRole = getUserRole(authentication);

        MedicalRecordCommandService.CreateVersionCommand command =
                new MedicalRecordCommandService.CreateVersionCommand(
                        userId,
                        userRole,
                        resource.content(),
                        resource.structuredData(),
                        resource.note(),
                        request.getRemoteAddr(),
                        request.getHeader("User-Agent")
                );

        MedicalRecord newVersion = commandService.createVersion(recordId, command);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(RecordResourceAssembler.toResourceFromEntity(newVersion));
    }

    @DeleteMapping("/{recordId}")
    @Operation(summary = "Delete a medical record (soft delete by default)")
    public ResponseEntity<Void> deleteRecord(
            @PathVariable Long recordId,
            @RequestParam(required = false, defaultValue = "false") Boolean hard,
            @RequestParam(required = false) String note,
            Authentication authentication,
            HttpServletRequest request) {

        Long userId = getUserId(authentication);
        String userRole = getUserRole(authentication);

        MedicalRecordCommandService.DeleteRecordCommand command =
                new MedicalRecordCommandService.DeleteRecordCommand(
                        userId,
                        userRole,
                        note,
                        request.getRemoteAddr(),
                        request.getHeader("User-Agent")
                );

        if (hard) {
            commandService.hardDeleteRecord(recordId, command);
        } else {
            commandService.softDeleteRecord(recordId, command);
        }

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{recordId}/versions")
    @Operation(summary = "Get version history of a medical record")
    public ResponseEntity<List<MedicalRecordResource>> getVersionHistory(
            @PathVariable Long recordId,
            Authentication authentication) {

        Long userId = getUserId(authentication);
        String userRole = getUserRole(authentication);

        List<MedicalRecord> versions = queryService.getVersionHistory(recordId, userId, userRole);
        List<MedicalRecordResource> resources = versions.stream()
                .map(RecordResourceAssembler::toResourceFromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(resources);
    }

    @GetMapping("/{recordId}/audit")
    @Operation(summary = "Get audit trail of a medical record")
    public ResponseEntity<List<RecordAuditLogResource>> getAuditTrail(
            @PathVariable Long recordId,
            Authentication authentication) {

        Long userId = getUserId(authentication);
        String userRole = getUserRole(authentication);

        List<RecordAuditLog> logs = queryService.getAuditTrail(recordId, userId, userRole);
        List<RecordAuditLogResource> resources = logs.stream()
                .map(RecordResourceAssembler::toResourceFromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(resources);
    }

    @PostMapping("/{recordId}/attachments")
    @Operation(summary = "Add attachment to a medical record")
    public ResponseEntity<Void> addAttachment(
            @PathVariable Long recordId,
            @RequestParam String attachmentId,
            Authentication authentication) {

        Long userId = getUserId(authentication);
        String userRole = getUserRole(authentication);

        commandService.addAttachment(recordId, attachmentId, userId, userRole);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{recordId}/tags")
    @Operation(summary = "Add tag to a medical record")
    public ResponseEntity<Void> addTag(
            @PathVariable Long recordId,
            @RequestParam String tag,
            Authentication authentication) {

        Long userId = getUserId(authentication);
        String userRole = getUserRole(authentication);

        commandService.addTag(recordId, tag, userId, userRole);
        return ResponseEntity.ok().build();
    }

    // Patient-specific endpoints
    @GetMapping("/patients/{patientId}/records")
    @Operation(summary = "Get all records for a patient")
    public ResponseEntity<Page<MedicalRecordResource>> getPatientRecords(
            @PathVariable Long patientId,
            @RequestParam(required = false) RecordType type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int limit,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir,
            Authentication authentication) {

        Long userId = getUserId(authentication);
        String userRole = getUserRole(authentication);

        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, limit, sort);

        Page<MedicalRecord> records = queryService.getRecordsByPatient(
                patientId, type, userId, userRole, pageable
        );

        Page<MedicalRecordResource> resources = records.map(RecordResourceAssembler::toResourceFromEntity);
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/patients/{patientId}/records/export")
    @Operation(summary = "Export patient records")
    public ResponseEntity<String> exportPatientRecords(
            @PathVariable Long patientId,
            @RequestParam(defaultValue = "json") String format,
            Authentication authentication) {

        String export;
        if ("fhir".equalsIgnoreCase(format)) {
            export = exportService.exportPatientRecordsAsFhir(patientId);
        } else {
            export = exportService.exportPatientRecordsAsJson(patientId);
        }

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .header("Content-Disposition", "attachment; filename=patient-" + patientId + "-records." + format)
                .body(export);
    }

    @GetMapping("/patients/{patientId}/records/filter")
    @Operation(summary = "Get records with filters")
    public ResponseEntity<Page<MedicalRecordResource>> getRecordsWithFilters(
            @PathVariable Long patientId,
            @RequestParam(required = false) RecordType type,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int limit,
            Authentication authentication) {

        Long userId = getUserId(authentication);
        String userRole = getUserRole(authentication);

        Pageable pageable = PageRequest.of(page, limit, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<MedicalRecord> records = queryService.getRecordsWithFilters(
                patientId, type, startDate, endDate, userId, userRole, pageable
        );

        Page<MedicalRecordResource> resources = records.map(RecordResourceAssembler::toResourceFromEntity);
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/patients/{patientId}/records/tags/{tag}")
    @Operation(summary = "Get records by tag")
    public ResponseEntity<Page<MedicalRecordResource>> getRecordsByTag(
            @PathVariable Long patientId,
            @PathVariable String tag,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int limit,
            Authentication authentication) {

        Long userId = getUserId(authentication);
        String userRole = getUserRole(authentication);

        Pageable pageable = PageRequest.of(page, limit, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<MedicalRecord> records = queryService.getRecordsByTag(
                patientId, tag, userId, userRole, pageable
        );

        Page<MedicalRecordResource> resources = records.map(RecordResourceAssembler::toResourceFromEntity);
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/patients/{patientId}/records/count")
    @Operation(summary = "Count records for a patient")
    public ResponseEntity<Long> countRecords(@PathVariable Long patientId) {
        long count = queryService.countRecordsByPatient(patientId);
        return ResponseEntity.ok(count);
    }

    // Utility methods
    private Long getUserId(Authentication authentication) {
        // Extract user ID from authentication
        // This is a placeholder - implement based on your auth mechanism
        return 1L;
    }

    private String getUserRole(Authentication authentication) {
        if (authentication == null || authentication.getAuthorities() == null) {
            return "USER";
        }
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("USER");
    }
}
