package com.chronicare.platform.attachments.application.internal.commandservices;

import com.chronicare.platform.attachments.domain.model.aggregates.Attachment;
import com.chronicare.platform.attachments.domain.model.commands.CreateAttachmentCommand;
import com.chronicare.platform.attachments.domain.model.commands.DeleteAttachmentCommand;
import com.chronicare.platform.attachments.domain.model.commands.UpdateAttachmentCommand;
import com.chronicare.platform.attachments.domain.services.AttachmentCommandService;
import com.chronicare.platform.attachments.infrastructure.persistence.jpa.repositories.AttachmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Summary: Implementation of AttachmentCommandService
 */
@Service
@Transactional
public class AttachmentCommandServiceImpl implements AttachmentCommandService {

    private final AttachmentRepository attachmentRepository;

    public AttachmentCommandServiceImpl(AttachmentRepository attachmentRepository) {
        this.attachmentRepository = attachmentRepository;
    }

    @Override
    public Optional<Attachment> handle(CreateAttachmentCommand command) {
        var attachment = new Attachment(command);
        var savedAttachment = attachmentRepository.save(attachment);
        return Optional.of(savedAttachment);
    }

    @Override
    public Optional<Attachment> handle(UpdateAttachmentCommand command) {
        var attachmentOptional = attachmentRepository.findById(command.attachmentId());
        if (attachmentOptional.isEmpty()) {
            return Optional.empty();
        }
        var attachment = attachmentOptional.get();
        attachment.updateFromCommand(command);
        var updatedAttachment = attachmentRepository.save(attachment);
        return Optional.of(updatedAttachment);
    }

    @Override
    public void handle(DeleteAttachmentCommand command) {
        if (!attachmentRepository.existsById(command.attachmentId())) {
            throw new IllegalArgumentException("Attachment not found with id: " + command.attachmentId());
        }
        attachmentRepository.deleteById(command.attachmentId());
    }
}
