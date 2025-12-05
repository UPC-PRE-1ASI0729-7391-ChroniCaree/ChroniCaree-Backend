package com.chronicare.platform.attachments.infrastructure.persistence.jpa.repositories;

import com.chronicare.platform.attachments.domain.model.aggregates.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * JPA Repository for Attachment aggregate
 */
@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    
    /**
     * Find attachments by file name containing
     */
    List<Attachment> findByFileNameContaining(String fileName);
    
    /**
     * Find attachments by mime type
     */
    List<Attachment> findByMimeType(String mimeType);
}
