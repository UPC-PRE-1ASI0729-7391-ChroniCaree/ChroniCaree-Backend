package com.chronicare.platform.attachments.domain.services;

import com.chronicare.platform.attachments.domain.model.aggregates.Attachment;
import com.chronicare.platform.attachments.domain.model.commands.CreateAttachmentCommand;
import com.chronicare.platform.attachments.domain.model.commands.DeleteAttachmentCommand;
import com.chronicare.platform.attachments.domain.model.commands.UpdateAttachmentCommand;

import java.util.Optional;

/**
 * Service interface for Attachment commands
 */
public interface AttachmentCommandService {
    
    /**
     * Handle create attachment command
     * @param command the create attachment command
     * @return Optional with created attachment
     */
    Optional<Attachment> handle(CreateAttachmentCommand command);
    
    /**
     * Handle update attachment command
     * @param command the update attachment command
     * @return Optional with updated attachment
     */
    Optional<Attachment> handle(UpdateAttachmentCommand command);
    
    /**
     * Handle delete attachment command
     * @param command the delete attachment command
     */
    void handle(DeleteAttachmentCommand command);
}
