/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.messages.interfaces.rest;

import com.example.messages.application.services.MessageService;
import com.example.messages.domain.model.Message;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/messages")
@Tag(name = "Messages", description = "Message management API")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping
    @Operation(summary = "List all messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        return ResponseEntity.ok(messageService.getAllMessages());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get message by ID")
    public ResponseEntity<Message> getMessageById(@PathVariable Long id) {
        Optional<Message> oMessage = messageService.getMessageById(id);

        if (oMessage.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(oMessage.get());

    }

    @GetMapping("/thread/{threadId}")
    @Operation(summary = "Get messages by thread ID")
    public ResponseEntity<List<Message>> getMessagesByThread(@PathVariable Long threadId) {
        return ResponseEntity.ok(messageService.getMessagesByThread(threadId));
    }

    @PostMapping
    @Operation(summary = "Create a new message")
    public ResponseEntity<Message> createMessage(@RequestBody Message message) {
        Message created = messageService.createMessage(message);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}/read")
    @Operation(summary = "Mark message as read")
    public ResponseEntity<Message> markAsRead(@PathVariable Long id) {
        return ResponseEntity.ok(messageService.markAsRead(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update message by ID")
    public ResponseEntity<Message> markAsRead(@PathVariable Long id, @RequestBody Message message) {
        Optional<Message> oMessage = messageService.getMessageById(id);

        if (oMessage.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(messageService.updateMessage(id, message));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete message by ID")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long id) {
        Optional<Message> oMessage = messageService.getMessageById(id);

        if (oMessage.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        messageService.deleteMessage(id);
        return ResponseEntity.noContent().build();
    }
}
