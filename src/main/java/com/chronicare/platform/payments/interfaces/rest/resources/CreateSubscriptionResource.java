package com.chronicare.platform.payments.interfaces.rest.resources;

/**
 * Resource for creating a subscription.
 * Supports both numeric planId and string planId (e.g., "tenant_professional").
 */
public record CreateSubscriptionResource(
    Long payerId,
    String payerType,
    String planId,            // String plan ID like "patient_standard", "tenant_professional"
    Long patientId,           // For patient subscriptions
    String status,            // Subscription status: active, pending, trial, etc.
    String startDate,         // ISO 8601 format: 2024-12-05T12:00:00.000Z
    String endDate,           // ISO 8601 format: 2025-01-05T12:00:00.000Z
    Boolean autoRenew,        // Whether to auto-renew (default true)
    String paymentMethod,     // CARD, BANK_TRANSFER, etc.
    String billingEmail,      // Email for billing notifications
    String nextBillingDate,   // ISO 8601 format
    String lastPaymentDate,   // ISO 8601 format
    Double lastPaymentAmount, // Last payment amount
    String stripeSubscriptionId,
    String stripeToken        // Stripe payment token for processing
) {}
