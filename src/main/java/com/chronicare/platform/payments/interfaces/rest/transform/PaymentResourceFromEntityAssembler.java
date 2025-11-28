package com.chronicare.platform.payments.interfaces.rest.transform;

import com.chronicare.platform.payments.domain.model.aggregates.Payment;
import com.chronicare.platform.payments.domain.model.valueobjects.PayerType;
import com.chronicare.platform.payments.domain.model.valueobjects.PaymentMethod;
import com.chronicare.platform.payments.domain.model.valueobjects.PaymentStatus;
import com.chronicare.platform.payments.interfaces.rest.resources.CreatePaymentResource;
import com.chronicare.platform.payments.interfaces.rest.resources.PaymentResource;

public class PaymentResourceFromEntityAssembler {
    public static PaymentResource toResourceFromEntity(Payment entity) {
        return new PaymentResource(
            entity.getId(),
            entity.getSubscriptionId(),
            entity.getPayerType().name().toLowerCase(),
            entity.getPayerId(),
            entity.getAmount(),
            entity.getCurrency(),
            entity.getStatus().name().toLowerCase(),
            entity.getPaymentMethod().name().toLowerCase(),
            entity.getTransactionId(),
            entity.getStripePaymentIntentId(),
            entity.getCreatedAt().toString(),
            entity.getUpdatedAt() != null ? entity.getUpdatedAt().toString() : null
        );
    }
}
