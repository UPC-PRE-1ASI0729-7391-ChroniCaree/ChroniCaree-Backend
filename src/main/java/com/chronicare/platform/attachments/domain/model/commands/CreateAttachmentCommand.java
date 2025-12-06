package com.chronicare.platform.attachments.domain.model.commands;

/**
 * Summary: Command to create a new Attachment
 */
public record CreateAttachmentCommand(
    String fileName,
    String mimeType,
    String url,
    Long sizeBytes
) {}
