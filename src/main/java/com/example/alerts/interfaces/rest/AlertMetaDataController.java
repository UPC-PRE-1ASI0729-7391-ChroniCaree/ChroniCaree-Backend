/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.alerts.interfaces.rest;

import com.example.alerts.application.services.AlertMetaDataService;
import com.example.alerts.domain.model.AlertMetaData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/v1/alert-metadata")
@Tag(name = "Alert Metadata", description = "Alert metadata management API")
public class AlertMetaDataController {

    private final AlertMetaDataService alertMetadataService;

    public AlertMetaDataController(AlertMetaDataService alertMetadataService) {
        this.alertMetadataService = alertMetadataService;
    }

    @GetMapping
    @Operation(summary = "List all alert metadata")
    public ResponseEntity<List<AlertMetaData>> getAllAlertMetadata() {
        return ResponseEntity.ok(alertMetadataService.getAllAlertMetadata());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get alert metadata by ID")
    public ResponseEntity<AlertMetaData> getAlertMetadataById(@PathVariable Long id) {
        return alertMetadataService.getAlertMetadataById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/vital-sign/{vitalSign}")
    @Operation(summary = "Get alert metadata by vital sign")
    public ResponseEntity<List<AlertMetaData>> getAlertMetadataByVitalSign(@PathVariable String vitalSign) {
        return ResponseEntity.ok(alertMetadataService.getAlertMetadataByVitalSign(vitalSign));
    }

    @PostMapping
    @Operation(summary = "Create new alert metadata")
    public ResponseEntity<AlertMetaData> createAlertMetadata(@RequestBody AlertMetaData alertMetadata) {
        AlertMetaData created = alertMetadataService.createAlertMetadata(alertMetadata);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update alert metadata by ID")
    public ResponseEntity<AlertMetaData> updateAlertMetadata(@PathVariable Long id, @RequestBody AlertMetaData alertMetadata) {
        return ResponseEntity.ok(alertMetadataService.updateAlertMetadata(id, alertMetadata));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete alert metadata by ID")
    public ResponseEntity<Void> deleteAlertMetadata(@PathVariable Long id) {
        alertMetadataService.deleteAlertMetadata(id);
        return ResponseEntity.noContent().build();
    }
}
