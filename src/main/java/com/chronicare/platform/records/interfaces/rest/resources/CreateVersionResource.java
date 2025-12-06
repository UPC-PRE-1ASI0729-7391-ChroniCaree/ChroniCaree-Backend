package com.chronicare.platform.records.interfaces.rest.resources;

public record CreateVersionResource(
        String content,
        String structuredData,
        String note
) {
}
