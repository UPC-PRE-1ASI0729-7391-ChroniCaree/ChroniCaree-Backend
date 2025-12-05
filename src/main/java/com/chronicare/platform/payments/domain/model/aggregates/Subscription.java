package com.chronicare.platform.payments.domain.model.aggregates;

import com.chronicare.platform.payments.domain.model.valueobjects.PayerType;
import com.chronicare.platform.payments.domain.model.valueobjects.PaymentMethod;
import com.chronicare.platform.payments.domain.model.valueobjects.SubscriptionStatus;
import com.chronicare.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "subscriptions")
public class Subscription extends AuditableAbstractAggregateRoot<Subscription> {

    @Column(name = "payer_id", nullable = false)
    private Long payerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "payer_type", nullable = false)
    private PayerType payerType;

    // For patient subscriptions, this stores the patient ID
    @Column(name = "patient_id")
    private Long patientId;

    // String plan ID like "patient_standard", "tenant_professional"
    @Column(name = "plan_id_string")
    private String planIdString;

    // Numeric plan ID reference (for database FK if needed)
    @Column(name = "plan_id")
    private Long planId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubscriptionStatus status;

    @Column(name = "stripe_subscription_id")
    private String stripeSubscriptionId;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "next_billing_date")
    private LocalDateTime nextBillingDate;

    @Column(name = "auto_renew")
    private Boolean autoRenew = true;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;

    @Column(name = "billing_email")
    private String billingEmail;

    @Column(name = "last_payment_date")
    private LocalDateTime lastPaymentDate;

    @Column(name = "last_payment_amount")
    private BigDecimal lastPaymentAmount;

    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

    public Subscription() {
    }

    public Subscription(Long payerId, PayerType payerType, Long planId, SubscriptionStatus status) {
        this.payerId = payerId;
        this.payerType = payerType;
        this.planId = planId;
        this.status = status;
        this.startDate = LocalDateTime.now();
        this.autoRenew = true;
    }

    public Subscription(Long payerId, PayerType payerType, String planIdString, SubscriptionStatus status,
                        Long patientId, Boolean autoRenew, PaymentMethod paymentMethod, String billingEmail) {
        this.payerId = payerId;
        this.payerType = payerType;
        this.planIdString = planIdString;
        this.status = status;
        this.patientId = patientId;
        this.autoRenew = autoRenew != null ? autoRenew : true;
        this.paymentMethod = paymentMethod;
        this.billingEmail = billingEmail;
        this.startDate = LocalDateTime.now();
        this.endDate = this.startDate.plusMonths(1);
        this.nextBillingDate = this.endDate;
    }

    public Long getPayerId() {
        return payerId;
    }

    public void setPayerId(Long payerId) {
        this.payerId = payerId;
    }

    public PayerType getPayerType() {
        return payerType;
    }

    public void setPayerType(PayerType payerType) {
        this.payerType = payerType;
    }

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    public SubscriptionStatus getStatus() {
        return status;
    }

    public void setStatus(SubscriptionStatus status) {
        this.status = status;
    }

    public String getStripeSubscriptionId() {
        return stripeSubscriptionId;
    }

    public void setStripeSubscriptionId(String stripeSubscriptionId) {
        this.stripeSubscriptionId = stripeSubscriptionId;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public LocalDateTime getNextBillingDate() {
        return nextBillingDate;
    }

    public void setNextBillingDate(LocalDateTime nextBillingDate) {
        this.nextBillingDate = nextBillingDate;
    }

    public LocalDateTime getCancelledAt() {
        return cancelledAt;
    }

    public void setCancelledAt(LocalDateTime cancelledAt) {
        this.cancelledAt = cancelledAt;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public String getPlanIdString() {
        return planIdString;
    }

    public void setPlanIdString(String planIdString) {
        this.planIdString = planIdString;
    }

    public Boolean getAutoRenew() {
        return autoRenew;
    }

    public void setAutoRenew(Boolean autoRenew) {
        this.autoRenew = autoRenew;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getBillingEmail() {
        return billingEmail;
    }

    public void setBillingEmail(String billingEmail) {
        this.billingEmail = billingEmail;
    }

    public LocalDateTime getLastPaymentDate() {
        return lastPaymentDate;
    }

    public void setLastPaymentDate(LocalDateTime lastPaymentDate) {
        this.lastPaymentDate = lastPaymentDate;
    }

    public BigDecimal getLastPaymentAmount() {
        return lastPaymentAmount;
    }

    public void setLastPaymentAmount(BigDecimal lastPaymentAmount) {
        this.lastPaymentAmount = lastPaymentAmount;
    }

    public void cancel() {
        this.status = SubscriptionStatus.CANCELLED;
        this.cancelledAt = LocalDateTime.now();
    }

    public void activate() {
        this.status = SubscriptionStatus.ACTIVE;
    }

    public void suspend() {
        this.status = SubscriptionStatus.SUSPENDED;
    }
}
