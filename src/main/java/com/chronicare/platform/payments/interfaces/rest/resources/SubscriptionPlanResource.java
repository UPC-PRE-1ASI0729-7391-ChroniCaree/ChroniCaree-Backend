package com.chronicare.platform.payments.interfaces.rest.resources;

public record SubscriptionPlanResource(
    Long id,
    String planId,
    String type,
    String name,
    Double price,
    String currency,
    String billingPeriod,
    Object features
) {}
