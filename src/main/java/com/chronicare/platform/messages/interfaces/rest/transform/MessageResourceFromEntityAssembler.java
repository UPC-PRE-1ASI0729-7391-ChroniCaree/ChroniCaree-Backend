package com.chronicare.platform.messages.interfaces.rest.transform;

import com.chronicare.platform.messages.domain.model.aggregates.Message;
import com.chronicare.platform.messages.domain.model.valueobjects.Attachment;
import com.chronicare.platform.messages.interfaces.rest.resources.MessageResource;
import org.springframework.stereotype.Component;

/**
 * Assembler for converting Message aggregate to MessageResource
 * Supports both legacy thread-based and new conversation-based models
 */
@Component
public class MessageResourceFromEntityAssembler {

    // Support for new conversation-based messages
    public MessageResource toResource(Message message) {
        MessageResource resource = new MessageResource();
        
        // Convert UUID id to Long by hashing
        if (message.getId() != null) {
            resource.setId((long) message.getId().hashCode());
        }
        resource.setConversationId(message.getConversationId());
        resource.setSenderId(message.getSenderId());
        resource.setBody(message.getBody());
        if (message.getContentType() != null) {
            resource.setContentType(message.getContentType().toString());
        }
        if (message.getStatus() != null) {
            resource.setStatus(message.getStatus().toString());
        }
        resource.setCreatedAt(message.getCreatedAt());
        resource.setEditedAt(message.getEditedAt());
        resource.setSequenceNumber(message.getSequence());
        resource.setDeliveredAt(message.getDeliveredAt());
        resource.setReadAt(message.getReadAt());
        resource.setTenantId(message.getTenantId());
        
        if (message.getAttachments() != null && !message.getAttachments().isEmpty()) {
            resource.setAttachments(message.getAttachments().stream()
                    .map(this::toAttachmentResource)
                    .toList());
        }
        
        return resource;
    }

    // Support for legacy thread-based messages
    public static MessageResource toResourceFromEntity(Message entity) {
        MessageResource resource = new MessageResource();
        if (entity.getId() != null) {
            resource.setId((long) entity.getId().hashCode());
        }
        resource.setThreadId(entity.getThreadId());
        resource.setSenderRole(entity.getSenderRole() != null ? entity.getSenderRole().toString() : null);
        resource.setSenderId(entity.getSenderId());
        resource.setReceiverId(entity.getReceiverId());
        resource.setBody(entity.getBody());
        resource.setSubject(entity.getSubject());
        resource.setCreatedAt(entity.getCreatedAt());
        resource.setArchivedToMedicalRecord(entity.isArchivedToMedicalRecord());
        resource.setUrgent(entity.isUrgent());
        resource.setRead(entity.isRead());
        return resource;
    }

    private MessageResource.AttachmentResource toAttachmentResource(Attachment attachment) {
        MessageResource.AttachmentResource resource = new MessageResource.AttachmentResource();
        resource.setId(attachment.getId());
        resource.setFilename(attachment.getFilename());
        resource.setMimeType(attachment.getMimeType());
        resource.setSize(attachment.getSize());
        resource.setDownloadUrl(attachment.getDownloadUrl());
        return resource;
    }
}
