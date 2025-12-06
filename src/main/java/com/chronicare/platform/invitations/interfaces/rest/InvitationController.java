package com.chronicare.platform.invitations.interfaces.rest;

import com.chronicare.platform.invitations.domain.model.commands.DeleteInvitationCommand;
import com.chronicare.platform.invitations.domain.model.commands.UpdateInvitationStatusCommand;
import com.chronicare.platform.invitations.domain.model.queries.*;
import com.chronicare.platform.invitations.domain.services.InvitationCommandService;
import com.chronicare.platform.invitations.domain.services.InvitationQueryService;
import com.chronicare.platform.invitations.interfaces.rest.resources.*;
import com.chronicare.platform.invitations.interfaces.rest.transform.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Summary: REST Controller for Invitation management
 * Handles doctor invitations to hospitals/tenants
 */
@RestController
@RequestMapping(value = "/api/v1/invitations", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Invitations", description = "Invitation Management Endpoints")
public class InvitationController {

    private static final Logger log = LoggerFactory.getLogger(InvitationController.class);

    private final InvitationCommandService invitationCommandService;
    private final InvitationQueryService invitationQueryService;

    public InvitationController(
            InvitationCommandService invitationCommandService,
            InvitationQueryService invitationQueryService
    ) {
        this.invitationCommandService = invitationCommandService;
        this.invitationQueryService = invitationQueryService;
    }

    /**
     * GET /api/v1/invitations
     * Get all invitations with optional filters
     */
    @GetMapping
    @Operation(summary = "Get all invitations with optional filters")
    public ResponseEntity<List<InvitationResource>> getInvitations(
            @RequestParam(required = false) Long tenantId,
            @RequestParam(required = false) String status
    ) {
        log.info("GET /api/v1/invitations - tenantId: {}, status: {}", tenantId, status);
        
        try {
            List<InvitationResource> resources;

            if (tenantId != null && status != null && !status.isEmpty()) {
                // Filter by tenant and status
                var query = new GetInvitationsByTenantIdAndStatusQuery(tenantId, status);
                resources = invitationQueryService.handle(query).stream()
                        .map(InvitationResourceFromEntityAssembler::toResourceFromEntity)
                        .collect(Collectors.toList());
            } else if (tenantId != null) {
                // Filter by tenant only
                var query = new GetInvitationsByTenantIdQuery(tenantId);
                resources = invitationQueryService.handle(query).stream()
                        .map(InvitationResourceFromEntityAssembler::toResourceFromEntity)
                        .collect(Collectors.toList());
            } else {
                // Get all
                var query = new GetAllInvitationsQuery();
                resources = invitationQueryService.handle(query).stream()
                        .map(InvitationResourceFromEntityAssembler::toResourceFromEntity)
                        .collect(Collectors.toList());
            }

            log.info("Found {} invitations", resources.size());
            return ResponseEntity.ok(resources);
            
        } catch (Exception e) {
            log.error("Error getting invitations: tenantId={}, status={}", tenantId, status, e);
            throw e;
        }
    }

    /**
     * GET /api/v1/invitations/{id}
     * Get invitation by ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get invitation by ID")
    public ResponseEntity<InvitationResource> getInvitationById(@PathVariable Long id) {
        log.info("GET /api/v1/invitations/{}", id);
        
        var query = new GetInvitationByIdQuery(id);
        var invitation = invitationQueryService.handle(query);
        
        if (invitation.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        var resource = InvitationResourceFromEntityAssembler.toResourceFromEntity(invitation.get());
        return ResponseEntity.ok(resource);
    }

    /**
     * GET /api/v1/invitations/token/{token}
     * Get invitation by token (for validating invitation links)
     */
    @GetMapping("/token/{token}")
    @Operation(summary = "Get invitation by token")
    public ResponseEntity<InvitationResource> getInvitationByToken(@PathVariable String token) {
        log.info("GET /api/v1/invitations/token/{}", token);
        
        var query = new GetInvitationByTokenQuery(token);
        var invitation = invitationQueryService.handle(query);
        
        if (invitation.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        var resource = InvitationResourceFromEntityAssembler.toResourceFromEntity(invitation.get());
        return ResponseEntity.ok(resource);
    }

    /**
     * POST /api/v1/invitations
     * Create a new invitation
     */
    @PostMapping
    @Operation(summary = "Create a new invitation")
    public ResponseEntity<InvitationResource> createInvitation(@RequestBody CreateInvitationResource resource) {
        log.info("POST /api/v1/invitations - email: {}, tenantId: {}", resource.email(), resource.tenantId());
        
        try {
            var command = CreateInvitationCommandFromResourceAssembler.toCommandFromResource(resource);
            var invitation = invitationCommandService.handle(command);
            
            if (invitation.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            
            var invitationResource = InvitationResourceFromEntityAssembler.toResourceFromEntity(invitation.get());
            return new ResponseEntity<>(invitationResource, HttpStatus.CREATED);
            
        } catch (IllegalStateException e) {
            log.warn("Conflict creating invitation: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    /**
     * POST /api/v1/invitations/{id}/accept
     * Accept an invitation and create doctor account
     */
    @PostMapping("/{id}/accept")
    @Operation(summary = "Accept invitation and create doctor account")
    public ResponseEntity<AcceptInvitationResponse> acceptInvitation(
            @PathVariable Long id,
            @RequestBody AcceptInvitationResource resource
    ) {
        log.info("POST /api/v1/invitations/{}/accept", id);
        
        try {
            var command = AcceptInvitationCommandFromResourceAssembler.toCommandFromResource(id, resource);
            var response = invitationCommandService.handle(command);
            
            if (response.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            
            return ResponseEntity.ok(response.get());
            
        } catch (IllegalArgumentException e) {
            log.warn("Bad request accepting invitation: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (IllegalStateException e) {
            log.warn("Conflict accepting invitation: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    /**
     * PATCH /api/v1/invitations/{id}
     * Update invitation status
     */
    @PatchMapping("/{id}")
    @Operation(summary = "Update invitation status")
    public ResponseEntity<InvitationResource> updateInvitation(
            @PathVariable Long id,
            @RequestBody UpdateInvitationResource resource
    ) {
        log.info("PATCH /api/v1/invitations/{} - status: {}", id, resource.status());
        
        try {
            var command = new UpdateInvitationStatusCommand(id, resource.status());
            var invitation = invitationCommandService.handle(command);
            
            if (invitation.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            
            var invitationResource = InvitationResourceFromEntityAssembler.toResourceFromEntity(invitation.get());
            return ResponseEntity.ok(invitationResource);
            
        } catch (IllegalArgumentException e) {
            log.warn("Bad request updating invitation: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (IllegalStateException e) {
            log.warn("Conflict updating invitation: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    /**
     * DELETE /api/v1/invitations/{id}
     * Delete/cancel an invitation
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete/cancel invitation")
    public ResponseEntity<Void> deleteInvitation(@PathVariable Long id) {
        log.info("DELETE /api/v1/invitations/{}", id);
        
        try {
            var command = new DeleteInvitationCommand(id);
            invitationCommandService.handle(command);
            return ResponseEntity.noContent().build();
            
        } catch (IllegalArgumentException e) {
            log.warn("Not found deleting invitation: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
