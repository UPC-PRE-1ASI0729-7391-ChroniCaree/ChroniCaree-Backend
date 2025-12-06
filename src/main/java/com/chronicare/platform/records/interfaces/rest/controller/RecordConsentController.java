package com.chronicare.platform.records.interfaces.rest.controller;

import com.chronicare.platform.records.application.internal.commandservices.RecordConsentCommandService;
import com.chronicare.platform.records.domain.model.entities.RecordConsent;
import com.chronicare.platform.records.interfaces.rest.resources.GrantConsentResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/records/consents")
@RequiredArgsConstructor
@Tag(name = "Record Consents", description = "Medical Record Consent Management API")
public class RecordConsentController {

    private final RecordConsentCommandService consentCommandService;

    @PostMapping
    @Operation(summary = "Grant consent to access a medical record")
    public ResponseEntity<Void> grantConsent(
            @RequestBody GrantConsentResource resource,
            Authentication authentication) {

        RecordConsentCommandService.GrantConsentCommand command =
                new RecordConsentCommandService.GrantConsentCommand(
                        resource.recordId(),
                        resource.patientId(),
                        resource.grantedToUserId(),
                        resource.expiresAt(),
                        resource.purpose()
                );

        consentCommandService.grantConsent(command);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{consentId}")
    @Operation(summary = "Revoke a consent")
    public ResponseEntity<Void> revokeConsent(
            @PathVariable Long consentId,
            Authentication authentication) {

        consentCommandService.revokeConsent(consentId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/records/{recordId}")
    @Operation(summary = "Revoke all consents for a specific record")
    public ResponseEntity<Void> revokeAllConsentsForRecord(
            @PathVariable Long recordId,
            Authentication authentication) {

        consentCommandService.revokeAllConsentsForRecord(recordId);
        return ResponseEntity.noContent().build();
    }
}
