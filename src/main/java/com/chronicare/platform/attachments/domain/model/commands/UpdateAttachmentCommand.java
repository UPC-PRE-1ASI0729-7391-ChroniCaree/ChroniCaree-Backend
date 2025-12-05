package com.chronicare.platform.attachments.domain.model.commands;

/**
 * Command to update an existing Attachment
 */
public record UpdateAttachmentCommand(
    Long attachmentId,
    String fileName,
    String mimeType,
    String url,
    Long sizeBytes
) {}
