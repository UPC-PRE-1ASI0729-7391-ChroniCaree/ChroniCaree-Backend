package com.chronicare.platform.records.interfaces.rest.resources;

import com.chronicare.platform.records.domain.model.valueobjects.RecordVisibility;

public record UpdateRecordResource(
        String title,
        String content,
        String structuredData,
        RecordVisibility visibility
) {
}
