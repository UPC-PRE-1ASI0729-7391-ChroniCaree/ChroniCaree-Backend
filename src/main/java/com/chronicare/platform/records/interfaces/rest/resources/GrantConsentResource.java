package com.chronicare.platform.records.interfaces.rest.resources;

import java.time.LocalDateTime;

public record GrantConsentResource(
        Long recordId,
        Long patientId,
        Long grantedToUserId,
        LocalDateTime expiresAt,
        String purpose
) {
}
