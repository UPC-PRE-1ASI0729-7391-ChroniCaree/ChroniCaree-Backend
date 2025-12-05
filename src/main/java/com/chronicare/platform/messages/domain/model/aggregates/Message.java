package com.chronicare.platform.messages.domain.model.aggregates;

import com.chronicare.platform.messages.domain.model.commands.CreateMessageCommand;
import com.chronicare.platform.messages.domain.model.valueobjects.SenderRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Message Aggregate Root
 * Represents a message in the system
 */
@Entity
@Table(name = "messages")
@Getter
@Setter
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long threadId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SenderRole senderRole;

    @Column(nullable = false)
    private Long senderId;

    @Column(nullable = false)
    private Long receiverId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String body;

    private String subject;

    private LocalDateTime createdAt;

    private boolean archivedToMedicalRecord;
    private boolean isUrgent;
    private boolean isRead;

    /**
     * Default constructor required by JPA
     */
    public Message() {
    }

    /**
     * Constructor for creating a new Message from a command
     * @param command The CreateMessageCommand containing message data
     */
    public Message(CreateMessageCommand command) {
        this.threadId = command.threadId();
        this.senderRole = command.senderRole();
        this.senderId = command.senderId();
        this.receiverId = command.receiverId();
        this.body = command.body();
        this.subject = command.subject();
        this.isUrgent = command.isUrgent();
        this.archivedToMedicalRecord = false;
        this.isRead = false;
    }

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.isRead = false;
        this.archivedToMedicalRecord = false;
    }

    /**
     * Mark message as read
     */
    public Message markAsRead() {
        this.isRead = true;
        return this;
    }

    /**
     * Archive message to medical record
     */
    public Message archiveToMedicalRecord() {
        this.archivedToMedicalRecord = true;
        return this;
    }

    /**
     * Update message details
     */
    public Message updateMessage(String body, String subject, boolean isUrgent) {
        this.body = body;
        this.subject = subject;
        this.isUrgent = isUrgent;
        return this;
    }
}
