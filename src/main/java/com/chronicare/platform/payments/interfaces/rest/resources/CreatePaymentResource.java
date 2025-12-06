package com.chronicare.platform.payments.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CreatePaymentResource(
    Long subscriptionId,
    String payerType,
    Long payerId,
    BigDecimal amount,
    String currency,
    String status,
    String paymentMethod,
    String transactionId,
    String stripePaymentIntentId
) {}
