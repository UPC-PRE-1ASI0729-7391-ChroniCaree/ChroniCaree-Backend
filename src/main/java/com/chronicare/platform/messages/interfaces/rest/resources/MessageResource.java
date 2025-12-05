package com.chronicare.platform.messages.interfaces.rest.resources;

import com.chronicare.platform.messages.domain.model.valueobjects.SenderRole;

import java.time.LocalDateTime;

/**
 * Resource representing a Message
 */
public record MessageResource(
        Long id,
        Long threadId,
        SenderRole senderRole,
        Long senderId,
        Long receiverId,
        String body,
        String subject,
        LocalDateTime createdAt,
        boolean archivedToMedicalRecord,
        boolean isUrgent,
        boolean isRead
) {
}
