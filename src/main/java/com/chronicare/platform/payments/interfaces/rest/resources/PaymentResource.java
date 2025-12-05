package com.chronicare.platform.payments.interfaces.rest.resources;

import java.math.BigDecimal;

public record PaymentResource(
    Long id,
    Long subscriptionId,
    String payerType,
    Long payerId,
    BigDecimal amount,
    String currency,
    String status,
    String paymentMethod,
    String transactionId,
    String stripePaymentIntentId,
    String createdAt,
    String updatedAt
) {}
