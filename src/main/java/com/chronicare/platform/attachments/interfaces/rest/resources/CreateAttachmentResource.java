package com.chronicare.platform.attachments.interfaces.rest.resources;

/**
 * Summary: Resource for creating an Attachment
 */
public record CreateAttachmentResource(
    String fileName,
    String mimeType,
    String url,
    Long sizeBytes
) {}
