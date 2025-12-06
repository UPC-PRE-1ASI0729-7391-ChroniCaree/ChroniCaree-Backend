package com.chronicare.platform.messages.interfaces.rest.resources;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Resource representation of Message (supports both legacy thread-based and new conversation-based models)
 */
public class MessageResource {
    
    // Legacy fields (for backward compatibility with thread-based messages)
    private Long id;
    private Long threadId;
    private String senderRole;
    private Long senderId;
    private Long receiverId;
    private String subject;
    private boolean archivedToMedicalRecord;
    private boolean isUrgent;
    private boolean isRead;
    
    // New conversation-based fields
    private String conversationId;
    private String body;
    private String contentType;
    private String status;
    private List<AttachmentResource> attachments;
    private Map<Long, Instant> deliveredAt;
    private Map<Long, Instant> readAt;
    private LocalDateTime createdAt;
    private LocalDateTime editedAt;
    private Long sequenceNumber;
    private Long tenantId;

    // Default constructor
    public MessageResource() {
    }

    // Legacy constructor (for backward compatibility)
    public MessageResource(Long id, Long threadId, String senderRole, Long senderId, Long receiverId,
                          String body, String subject, LocalDateTime createdAt, boolean archivedToMedicalRecord,
                          boolean isUrgent, boolean isRead) {
        this.id = id;
        this.threadId = threadId;
        this.senderRole = senderRole;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.subject = subject;
        this.body = body;
        this.createdAt = createdAt;
        this.archivedToMedicalRecord = archivedToMedicalRecord;
        this.isUrgent = isUrgent;
        this.isRead = isRead;
    }

    // Getters and Setters for legacy fields
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getThreadId() { return threadId; }
    public void setThreadId(Long threadId) { this.threadId = threadId; }
    
    public String getSenderRole() { return senderRole; }
    public void setSenderRole(String senderRole) { this.senderRole = senderRole; }
    
    public Long getSenderId() { return senderId; }
    public void setSenderId(Long senderId) { this.senderId = senderId; }
    
    public Long getReceiverId() { return receiverId; }
    public void setReceiverId(Long receiverId) { this.receiverId = receiverId; }
    
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    
    public boolean isArchivedToMedicalRecord() { return archivedToMedicalRecord; }
    public void setArchivedToMedicalRecord(boolean archivedToMedicalRecord) { this.archivedToMedicalRecord = archivedToMedicalRecord; }
    
    public boolean isUrgent() { return isUrgent; }
    public void setUrgent(boolean urgent) { isUrgent = urgent; }
    
    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }

    // Getters and Setters for new conversation-based fields
    public String getConversationId() { return conversationId; }
    public void setConversationId(String conversationId) { this.conversationId = conversationId; }
    
    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }
    
    public String getContentType() { return contentType; }
    public void setContentType(String contentType) { this.contentType = contentType; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public List<AttachmentResource> getAttachments() { return attachments; }
    public void setAttachments(List<AttachmentResource> attachments) { this.attachments = attachments; }
    
    public Map<Long, Instant> getDeliveredAt() { return deliveredAt; }
    public void setDeliveredAt(Map<Long, Instant> deliveredAt) { this.deliveredAt = deliveredAt; }
    
    public Map<Long, Instant> getReadAt() { return readAt; }
    public void setReadAt(Map<Long, Instant> readAt) { this.readAt = readAt; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getEditedAt() { return editedAt; }
    public void setEditedAt(LocalDateTime editedAt) { this.editedAt = editedAt; }
    
    public Long getSequenceNumber() { return sequenceNumber; }
    public void setSequenceNumber(Long sequenceNumber) { this.sequenceNumber = sequenceNumber; }
    
    public Long getTenantId() { return tenantId; }
    public void setTenantId(Long tenantId) { this.tenantId = tenantId; }

    // Nested DTOs for requests/responses
    public static class CreateRequest {
        private String conversationId;
        private Long senderId;
        private Long tenantId;
        private String body;
        private String contentType;

        public String getConversationId() { return conversationId; }
        public void setConversationId(String conversationId) { this.conversationId = conversationId; }
        public Long getSenderId() { return senderId; }
        public void setSenderId(Long senderId) { this.senderId = senderId; }
        public Long getTenantId() { return tenantId; }
        public void setTenantId(Long tenantId) { this.tenantId = tenantId; }
        public String getBody() { return body; }
        public void setBody(String body) { this.body = body; }
        public String getContentType() { return contentType; }
        public void setContentType(String contentType) { this.contentType = contentType; }
    }

    public static class EditRequest {
        private String newBody;

        public String getNewBody() { return newBody; }
        public void setNewBody(String newBody) { this.newBody = newBody; }
    }

    public static class StatusUpdateRequest {
        private String status;

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }

    public static class AttachmentResource {
        private String id;
        private String filename;
        private String mimeType;
        private long size;
        private String downloadUrl;

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getFilename() { return filename; }
        public void setFilename(String filename) { this.filename = filename; }
        public String getMimeType() { return mimeType; }
        public void setMimeType(String mimeType) { this.mimeType = mimeType; }
        public long getSize() { return size; }
        public void setSize(long size) { this.size = size; }
        public String getDownloadUrl() { return downloadUrl; }
        public void setDownloadUrl(String downloadUrl) { this.downloadUrl = downloadUrl; }
    }
}
