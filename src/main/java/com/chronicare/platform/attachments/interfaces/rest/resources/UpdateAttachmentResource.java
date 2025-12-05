package com.chronicare.platform.attachments.interfaces.rest.resources;

/**
 * Resource for updating an Attachment
 */
public record UpdateAttachmentResource(
    String fileName,
    String mimeType,
    String url,
    Long sizeBytes
) {}
