package com.chronicare.platform.payments.interfaces.rest.resources;

/**
 * Resource for creating a subscription.
 * Supports both numeric planId and string planId (e.g., "tenant_professional").
 */
public record CreateSubscriptionResource(
    Long payerId,
    String payerType,
    String planId,  // Can be numeric ID or string plan ID like "tenant_professional"
    String stripeSubscriptionId
) {
    // Constructor for compatibility with requests that don't include stripeSubscriptionId
    public CreateSubscriptionResource(Long payerId, String payerType, String planId) {
        this(payerId, payerType, planId, null);
    }
}
