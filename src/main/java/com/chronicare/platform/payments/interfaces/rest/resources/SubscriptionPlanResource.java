package com.chronicare.platform.payments.interfaces.rest.resources;

/**
 * Resource for subscription plans.
 * Note: The 'id' field contains the string plan ID (e.g., "tenant_professional")
 * to match frontend expectations. The numeric database ID is available in 'numericId'.
 */
public record SubscriptionPlanResource(
    String id,              // String plan ID like "tenant_professional" (frontend expects this)
    Long numericId,         // Numeric database ID for internal use
    String type,
    String name,
    Double price,
    String currency,
    String billingPeriod,
    Object features
) {}
