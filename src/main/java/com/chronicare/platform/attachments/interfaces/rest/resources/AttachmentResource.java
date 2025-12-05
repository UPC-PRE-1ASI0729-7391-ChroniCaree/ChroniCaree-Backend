package com.chronicare.platform.attachments.interfaces.rest.resources;

import java.time.LocalDateTime;

/**
 * Resource representation for Attachment
 */
public record AttachmentResource(
    Long id,
    String fileName,
    String mimeType,
    String url,
    Long sizeBytes,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
