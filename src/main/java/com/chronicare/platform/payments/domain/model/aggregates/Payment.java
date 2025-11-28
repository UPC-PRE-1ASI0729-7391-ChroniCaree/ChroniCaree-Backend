package com.chronicare.platform.payments.domain.model.aggregates;

import com.chronicare.platform.payments.domain.model.valueobjects.PayerType;
import com.chronicare.platform.payments.domain.model.valueobjects.PaymentMethod;
import com.chronicare.platform.payments.domain.model.valueobjects.PaymentStatus;
import com.chronicare.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "payments")
@Getter
@Setter
public class Payment extends AuditableAbstractAggregateRoot<Payment> {

    @Column(name = "subscription_id", nullable = false)
    private Long subscriptionId;

    @Enumerated(EnumType.STRING)
    @Column(name = "payer_type", nullable = false)
    private PayerType payerType;

    @Column(name = "payer_id", nullable = false)
    private Long payerId;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private String currency;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    private PaymentMethod paymentMethod;

    @Column(name = "transaction_id", nullable = false)
    private String transactionId;

    @Column(name = "stripe_payment_intent_id")
    private String stripePaymentIntentId;

    public Payment() {
    }

    public Payment(Long subscriptionId, PayerType payerType, Long payerId, BigDecimal amount, String currency, PaymentStatus status, PaymentMethod paymentMethod, String transactionId, String stripePaymentIntentId) {
        this.subscriptionId = subscriptionId;
        this.payerType = payerType;
        this.payerId = payerId;
        this.amount = amount;
        this.currency = currency;
        this.status = status;
        this.paymentMethod = paymentMethod;
        this.transactionId = transactionId;
        this.stripePaymentIntentId = stripePaymentIntentId;
    }
}
