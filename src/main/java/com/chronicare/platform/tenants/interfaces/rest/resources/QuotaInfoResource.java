package com.chronicare.platform.tenants.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

/**
 * Quota Info Resource for REST API responses
 */
public record QuotaInfoResource(
    @JsonProperty("used") Integer used,
    @JsonProperty("max") Integer max,
    @JsonProperty("percentage") BigDecimal percentage
) {
}
