package com.chronicare.platform.messages.domain.model.aggregates;

import com.chronicare.platform.messages.domain.model.valueobjects.ConversationType;
import com.chronicare.platform.messages.domain.model.valueobjects.Participant;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.*;

@Entity
@Table(name = "conversations")
public class Conversation {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private Long tenantId;

    @Enumerated(EnumType.STRING)
    private ConversationType type;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "conversation_participants", joinColumns = @JoinColumn(name = "conversation_id"))
    private Set<Participant> participants = new HashSet<>();

    private String subject;
    
    private String title;
    
    private String description;

    @Column(columnDefinition = "TEXT")
    private String metadata;

    private Instant createdAt;
    private Instant updatedAt;
    private Instant lastMessageAt;
    
    private int messageCount = 0;

    public Conversation() {}

    public Conversation(Long tenantId, ConversationType type, Set<Participant> participants, String subject) {
        if (tenantId == null) throw new IllegalArgumentException("tenantId is required");
        if (type == null) throw new IllegalArgumentException("type is required");
        if (participants == null || participants.isEmpty()) throw new IllegalArgumentException("participants cannot be empty");
        
        this.tenantId = tenantId;
        this.type = type;
        this.participants = participants;
        this.subject = subject;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Long getTenantId() { return tenantId; }
    public void setTenantId(Long tenantId) { this.tenantId = tenantId; }
    
    public ConversationType getType() { return type; }
    public void setType(ConversationType type) { this.type = type; }
    public void setType(String typeStr) { 
        try {
            this.type = ConversationType.valueOf(typeStr.toUpperCase());
        } catch (IllegalArgumentException _) {
            this.type = ConversationType.GROUP;
        }
    }
    
    public Set<Participant> getParticipants() { return new HashSet<>(participants); }
    public void setParticipants(Set<Participant> participants) { this.participants = participants; }
    
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getMetadata() { return metadata; }
    public void setMetadata(String metadata) { this.metadata = metadata; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    
    public Instant getUpdatedAt() { return updatedAt; }
    
    public Instant getLastMessageAt() { return lastMessageAt; }
    public void setLastMessageAt(Instant lastMessageAt) { 
        this.lastMessageAt = lastMessageAt; 
        this.updatedAt = Instant.now();
    }
    
    public int getMessageCount() { return messageCount; }
    public void setMessageCount(int messageCount) { this.messageCount = messageCount; }

    public void addParticipant(Participant participant) {
        if (participant == null) throw new IllegalArgumentException("Participant cannot be null");
        this.participants.add(participant);
        this.updatedAt = Instant.now();
    }

    public void removeParticipant(Long userId) {
        this.participants.removeIf(p -> p.getUserId().equals(userId));
        this.updatedAt = Instant.now();
    }
}
