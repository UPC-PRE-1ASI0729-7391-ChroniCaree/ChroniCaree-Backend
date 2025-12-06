package com.chronicare.platform.records.interfaces.rest.resources;

import com.chronicare.platform.records.domain.model.valueobjects.RecordType;
import com.chronicare.platform.records.domain.model.valueobjects.RecordVisibility;

import java.util.List;

public record CreateRecordResource(
        Long tenantId,
        Long patientId,
        Long authorId,
        RecordType type,
        String title,
        String content,
        String structuredData,
        List<String> attachments,
        List<String> tags,
        RecordVisibility visibility
) {
}
