package com.chronicare.platform.payments.interfaces.rest.transform;

import com.chronicare.platform.payments.domain.model.aggregates.Subscription;
import com.chronicare.platform.payments.domain.model.valueobjects.PayerType;
import com.chronicare.platform.payments.domain.model.valueobjects.SubscriptionStatus;
import com.chronicare.platform.payments.interfaces.rest.resources.CreateSubscriptionResource;

public class CreateSubscriptionCommandFromResourceAssembler {

    public static Subscription toCommandFromResource(CreateSubscriptionResource resource) {
        PayerType payerType = PayerType.valueOf(resource.payerType().toUpperCase());
        return new Subscription(
            resource.payerId(),
            payerType,
            resource.planId(),
            SubscriptionStatus.PENDING
        );
    }
}
