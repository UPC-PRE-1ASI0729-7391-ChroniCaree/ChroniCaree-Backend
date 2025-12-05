package com.chronicare.platform.shared.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Standard API Error Response
 * @param code Error code for frontend translation (e.g., AUTH_INVALID_CREDENTIALS)
 * @param message Human-readable error message
 * @param details Optional additional error details
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiErrorResponse(
    String code,
    String message,
    Object details
) {
    public ApiErrorResponse(String code, String message) {
        this(code, message, null);
    }
}
