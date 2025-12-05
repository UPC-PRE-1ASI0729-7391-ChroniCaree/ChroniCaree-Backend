package com.chronicare.platform.payments.interfaces.rest.transform;

import com.chronicare.platform.payments.domain.model.aggregates.Subscription;
import com.chronicare.platform.payments.interfaces.rest.resources.SubscriptionResource;

public class SubscriptionResourceFromEntityAssembler {

    public static SubscriptionResource toResourceFromEntity(Subscription subscription) {
        return new SubscriptionResource(
            subscription.getId(),
            subscription.getPayerId(),
            subscription.getPayerType().name().toLowerCase(),
            subscription.getPatientId(),
            subscription.getPlanIdString(),        // String plan ID like "patient_standard"
            subscription.getPlanId(),              // Numeric plan ID
            subscription.getStatus().name().toLowerCase(),
            subscription.getStripeSubscriptionId(),
            subscription.getStartDate(),
            subscription.getEndDate(),
            subscription.getNextBillingDate(),
            subscription.getAutoRenew(),
            subscription.getPaymentMethod() != null ? subscription.getPaymentMethod().name() : null,
            subscription.getBillingEmail(),
            subscription.getLastPaymentDate(),
            subscription.getLastPaymentAmount()
        );
    }
}
