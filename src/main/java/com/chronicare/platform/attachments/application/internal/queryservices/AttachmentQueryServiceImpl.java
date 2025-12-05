package com.chronicare.platform.attachments.application.internal.queryservices;

import com.chronicare.platform.attachments.domain.model.aggregates.Attachment;
import com.chronicare.platform.attachments.domain.model.queries.GetAllAttachmentsQuery;
import com.chronicare.platform.attachments.domain.model.queries.GetAttachmentByIdQuery;
import com.chronicare.platform.attachments.domain.services.AttachmentQueryService;
import com.chronicare.platform.attachments.infrastructure.persistence.jpa.repositories.AttachmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of AttachmentQueryService
 */
@Service
@Transactional(readOnly = true)
public class AttachmentQueryServiceImpl implements AttachmentQueryService {

    private final AttachmentRepository attachmentRepository;

    public AttachmentQueryServiceImpl(AttachmentRepository attachmentRepository) {
        this.attachmentRepository = attachmentRepository;
    }

    @Override
    public Optional<Attachment> handle(GetAttachmentByIdQuery query) {
        return attachmentRepository.findById(query.attachmentId());
    }

    @Override
    public List<Attachment> handle(GetAllAttachmentsQuery query) {
        return attachmentRepository.findAll();
    }
}
