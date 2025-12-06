package com.chronicare.platform.messages.domain.model.aggregates;

import com.chronicare.platform.messages.domain.model.commands.CreateMessageCommand;
import com.chronicare.platform.messages.domain.model.valueobjects.SenderRole;
import com.chronicare.platform.messages.domain.model.valueobjects.Attachment;
import com.chronicare.platform.messages.domain.model.valueobjects.ContentType;
import com.chronicare.platform.messages.domain.model.valueobjects.MessageStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.Instant;
import java.util.*;

/**
 * Message Aggregate Root
 * Represents a message in a conversation.
 * Domain model for messaging bounded context.
 */
@Entity
@Table(name = "messages", indexes = {
    @Index(name = "idx_conversation_id_created_at", columnList = "conversation_id,created_at"),
    @Index(name = "idx_sender_id", columnList = "sender_id"),
    @Index(name = "idx_status", columnList = "status"),
    @Index(name = "idx_tenant_id", columnList = "tenant_id")
})
@Getter
@Setter
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    // Legacy support: thread-based messaging
    private Long threadId;

    // New Bounded Context: Conversation-based messaging
    private String conversationId;
    private Long tenantId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SenderRole senderRole;

    @Column(nullable = false)
    private Long senderId;

    // Legacy: single receiver (ONE_TO_ONE)
    private Long receiverId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String body;

    private String subject;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(50) DEFAULT 'TEXT'")
    private ContentType contentType = ContentType.TEXT;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(50) DEFAULT 'SENT'")
    private MessageStatus status = MessageStatus.SENT;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "message_attachments_new", joinColumns = @JoinColumn(name = "message_id"))
    private Set<Attachment> attachments = new HashSet<>();

    @Column(columnDefinition = "TEXT")
    private String metadata;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime editedAt;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "message_delivered_at_new", joinColumns = @JoinColumn(name = "message_id"))
    @MapKeyColumn(name = "recipient_id")
    @Column(name = "delivered_at")
    private Map<Long, Instant> deliveredAt = new HashMap<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "message_read_at_new", joinColumns = @JoinColumn(name = "message_id"))
    @MapKeyColumn(name = "recipient_id")
    @Column(name = "read_at")
    private Map<Long, Instant> readAt = new HashMap<>();

    private Long sequence;

    // Legacy fields
    private boolean archivedToMedicalRecord;
    private boolean isUrgent;
    private boolean isRead;

    /**
     * Default constructor required by JPA
     */
    public Message() {
    }

    /**
     * Constructor for creating a new Message from a command (legacy thread-based)
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

    /**
     * Constructor for creating a new Message in conversation-based context
     */
    public Message(String conversationId, Long tenantId, Long senderId, String body) {
        if (conversationId == null) throw new IllegalArgumentException("conversationId is required");
        if (tenantId == null) throw new IllegalArgumentException("tenantId is required");
        if (senderId == null) throw new IllegalArgumentException("senderId is required");
        if (body == null || body.isBlank()) throw new IllegalArgumentException("body is required");

        this.conversationId = conversationId;
        this.tenantId = tenantId;
        this.senderId = senderId;
        this.body = body;
        this.senderRole = SenderRole.PATIENT; // default
        this.contentType = ContentType.TEXT;
        this.status = MessageStatus.SENT;
    }

    @PrePersist
    public void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        if (this.updatedAt == null) {
            this.updatedAt = LocalDateTime.now();
        }
        if (!this.isRead && this.threadId != null) {
            this.isRead = false;
        }
        if (!this.archivedToMedicalRecord && this.threadId != null) {
            this.archivedToMedicalRecord = false;
        }
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Mark message as read (per recipient)
     */
    public Message markAsRead() {
        this.isRead = true;
        return this;
    }

    public void markReadByRecipient(Long recipientId) {
        this.readAt.put(recipientId, Instant.now());
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Mark message as delivered (per recipient)
     */
    public void markDeliveredToRecipient(Long recipientId, Instant timestamp) {
        this.deliveredAt.put(recipientId, timestamp);
        this.updatedAt = LocalDateTime.now();
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
        this.editedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        return this;
    }

    /**
     * Edit message (conversation-based)
     */
    public void edit(String newBody) {
        if (newBody == null || newBody.isBlank()) throw new IllegalArgumentException("body cannot be null or blank");
        this.body = newBody;
        this.editedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Add attachment to message
     */
    public void addAttachment(Attachment attachment) {
        if (attachment == null) throw new IllegalArgumentException("Attachment cannot be null");
        this.attachments.add(attachment);
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Set message status
     */
    public void setMessageStatus(MessageStatus newStatus) {
        if (newStatus == null) throw new IllegalArgumentException("Status cannot be null");
        this.status = newStatus;
        this.updatedAt = LocalDateTime.now();
    }
}
