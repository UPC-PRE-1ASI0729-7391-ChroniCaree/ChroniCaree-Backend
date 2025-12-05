package com.chronicare.platform.attachments.interfaces.rest;

import com.chronicare.platform.attachments.domain.model.commands.DeleteAttachmentCommand;
import com.chronicare.platform.attachments.domain.model.queries.GetAllAttachmentsQuery;
import com.chronicare.platform.attachments.domain.model.queries.GetAttachmentByIdQuery;
import com.chronicare.platform.attachments.domain.services.AttachmentCommandService;
import com.chronicare.platform.attachments.domain.services.AttachmentQueryService;
import com.chronicare.platform.attachments.interfaces.rest.resources.AttachmentResource;
import com.chronicare.platform.attachments.interfaces.rest.resources.CreateAttachmentResource;
import com.chronicare.platform.attachments.interfaces.rest.resources.UpdateAttachmentResource;
import com.chronicare.platform.attachments.interfaces.rest.transform.AttachmentResourceFromEntityAssembler;
import com.chronicare.platform.attachments.interfaces.rest.transform.CreateAttachmentCommandFromResourceAssembler;
import com.chronicare.platform.attachments.interfaces.rest.transform.UpdateAttachmentCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Attachment management
 */
@RestController
@RequestMapping(value = "/api/v1/attachments", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Attachments", description = "Attachment Management Endpoints")
public class AttachmentController {

    private final AttachmentCommandService attachmentCommandService;
    private final AttachmentQueryService attachmentQueryService;

    public AttachmentController(
            AttachmentCommandService attachmentCommandService,
            AttachmentQueryService attachmentQueryService) {
        this.attachmentCommandService = attachmentCommandService;
        this.attachmentQueryService = attachmentQueryService;
    }

    @GetMapping
    @Operation(summary = "Get all attachments")
    public ResponseEntity<List<AttachmentResource>> getAllAttachments() {
        var query = new GetAllAttachmentsQuery();
        var attachments = attachmentQueryService.handle(query);
        var resources = attachments.stream()
                .map(AttachmentResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get attachment by ID")
    public ResponseEntity<AttachmentResource> getAttachmentById(@PathVariable Long id) {
        var query = new GetAttachmentByIdQuery(id);
        var attachment = attachmentQueryService.handle(query);
        return attachment
                .map(a -> ResponseEntity.ok(AttachmentResourceFromEntityAssembler.toResourceFromEntity(a)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create a new attachment")
    public ResponseEntity<AttachmentResource> createAttachment(@RequestBody CreateAttachmentResource resource) {
        var command = CreateAttachmentCommandFromResourceAssembler.toCommandFromResource(resource);
        var attachment = attachmentCommandService.handle(command);
        return attachment
                .map(a -> new ResponseEntity<>(AttachmentResourceFromEntityAssembler.toResourceFromEntity(a), HttpStatus.CREATED))
                .orElse(ResponseEntity.badRequest().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an attachment")
    public ResponseEntity<AttachmentResource> updateAttachment(
            @PathVariable Long id,
            @RequestBody UpdateAttachmentResource resource) {
        var command = UpdateAttachmentCommandFromResourceAssembler.toCommandFromResource(id, resource);
        var attachment = attachmentCommandService.handle(command);
        return attachment
                .map(a -> ResponseEntity.ok(AttachmentResourceFromEntityAssembler.toResourceFromEntity(a)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an attachment")
    public ResponseEntity<Void> deleteAttachment(@PathVariable Long id) {
        var command = new DeleteAttachmentCommand(id);
        attachmentCommandService.handle(command);
        return ResponseEntity.noContent().build();
    }
}
