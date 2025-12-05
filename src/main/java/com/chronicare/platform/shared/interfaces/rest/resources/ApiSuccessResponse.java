package com.chronicare.platform.shared.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Standard API Success Response wrapper
 * @param code Success code for frontend (e.g., AUTH_SIGNIN_SUCCESS)
 * @param message Human-readable success message
 * @param data The response payload
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiSuccessResponse<T>(
    String code,
    String message,
    T data
) {
    public ApiSuccessResponse(T data) {
        this(null, null, data);
    }
}
