package com.chronicare.platform.payments.domain.model.aggregates;

import com.chronicare.platform.payments.domain.model.valueobjects.BillingPeriod;
import com.chronicare.platform.payments.domain.model.valueobjects.PlanType;
import com.chronicare.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "subscription_plans")
public class SubscriptionPlan extends AuditableAbstractAggregateRoot<SubscriptionPlan> {

    @Column(name = "plan_id", nullable = false, unique = true)
    private String planId;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private PlanType type;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private String currency;

    @Enumerated(EnumType.STRING)
    @Column(name = "billing_period", nullable = false)
    private BillingPeriod billingPeriod;

    @Column(columnDefinition = "JSON")
    private String features;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    public SubscriptionPlan() {
    }

    public SubscriptionPlan(String planId, PlanType type, String name, BigDecimal price, String currency, BillingPeriod billingPeriod, String features) {
        this.planId = planId;
        this.type = type;
        this.name = name;
        this.price = price;
        this.currency = currency;
        this.billingPeriod = billingPeriod;
        this.features = features;
        this.isActive = true;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public PlanType getType() {
        return type;
    }

    public void setType(PlanType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BillingPeriod getBillingPeriod() {
        return billingPeriod;
    }

    public void setBillingPeriod(BillingPeriod billingPeriod) {
        this.billingPeriod = billingPeriod;
    }

    public String getFeatures() {
        return features;
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}
