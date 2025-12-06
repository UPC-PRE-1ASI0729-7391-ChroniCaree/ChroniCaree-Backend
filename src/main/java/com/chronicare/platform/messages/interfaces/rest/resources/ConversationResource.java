package com.chronicare.platform.messages.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.util.List;

/**
 * Resource representation of Conversation
 */
public class ConversationResource {
    
    private String id;
    private String type;
    private String title;
    private String description;
    private Long tenantId;
    private List<ParticipantResource> participants;
    private Instant createdAt;
    private Instant lastMessageAt;
    private int messageCount;

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public Long getTenantId() { return tenantId; }
    public void setTenantId(Long tenantId) { this.tenantId = tenantId; }
    
    public List<ParticipantResource> getParticipants() { return participants; }
    public void setParticipants(List<ParticipantResource> participants) { this.participants = participants; }
    
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    
    public Instant getLastMessageAt() { return lastMessageAt; }
    public void setLastMessageAt(Instant lastMessageAt) { this.lastMessageAt = lastMessageAt; }
    
    public int getMessageCount() { return messageCount; }
    public void setMessageCount(int messageCount) { this.messageCount = messageCount; }

    public static class CreateRequest {
        private Long tenantId;
        private String type;
        private List<Long> participantUserIds;

        public Long getTenantId() { return tenantId; }
        public void setTenantId(Long tenantId) { this.tenantId = tenantId; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public List<Long> getParticipantUserIds() { return participantUserIds; }
        public void setParticipantUserIds(List<Long> participantUserIds) { this.participantUserIds = participantUserIds; }
    }

    public static class UpdateRequest {
        private String title;
        private String description;

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }

    public static class ParticipantResource {
        private Long userId;
        private String role;
        private Instant joinedAt;

        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
        public Instant getJoinedAt() { return joinedAt; }
        public void setJoinedAt(Instant joinedAt) { this.joinedAt = joinedAt; }
    }
}
