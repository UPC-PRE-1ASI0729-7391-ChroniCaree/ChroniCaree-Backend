package com.chronicare.platform.payments.interfaces.rest.resources;

import java.time.LocalDateTime;

public record SubscriptionResource(
    Long id,
    Long payerId,
    String payerType,
    Long planId,
    String status,
    String stripeSubscriptionId,
    LocalDateTime startDate,
    LocalDateTime endDate,
    LocalDateTime nextBillingDate
) {}
