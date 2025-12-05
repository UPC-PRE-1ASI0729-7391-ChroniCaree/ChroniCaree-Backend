package com.chronicare.platform.messages.interfaces.rest;

import com.chronicare.platform.messages.domain.model.commands.DeleteMessageCommand;
import com.chronicare.platform.messages.domain.model.commands.MarkMessageAsReadCommand;
import com.chronicare.platform.messages.domain.model.queries.*;
import com.chronicare.platform.messages.domain.services.MessageCommandService;
import com.chronicare.platform.messages.domain.services.MessageQueryService;
import com.chronicare.platform.messages.interfaces.rest.resources.CreateMessageResource;
import com.chronicare.platform.messages.interfaces.rest.resources.MessageResource;
import com.chronicare.platform.messages.interfaces.rest.resources.UpdateMessageResource;
import com.chronicare.platform.messages.interfaces.rest.transform.CreateMessageCommandFromResourceAssembler;
import com.chronicare.platform.messages.interfaces.rest.transform.MessageResourceFromEntityAssembler;
import com.chronicare.platform.messages.interfaces.rest.transform.UpdateMessageCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Message management
 */
@RestController
@RequestMapping(value = "/api/v1/messages", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Messages", description = "Message Management Endpoints")
public class MessageController {

    private final MessageCommandService messageCommandService;
    private final MessageQueryService messageQueryService;

    public MessageController(
            MessageCommandService messageCommandService,
            MessageQueryService messageQueryService) {
        this.messageCommandService = messageCommandService;
        this.messageQueryService = messageQueryService;
    }

    @GetMapping
    @Operation(summary = "Get all messages")
    public ResponseEntity<List<MessageResource>> getAllMessages() {
        var query = new GetAllMessagesQuery();
        var messages = messageQueryService.handle(query);
        var resources = messages.stream()
                .map(MessageResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get message by ID")
    public ResponseEntity<MessageResource> getMessageById(@PathVariable Long id) {
        var query = new GetMessageByIdQuery(id);
        var message = messageQueryService.handle(query);
        return message
                .map(m -> ResponseEntity.ok(MessageResourceFromEntityAssembler.toResourceFromEntity(m)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/thread/{threadId}")
    @Operation(summary = "Get messages by thread ID")
    public ResponseEntity<List<MessageResource>> getMessagesByThread(@PathVariable Long threadId) {
        var query = new GetMessagesByThreadIdQuery(threadId);
        var messages = messageQueryService.handle(query);
        var resources = messages.stream()
                .map(MessageResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(resources);
    }

    @PostMapping
    @Operation(summary = "Create a new message")
    public ResponseEntity<MessageResource> createMessage(@RequestBody CreateMessageResource resource) {
        var command = CreateMessageCommandFromResourceAssembler.toCommandFromResource(resource);
        var message = messageCommandService.handle(command);
        return message
                .map(m -> new ResponseEntity<>(MessageResourceFromEntityAssembler.toResourceFromEntity(m), HttpStatus.CREATED))
                .orElse(ResponseEntity.badRequest().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a message")
    public ResponseEntity<MessageResource> updateMessage(
            @PathVariable Long id,
            @RequestBody UpdateMessageResource resource) {
        var command = UpdateMessageCommandFromResourceAssembler.toCommandFromResource(id, resource);
        var message = messageCommandService.handle(command);
        return message
                .map(m -> ResponseEntity.ok(MessageResourceFromEntityAssembler.toResourceFromEntity(m)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/read")
    @Operation(summary = "Mark message as read")
    public ResponseEntity<MessageResource> markAsRead(@PathVariable Long id) {
        var command = new MarkMessageAsReadCommand(id);
        var message = messageCommandService.handle(command);
        return message
                .map(m -> ResponseEntity.ok(MessageResourceFromEntityAssembler.toResourceFromEntity(m)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a message")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long id) {
        var command = new DeleteMessageCommand(id);
        messageCommandService.handle(command);
        return ResponseEntity.noContent().build();
    }
}
