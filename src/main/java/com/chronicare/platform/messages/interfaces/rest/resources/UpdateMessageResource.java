package com.chronicare.platform.messages.interfaces.rest.resources;

/**
 * Resource for updating an existing Message
 */
public record UpdateMessageResource(
        String body,
        String subject,
        boolean isUrgent
) {
}
