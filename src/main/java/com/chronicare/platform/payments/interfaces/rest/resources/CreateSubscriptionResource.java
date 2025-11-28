package com.chronicare.platform.payments.interfaces.rest.resources;

public record CreateSubscriptionResource(
    Long payerId,
    String payerType,
    Long planId
) {}
