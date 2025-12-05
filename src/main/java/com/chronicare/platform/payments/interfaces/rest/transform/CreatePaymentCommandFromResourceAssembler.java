package com.chronicare.platform.payments.interfaces.rest.transform;

import com.chronicare.platform.payments.domain.model.aggregates.Payment;
import com.chronicare.platform.payments.domain.model.valueobjects.PayerType;
import com.chronicare.platform.payments.domain.model.valueobjects.PaymentMethod;
import com.chronicare.platform.payments.domain.model.valueobjects.PaymentStatus;
import com.chronicare.platform.payments.interfaces.rest.resources.CreatePaymentResource;

public class CreatePaymentCommandFromResourceAssembler {
    public static Payment toCommandFromResource(CreatePaymentResource resource) {
        return new Payment(
            resource.subscriptionId(),
            PayerType.valueOf(resource.payerType().toUpperCase()),
            resource.payerId(),
            resource.amount(),
            resource.currency(),
            PaymentStatus.valueOf(resource.status().toUpperCase()),
            PaymentMethod.valueOf(resource.paymentMethod().toUpperCase()),
            resource.transactionId(),
            resource.stripePaymentIntentId()
        );
    }
}
