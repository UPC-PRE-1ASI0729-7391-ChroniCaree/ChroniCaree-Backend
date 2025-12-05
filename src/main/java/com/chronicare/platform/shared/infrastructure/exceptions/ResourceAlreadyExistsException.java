package com.chronicare.platform.shared.infrastructure.exceptions;

/**
 * Exception thrown when trying to create a resource that already exists
 */
public class ResourceAlreadyExistsException extends RuntimeException {
    public ResourceAlreadyExistsException(String message) {
        super(message);
    }

    public ResourceAlreadyExistsException(String resourceName, String field, Object value) {
        super(String.format("%s con %s '%s' ya existe", resourceName, field, value));
    }
}
