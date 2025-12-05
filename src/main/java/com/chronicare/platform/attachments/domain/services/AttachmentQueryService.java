package com.chronicare.platform.attachments.domain.services;

import com.chronicare.platform.attachments.domain.model.aggregates.Attachment;
import com.chronicare.platform.attachments.domain.model.queries.GetAllAttachmentsQuery;
import com.chronicare.platform.attachments.domain.model.queries.GetAttachmentByIdQuery;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for Attachment queries
 */
public interface AttachmentQueryService {
    
    /**
     * Handle get attachment by ID query
     * @param query the get attachment by ID query
     * @return Optional with attachment if found
     */
    Optional<Attachment> handle(GetAttachmentByIdQuery query);
    
    /**
     * Handle get all attachments query
     * @param query the get all attachments query
     * @return List of all attachments
     */
    List<Attachment> handle(GetAllAttachmentsQuery query);
}
