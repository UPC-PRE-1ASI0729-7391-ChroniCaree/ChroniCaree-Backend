package com.chronicare.platform.records.interfaces.rest.controller;

import com.chronicare.platform.records.application.internal.queryservices.MedicalRecordQueryService;
import com.chronicare.platform.records.domain.model.aggregates.MedicalRecord;
import com.chronicare.platform.records.interfaces.rest.resources.MedicalRecordResource;
import com.chronicare.platform.records.interfaces.rest.transform.RecordResourceAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/records/search")
@RequiredArgsConstructor
@Tag(name = "Record Search", description = "Medical Record Search API")
public class RecordSearchController {

    private final MedicalRecordQueryService queryService;

    @GetMapping
    @Operation(summary = "Full-text search across medical records")
    public ResponseEntity<Page<MedicalRecordResource>> searchRecords(
            @RequestParam String q,
            @RequestParam(required = false) Long patientId,
            @RequestParam(required = false) Long tenantId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int limit,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir,
            Authentication authentication) {

        Long userId = getUserId(authentication);
        String userRole = getUserRole(authentication);

        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, limit, sort);

        Page<MedicalRecord> records = queryService.searchRecords(
                patientId, q, userId, userRole, pageable
        );

        Page<MedicalRecordResource> resources = records.map(RecordResourceAssembler::toResourceFromEntity);
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/patients/{patientId}")
    @Operation(summary = "Search records for a specific patient")
    public ResponseEntity<Page<MedicalRecordResource>> searchPatientRecords(
            @PathVariable Long patientId,
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int limit,
            Authentication authentication) {

        Long userId = getUserId(authentication);
        String userRole = getUserRole(authentication);

        Pageable pageable = PageRequest.of(page, limit, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<MedicalRecord> records = queryService.searchRecords(
                patientId, q, userId, userRole, pageable
        );

        Page<MedicalRecordResource> resources = records.map(RecordResourceAssembler::toResourceFromEntity);
        return ResponseEntity.ok(resources);
    }

    // Utility methods
    private Long getUserId(Authentication authentication) {
        // Extract user ID from authentication
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
