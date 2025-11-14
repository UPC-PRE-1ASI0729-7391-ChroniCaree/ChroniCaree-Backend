/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.attachments.interfaces.rest;

import com.example.attachments.application.services.AttachmentService;
import com.example.attachments.domain.model.Attachment;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/attachments")
@Tag(name = "Attachments", description = "Attachment management API")
public class AttachmentController {

    private final AttachmentService attachmentService;

    public AttachmentController(AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }

    @GetMapping
    @Operation(summary = "List all attachments")
    public ResponseEntity<List<Attachment>> getAllAttachments() {
        return ResponseEntity.ok(attachmentService.getAllAttachments());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get attachment by ID")
    public ResponseEntity<Attachment> getAttachmentById(@PathVariable String id) {
        Optional<Attachment> oAttachment = attachmentService.getAttachmentById(id);

        if (oAttachment.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(oAttachment.get());
    }

    @PostMapping
    @Operation(summary = "Create new attachment")
    public ResponseEntity<Attachment> createAttachment(@RequestBody Attachment attachment) {
        Attachment created = attachmentService.createAttachment(attachment);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update attachment by ID")
    public ResponseEntity<Attachment> updateAttachment(@PathVariable String id, @RequestBody Attachment attachment) {
        Optional<Attachment> oAttachment = attachmentService.getAttachmentById(id);

        if (oAttachment.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(attachmentService.updateAttachment(id, attachment));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete attachment by ID")
    public ResponseEntity<Void> deleteAttachment(@PathVariable String id) {
         Optional<Attachment> oAttachment = attachmentService.getAttachmentById(id);

        if (oAttachment.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        attachmentService.deleteAttachment(id);
        return ResponseEntity.noContent().build();
    }
}
