package com.chronicare.platform.attachments.interfaces.rest.transform;

import com.chronicare.platform.attachments.domain.model.aggregates.Attachment;
import com.chronicare.platform.attachments.interfaces.rest.resources.AttachmentResource;

/**
 * Summary: Assembler to transform Attachment entity to AttachmentResource
 */
public class AttachmentResourceFromEntityAssembler {

    public static AttachmentResource toResourceFromEntity(Attachment entity) {
        return new AttachmentResource(
            entity.getId(),
            entity.getFileName(),
            entity.getMimeType(),
            entity.getUrl(),
            entity.getSizeBytes(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }
}
