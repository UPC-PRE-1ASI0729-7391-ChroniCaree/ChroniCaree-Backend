package com.chronicare.platform.records.domain.model.valueobjects;

public enum RecordVisibility {
    PRIVATE,           // Only author
    TEAM,              // Author + care team
    PUBLIC_WITHIN_TENANT // All authorized users in tenant
}
