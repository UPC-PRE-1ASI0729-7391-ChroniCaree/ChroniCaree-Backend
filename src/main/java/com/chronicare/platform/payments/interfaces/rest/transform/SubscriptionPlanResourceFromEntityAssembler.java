package com.chronicare.platform.payments.interfaces.rest.transform;

import com.chronicare.platform.payments.domain.model.aggregates.SubscriptionPlan;
import com.chronicare.platform.payments.interfaces.rest.resources.SubscriptionPlanResource;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SubscriptionPlanResourceFromEntityAssembler {
    
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static SubscriptionPlanResource toResourceFromEntity(SubscriptionPlan plan) {
        Object features = null;
        if (plan.getFeatures() != null) {
            try {
                features = objectMapper.readValue(plan.getFeatures(), Object.class);
            } catch (JsonProcessingException e) {
                features = plan.getFeatures();
            }
        }
        
        return new SubscriptionPlanResource(
            plan.getId(),
            plan.getPlanId(),
            plan.getType().name().toLowerCase(),
            plan.getName(),
            plan.getPrice().doubleValue(),
            plan.getCurrency(),
            plan.getBillingPeriod().name().toLowerCase(),
            features
        );
    }
}
