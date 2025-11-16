/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/RestController.java to edit this template
 */
package com.example.tenants.interfaces.rest;

 
import com.example.tenants.application.services.TenantService;
import com.example.tenants.domain.aggregates.Tenant;
import com.example.tenants.domain.commands.CreateTenantCommand;
import com.example.tenants.domain.commands.DeleteTenantCommand;
import com.example.tenants.domain.commands.UpdateTenantCommand;
import com.example.tenants.domain.valueobjects.TenantName;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * TenantController - expone endpoints REST usando commands/queries.
 */
@RestController
@RequestMapping("/api/v1/tenants")
@Tag(name = "Tenants", description = "Tenant management API")
public class TenantController {

    private final TenantService tenantService;

    public TenantController(TenantService tenantService) {
        this.tenantService = tenantService;
    }

    record TenantResponse(Long id, String name) { }

    record CreateTenantRequest(String name) {
        public CreateTenantRequest {
            if (name == null || name.isBlank()) {
                throw new IllegalArgumentException("name cannot be null or blank");
            }
        }
    }

    record UpdateTenantRequest(String name) {
        public UpdateTenantRequest {
            if (name == null || name.isBlank()) {
                throw new IllegalArgumentException("name cannot be null or blank");
            }
        }
    }

    @GetMapping
    @Operation(summary = "List all tenants")
    public ResponseEntity<List<TenantResponse>> getAllTenants() {
        List<TenantResponse> resp = tenantService.getAllTenants().stream()
                .map(t -> new TenantResponse(t.getId(), t.getName().value()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get tenant by ID")
    public ResponseEntity<TenantResponse> getTenantById(@PathVariable Long id) {
        return tenantService.getTenantById(id)
                .map(t -> new TenantResponse(t.getId(), t.getName().value()))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create new tenant")
    public ResponseEntity<TenantResponse> createTenant(@RequestBody CreateTenantRequest req) {
        CreateTenantCommand cmd = new CreateTenantCommand(new TenantName(req.name()));
        Tenant created = tenantService.createTenant(cmd);
        TenantResponse resp = new TenantResponse(created.getId(), created.getName().value());
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update tenant by ID")
    public ResponseEntity<TenantResponse> updateTenant(@PathVariable Long id, @RequestBody UpdateTenantRequest req) {
        UpdateTenantCommand cmd = new UpdateTenantCommand(id, new TenantName(req.name()));
        Tenant updated = tenantService.updateTenant(cmd);
        TenantResponse resp = new TenantResponse(updated.getId(), updated.getName().value());
        return ResponseEntity.ok(resp);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete tenant by ID")
    public ResponseEntity<Void> deleteTenant(@PathVariable Long id) {
        DeleteTenantCommand cmd = new DeleteTenantCommand(id);
        tenantService.deleteTenant(cmd);
        return ResponseEntity.noContent().build();
    }
}