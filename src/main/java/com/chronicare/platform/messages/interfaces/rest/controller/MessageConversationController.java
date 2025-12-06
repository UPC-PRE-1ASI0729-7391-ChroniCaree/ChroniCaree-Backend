package com.chronicare.platform.messages.interfaces.rest.controller;

import com.chronicare.platform.messages.application.service.MessageCommandService;
import com.chronicare.platform.messages.application.service.MessageQueryService;
import com.chronicare.platform.messages.domain.model.aggregates.Message;
import com.chronicare.platform.messages.interfaces.rest.resources.MessageResource;
import com.chronicare.platform.messages.interfaces.rest.transform.MessageResourceFromEntityAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * REST Controller for Message management in Conversations
 */
@RestController
@RequestMapping("/api/v1/messages")
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@PreAuthorize("hasAnyRole('DOCTOR', 'PATIENT', 'ADMIN')")
public class MessageConversationController {

    private final MessageCommandService messageCommandService;
    private final MessageQueryService messageQueryService;
    private final MessageResourceFromEntityAssembler assembler;

    public MessageConversationController(MessageCommandService messageCommandService,
                                        MessageQueryService messageQueryService,
                                        MessageResourceFromEntityAssembler assembler) {
        this.messageCommandService = messageCommandService;
        this.messageQueryService = messageQueryService;
        this.assembler = assembler;
    }

    @GetMapping
    public ResponseEntity<List<MessageResource>> listMessages(
            @RequestParam String conversationId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int limit) {
        List<Message> messages = messageQueryService.listMessages(conversationId, page, limit);
        List<MessageResource> resources = messages.stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/{messageId}")
    public ResponseEntity<MessageResource> getMessageById(@PathVariable String messageId) {
        Message message = messageQueryService.getMessageById(messageId);
        return ResponseEntity.ok(assembler.toResource(message));
    }

    @PostMapping
    public ResponseEntity<MessageResource> createMessage(
            @RequestBody MessageResource.CreateRequest request,
            @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey) {
        Message message = messageCommandService.createMessage(
                request.getConversationId(),
                request.getSenderId(),
                request.getTenantId(),
                request.getBody(),
                request.getContentType());
        return ResponseEntity.status(HttpStatus.CREATED).body(assembler.toResource(message));
    }

    @PutMapping("/{messageId}")
    public ResponseEntity<MessageResource> editMessage(
            @PathVariable String messageId,
            @RequestBody MessageResource.EditRequest request) {
        Message message = messageCommandService.editMessage(messageId, request.getNewBody());
        return ResponseEntity.ok(assembler.toResource(message));
    }

    @PatchMapping("/{messageId}/status")
    public ResponseEntity<MessageResource> updateMessageStatus(
            @PathVariable String messageId,
            @RequestBody MessageResource.StatusUpdateRequest request) {
        Message message = messageCommandService.updateMessageStatus(messageId, request.getStatus());
        return ResponseEntity.ok(assembler.toResource(message));
    }

    @DeleteMapping("/{messageId}")
    public ResponseEntity<Void> deleteMessage(@PathVariable String messageId) {
        messageCommandService.deleteMessage(messageId);
        return ResponseEntity.noContent().build();
    }
}
