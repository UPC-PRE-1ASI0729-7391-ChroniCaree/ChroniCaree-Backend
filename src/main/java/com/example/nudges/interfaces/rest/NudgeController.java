/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.nudges.interfaces.rest;

import com.example.nudges.application.services.NudgeService;
import com.example.nudges.domain.model.Nudge;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Controlador REST para la gestión de Nudges.
 */
@RestController
@RequestMapping("/api/v1/nudges")
@Tag(name = "Nudges", description = "Nudge management API")
public class NudgeController {

    private final NudgeService nudgeService;

    public NudgeController(NudgeService nudgeService) {
        this.nudgeService = nudgeService;
    }

    @GetMapping
    @Operation(summary = "List all nudges")
    public ResponseEntity<List<Nudge>> getAllNudges() {
        return ResponseEntity.ok(nudgeService.getAllNudges());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get nudge by ID")
    public ResponseEntity<Nudge> getNudgeById(@PathVariable Long id) {
        return nudgeService.getNudgeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/patient/{patientId}")
    @Operation(summary = "Get nudges by patient ID")
    public ResponseEntity<List<Nudge>> getNudgesByPatientId(@PathVariable Long patientId) {
        return ResponseEntity.ok(nudgeService.getNudgesByPatientId(patientId));
    }

    @PostMapping
    @Operation(summary = "Create new nudge")
    public ResponseEntity<Nudge> createNudge(@RequestBody Nudge nudge) {
        Nudge created = nudgeService.createNudge(nudge);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update nudge by ID")
    public ResponseEntity<Nudge> updateNudge(@PathVariable Long id, @RequestBody Nudge nudge) {
        return ResponseEntity.ok(nudgeService.updateNudge(id, nudge));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete nudge by ID")
    public ResponseEntity<Void> deleteNudge(@PathVariable Long id) {
        nudgeService.deleteNudge(id);
        return ResponseEntity.noContent().build();
    }
}
