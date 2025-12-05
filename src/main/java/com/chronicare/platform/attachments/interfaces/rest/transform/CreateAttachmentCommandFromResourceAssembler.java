package com.chronicare.platform.attachments.interfaces.rest.transform;

import com.chronicare.platform.attachments.domain.model.commands.CreateAttachmentCommand;
import com.chronicare.platform.attachments.interfaces.rest.resources.CreateAttachmentResource;

/**
 * Assembler to transform CreateAttachmentResource to CreateAttachmentCommand
 */
public class CreateAttachmentCommandFromResourceAssembler {

    public static CreateAttachmentCommand toCommandFromResource(CreateAttachmentResource resource) {
        return new CreateAttachmentCommand(
            resource.fileName(),
            resource.mimeType(),
            resource.url(),
            resource.sizeBytes()
        );
    }
}
