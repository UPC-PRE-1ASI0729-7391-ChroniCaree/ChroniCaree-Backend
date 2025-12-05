package com.chronicare.platform.attachments.interfaces.rest.transform;

import com.chronicare.platform.attachments.domain.model.commands.UpdateAttachmentCommand;
import com.chronicare.platform.attachments.interfaces.rest.resources.UpdateAttachmentResource;

/**
 * Assembler to transform UpdateAttachmentResource to UpdateAttachmentCommand
 */
public class UpdateAttachmentCommandFromResourceAssembler {

    public static UpdateAttachmentCommand toCommandFromResource(Long attachmentId, UpdateAttachmentResource resource) {
        return new UpdateAttachmentCommand(
            attachmentId,
            resource.fileName(),
            resource.mimeType(),
            resource.url(),
            resource.sizeBytes()
        );
    }
}
