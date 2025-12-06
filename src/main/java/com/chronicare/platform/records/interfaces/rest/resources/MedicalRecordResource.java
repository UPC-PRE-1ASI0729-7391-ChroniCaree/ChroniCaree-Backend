package com.chronicare.platform.records.interfaces.rest.resources;

import com.chronicare.platform.records.domain.model.valueobjects.RecordType;
import com.chronicare.platform.records.domain.model.valueobjects.RecordVisibility;

import java.time.LocalDateTime;
import java.util.List;

public record MedicalRecordResource(
        Long id,
        Long tenantId,
        Long patientId,
        Long authorId,
        RecordType type,
        String title,
        String content,
        String structuredData,
        List<String> attachments,
        List<String> tags,
        RecordVisibility visibility,
        Integer version,
        Long parentRecordId,
        Boolean isDeleted,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
