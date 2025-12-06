package com.chronicare.platform.records.interfaces.rest.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/records/webhooks")
@RequiredArgsConstructor
@Tag(name = "Record Webhooks", description = "Webhook endpoints for external systems to push record data")
public class RecordWebhookController {

    @PostMapping("/lab-results")
    @Operation(summary = "Receive lab results from external lab systems")
    public ResponseEntity<Map<String, String>> receiveLabResults(@RequestBody Map<String, Object> payload) {
        // Process lab results webhook
        // Validate payload
        // Create medical record from lab data
        // Send notification to relevant parties
        
        return ResponseEntity.ok(Map.of(
                "status", "received",
                "message", "Lab results received and will be processed"
        ));
    }

    @PostMapping("/imaging-reports")
    @Operation(summary = "Receive imaging reports from PACS systems")
    public ResponseEntity<Map<String, String>> receiveImagingReports(@RequestBody Map<String, Object> payload) {
        // Process imaging report webhook
        // Validate payload
        // Create medical record from imaging data
        // Attach images if provided
        
        return ResponseEntity.ok(Map.of(
                "status", "received",
                "message", "Imaging report received and will be processed"
        ));
    }

    @PostMapping("/discharge-summaries")
    @Operation(summary = "Receive discharge summaries from hospital systems")
    public ResponseEntity<Map<String, String>> receiveDischargeSummaries(@RequestBody Map<String, Object> payload) {
        // Process discharge summary webhook
        // Validate payload
        // Create medical record from discharge data
        
        return ResponseEntity.ok(Map.of(
                "status", "received",
                "message", "Discharge summary received and will be processed"
        ));
    }
}
