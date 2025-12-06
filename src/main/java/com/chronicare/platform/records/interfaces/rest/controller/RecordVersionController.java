package com.chronicare.platform.records.interfaces.rest.controller;

import com.chronicare.platform.records.application.internal.queryservices.MedicalRecordQueryService;
import com.chronicare.platform.records.domain.model.aggregates.MedicalRecord;
import com.chronicare.platform.records.interfaces.rest.resources.MedicalRecordResource;
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
@RequestMapping("/api/v1/records/versions")
@RequiredArgsConstructor
@Tag(name = "Record Versions", description = "Medical Record Version History API")
public class RecordVersionController {

    private final MedicalRecordQueryService queryService;

    @GetMapping("/{recordId}")
    @Operation(summary = "Get version history for a medical record")
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
