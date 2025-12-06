package com.chronicare.platform.messages.interfaces.rest.controller;

import com.chronicare.platform.messages.application.service.ConversationCommandService;
import com.chronicare.platform.messages.application.service.ConversationQueryService;
import com.chronicare.platform.messages.domain.model.aggregates.Conversation;
import com.chronicare.platform.messages.interfaces.rest.resources.ConversationResource;
import com.chronicare.platform.messages.interfaces.rest.transform.ConversationResourceFromEntityAssembler;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * REST Controller for Conversation management
 */
@RestController
@RequestMapping("/api/v1/conversations")
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@PreAuthorize("hasAnyRole('DOCTOR', 'PATIENT', 'ADMIN')")
public class ConversationController {

    private final ConversationCommandService conversationCommandService;
    private final ConversationQueryService conversationQueryService;
    private final ConversationResourceFromEntityAssembler assembler;

    public ConversationController(ConversationCommandService conversationCommandService,
                                  ConversationQueryService conversationQueryService,
                                  ConversationResourceFromEntityAssembler assembler) {
        this.conversationCommandService = conversationCommandService;
        this.conversationQueryService = conversationQueryService;
        this.assembler = assembler;
    }

    @GetMapping
    public ResponseEntity<List<ConversationResource>> listConversations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit) {
        List<Conversation> conversations = conversationQueryService.listConversations(page, limit);
        List<ConversationResource> resources = conversations.stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/{conversationId}")
    public ResponseEntity<ConversationResource> getConversationById(@PathVariable String conversationId) {
        Conversation conversation = conversationQueryService.getConversationById(conversationId);
        return ResponseEntity.ok(assembler.toResource(conversation));
    }

    @PostMapping
    public ResponseEntity<ConversationResource> createConversation(
            @RequestBody ConversationResource.CreateRequest request) {
        Conversation conversation = conversationCommandService.createConversation(
                request.getTenantId(),
                request.getType(),
                request.getParticipantUserIds());
        return ResponseEntity.status(HttpStatus.CREATED).body(assembler.toResource(conversation));
    }

    @PutMapping("/{conversationId}")
    public ResponseEntity<ConversationResource> updateConversation(
            @PathVariable String conversationId,
            @RequestBody ConversationResource.UpdateRequest request) {
        Conversation conversation = conversationCommandService.updateConversation(
                conversationId, request.getTitle(), request.getDescription());
        return ResponseEntity.ok(assembler.toResource(conversation));
    }

    @DeleteMapping("/{conversationId}")
    public ResponseEntity<Void> deleteConversation(@PathVariable String conversationId) {
        conversationCommandService.deleteConversation(conversationId);
        return ResponseEntity.noContent().build();
    }
}
