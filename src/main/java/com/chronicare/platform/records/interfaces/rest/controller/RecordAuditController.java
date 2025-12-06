package com.chronicare.platform.records.interfaces.rest.controller;

import com.chronicare.platform.records.application.internal.queryservices.MedicalRecordQueryService;
import com.chronicare.platform.records.domain.model.entities.RecordAuditLog;
import com.chronicare.platform.records.interfaces.rest.resources.RecordAuditLogResource;
import com.chronicare.platform.records.interfaces.rest.transform.RecordResourceAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/records/audit")
@RequiredArgsConstructor
@Tag(name = "Record Audit", description = "Medical Record Audit Trail API")
public class RecordAuditController {

    private final MedicalRecordQueryService queryService;

    @GetMapping("/{recordId}")
    @Operation(summary = "Get complete audit trail for a medical record")
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

    // Utility methods
    private Long getUserId(Authentication authentication) {
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
