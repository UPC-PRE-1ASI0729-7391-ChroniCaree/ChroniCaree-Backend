package com.chronicare.platform.records.interfaces.rest.resources;

import java.time.LocalDateTime;

public record RecordAuditLogResource(
        Long id,
        Long recordId,
        Long userId,
        String action,
        String details,
        String ipAddress,
        LocalDateTime timestamp
) {
}
