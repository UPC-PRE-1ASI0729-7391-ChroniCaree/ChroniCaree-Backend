package com.chronicare.platform.messages.interfaces.rest.transform;

import com.chronicare.platform.messages.domain.model.aggregates.Message;
import com.chronicare.platform.messages.interfaces.rest.resources.MessageResource;

/**
 * Assembler to convert Message entity to MessageResource
 */
public class MessageResourceFromEntityAssembler {

    public static MessageResource toResourceFromEntity(Message entity) {
        return new MessageResource(
                entity.getId(),
                entity.getThreadId(),
                entity.getSenderRole(),
                entity.getSenderId(),
                entity.getReceiverId(),
                entity.getBody(),
                entity.getSubject(),
                entity.getCreatedAt(),
                entity.isArchivedToMedicalRecord(),
                entity.isUrgent(),
                entity.isRead()
        );
    }
}
