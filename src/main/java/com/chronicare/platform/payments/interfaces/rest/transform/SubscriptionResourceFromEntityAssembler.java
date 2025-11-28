package com.chronicare.platform.payments.interfaces.rest.transform;

import com.chronicare.platform.payments.domain.model.aggregates.Subscription;
import com.chronicare.platform.payments.interfaces.rest.resources.SubscriptionResource;

public class SubscriptionResourceFromEntityAssembler {

    public static SubscriptionResource toResourceFromEntity(Subscription subscription) {
        return new SubscriptionResource(
            subscription.getId(),
            subscription.getPayerId(),
            subscription.getPayerType().name().toLowerCase(),
            subscription.getPlanId(),
            subscription.getStatus().name(),
            subscription.getStripeSubscriptionId(),
            subscription.getStartDate(),
            subscription.getEndDate(),
            subscription.getNextBillingDate()
        );
    }
}
