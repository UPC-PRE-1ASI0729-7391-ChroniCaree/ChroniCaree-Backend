package com.chronicare.platform.iam.domain.model.valueobjects;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Email Address value object
 * @summary Encapsulates email validation logic
 */
@Embeddable
public record EmailAddress(
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    String address
) {
    public EmailAddress {
        if (address == null || address.isBlank()) {
            throw new IllegalArgumentException("Email address cannot be null or empty");
        }
    }

    public EmailAddress() {
        this("");
    }
}
