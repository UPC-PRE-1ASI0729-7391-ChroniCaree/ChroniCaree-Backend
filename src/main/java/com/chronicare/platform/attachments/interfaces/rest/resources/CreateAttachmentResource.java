package com.chronicare.platform.attachments.interfaces.rest.resources;

/**
 * Resource for creating an Attachment
 */
public record CreateAttachmentResource(
    String fileName,
    String mimeType,
    String url,
    Long sizeBytes
) {}
