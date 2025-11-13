/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.alerts.interfaces.rest;

import com.example.alerts.application.services.AlertService;
import com.example.alerts.domain.model.Alert;
import com.example.diagnosis.domain.model.Diagnosis;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/alerts")
@Tag(name = "Alerts", description = "Alert management API")
public class AlertController {

    private final AlertService alertService;

    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    @GetMapping
    @Operation(summary = "List all alerts")
    public ResponseEntity<List<Alert>> getAllAlerts() {
        return ResponseEntity.ok(alertService.getAllAlerts());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get alert by ID")
    public ResponseEntity<Alert> getAlertById(@PathVariable String id) {
        Optional<Alert> oAlert = alertService.getAlertById(id);
        if (oAlert.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(oAlert.get());
    }

    @GetMapping("/patient/{patientId}")
    @Operation(summary = "Get alerts by patient ID")
    public ResponseEntity<List<Alert>> getAlertsByPatientId(@PathVariable String patientId) {
        return ResponseEntity.ok(alertService.getAlertsByPatientId(patientId));
    }

    @PostMapping
    @Operation(summary = "Create new alert")
    public ResponseEntity<Alert> createAlert(@RequestBody Alert alert) {
        Alert created = alertService.createAlert(alert);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update alert by ID")
    public ResponseEntity<Alert> updateAlert(@PathVariable String id, @RequestBody Alert alert) {

        Optional<Alert> oAlert = alertService.getAlertById(id);

        if (oAlert.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(alertService.updateAlert(id, alert));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete alert by ID")
    public ResponseEntity<Void> deleteAlert(@PathVariable String id) {
        Optional<Alert> oAlert = alertService.getAlertById(id);
        if (oAlert.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        alertService.deleteAlert(id);
        return ResponseEntity.noContent().build();
    }

}
