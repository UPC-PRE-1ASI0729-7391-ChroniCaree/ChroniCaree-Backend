package com.chronicare.platform.payments.interfaces.rest.resources;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SubscriptionResource(
    Long id,
    Long payerId,
    String payerType,
    Long patientId,
    String planId,          // String plan ID like "patient_standard"
    Long planIdNumeric,     // Numeric plan ID for FK reference
    String status,
    String stripeSubscriptionId,
    LocalDateTime startDate,
    LocalDateTime endDate,
    LocalDateTime nextBillingDate,
    Boolean autoRenew,
    String paymentMethod,
    String billingEmail,
    LocalDateTime lastPaymentDate,
    BigDecimal lastPaymentAmount
) {}
