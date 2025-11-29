package com.chronicare.platform.payments.interfaces.rest.transform;

import com.chronicare.platform.payments.domain.model.aggregates.Subscription;
import com.chronicare.platform.payments.domain.model.valueobjects.PayerType;
import com.chronicare.platform.payments.domain.model.valueobjects.SubscriptionStatus;
import com.chronicare.platform.payments.interfaces.rest.resources.CreateSubscriptionResource;

public class CreateSubscriptionCommandFromResourceAssembler {

    /**
     * Converts a CreateSubscriptionResource to a Subscription entity.
     * Note: The planId in the resource can be either a numeric ID or a string plan ID.
     * The service layer will handle resolving the string plan ID to the actual entity.
     */
    public static Subscription toCommandFromResource(CreateSubscriptionResource resource) {
        PayerType payerType = PayerType.valueOf(resource.payerType().toUpperCase());
        
        Subscription subscription = new Subscription();
        subscription.setPayerId(resource.payerId());
        subscription.setPayerType(payerType);
        subscription.setStatus(SubscriptionStatus.PENDING);
        
        if (resource.stripeSubscriptionId() != null) {
            subscription.setStripeSubscriptionId(resource.stripeSubscriptionId());
        }
        
        return subscription;
    }
    
    /**
     * Extracts the plan ID from the resource.
     * Returns the string planId that can be used to look up the plan.
     */
    public static String extractPlanId(CreateSubscriptionResource resource) {
        return resource.planId();
    }
}
