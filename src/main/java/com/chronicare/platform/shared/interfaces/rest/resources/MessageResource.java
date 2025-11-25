package com.chronicare.platform.shared.interfaces.rest.resources;

/**
 * Message resource
 * @summary
 * This record is used to return simple message responses from REST endpoints
 * @param message The message content
 */
public record MessageResource(String message) {
}
