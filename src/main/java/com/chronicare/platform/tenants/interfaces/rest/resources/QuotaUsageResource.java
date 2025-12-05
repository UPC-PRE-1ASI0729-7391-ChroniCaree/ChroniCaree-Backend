package com.chronicare.platform.tenants.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Quota Usage Resource for REST API responses
 */
public record QuotaUsageResource(
    @JsonProperty("doctors") QuotaInfoResource doctors,
    @JsonProperty("patients") QuotaInfoResource patients
) {
}
