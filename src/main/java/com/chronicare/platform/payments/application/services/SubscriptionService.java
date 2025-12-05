package com.chronicare.platform.payments.application.services;

import com.chronicare.platform.payments.domain.model.aggregates.Subscription;
import com.chronicare.platform.payments.domain.model.aggregates.SubscriptionPlan;
import com.chronicare.platform.payments.domain.model.valueobjects.PayerType;
import com.chronicare.platform.payments.domain.model.valueobjects.PlanType;
import com.chronicare.platform.payments.domain.model.valueobjects.SubscriptionStatus;
import com.chronicare.platform.payments.infrastructure.persistence.jpa.repositories.SubscriptionPlanRepository;
import com.chronicare.platform.payments.infrastructure.persistence.jpa.repositories.SubscriptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionPlanRepository subscriptionPlanRepository;

    public SubscriptionService(SubscriptionRepository subscriptionRepository, 
                               SubscriptionPlanRepository subscriptionPlanRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.subscriptionPlanRepository = subscriptionPlanRepository;
    }

    /**
     * Creates a subscription using a string plan ID (e.g., "tenant_professional")
     * IMPORTANT: Respects the status from the subscription object (set by frontend after payment)
     */
    @Transactional
    public Subscription createSubscriptionWithPlanId(Subscription subscription, String planIdString) {
        // Try to resolve planId - it could be a numeric ID or a string identifier
        SubscriptionPlan plan = resolvePlan(planIdString);
        
        // Validate that plan type matches payer type
        validatePlanTypeMatchesPayerType(plan.getType(), subscription.getPayerType());
        
        // Set the resolved numeric plan ID and store the string plan ID
        subscription.setPlanId(plan.getId());
        subscription.setPlanIdString(plan.getPlanId()); // Store the string plan ID (e.g., "patient_standard")
        
        // Set start date if not already set
        if (subscription.getStartDate() == null) {
            subscription.setStartDate(LocalDateTime.now());
        }
        
        // Set end date if not already set (default 1 month)
        if (subscription.getEndDate() == null) {
            subscription.setEndDate(subscription.getStartDate().plusMonths(1));
        }
        
        // Set next billing date if not already set
        if (subscription.getNextBillingDate() == null) {
            subscription.setNextBillingDate(subscription.getEndDate());
        }
        
        // IMPORTANT: Do NOT override status! The assembler sets it from the frontend request
        // This allows the frontend to create subscriptions with status='active' after successful payment
        // If status is still null for some reason, default to ACTIVE
        if (subscription.getStatus() == null) {
            subscription.setStatus(SubscriptionStatus.ACTIVE);
        }
        
        return subscriptionRepository.save(subscription);
    }

    /**
     * Resolves a plan from either a numeric ID or a string plan ID
     */
    private SubscriptionPlan resolvePlan(String planIdString) {
        // First, try to parse as a numeric ID
        try {
            Long numericId = Long.parseLong(planIdString);
            return subscriptionPlanRepository.findById(numericId)
                    .orElseThrow(() -> new IllegalArgumentException("Plan not found with numeric id: " + numericId));
        } catch (NumberFormatException e) {
            // Not a numeric ID, try to find by string plan ID
            return subscriptionPlanRepository.findByPlanId(planIdString)
                    .orElseThrow(() -> new IllegalArgumentException("Plan not found with plan id: " + planIdString));
        }
    }

    @Transactional
    public Subscription createSubscription(Subscription subscription) {
        // This method expects planId to already be set as a numeric ID
        SubscriptionPlan plan = subscriptionPlanRepository.findById(subscription.getPlanId())
                .orElseThrow(() -> new IllegalArgumentException("Plan not found with id: " + subscription.getPlanId()));
        
        validatePlanTypeMatchesPayerType(plan.getType(), subscription.getPayerType());
        
        subscription.setStartDate(LocalDateTime.now());
        subscription.setStatus(SubscriptionStatus.PENDING);
        return subscriptionRepository.save(subscription);
    }

    private void validatePlanTypeMatchesPayerType(PlanType planType, PayerType payerType) {
        boolean isValid = (planType == PlanType.PATIENT && payerType == PayerType.PATIENT) ||
                          (planType == PlanType.TENANT && payerType == PayerType.TENANT);
        
        if (!isValid) {
            throw new IllegalArgumentException(
                String.format("Plan type %s does not match payer type %s. " +
                              "Patient plans are for patients, Tenant plans are for hospitals/clinics.",
                              planType, payerType));
        }
    }

    @Transactional(readOnly = true)
    public Optional<Subscription> getSubscriptionById(Long id) {
        return subscriptionRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Subscription> getSubscriptionsByPayer(Long payerId, PayerType payerType) {
        return subscriptionRepository.findByPayerIdAndPayerType(payerId, payerType);
    }

    @Transactional(readOnly = true)
    public List<Subscription> getAllSubscriptions() {
        return subscriptionRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Subscription> getActiveSubscription(Long payerId, PayerType payerType) {
        // First try to find an ACTIVE subscription
        Optional<Subscription> active = subscriptionRepository.findByPayerIdAndPayerTypeAndStatus(payerId, payerType, SubscriptionStatus.ACTIVE);
        if (active.isPresent()) {
            return active;
        }
        // If no active subscription, look for PENDING (considered as active by frontend)
        return subscriptionRepository.findByPayerIdAndPayerTypeAndStatus(payerId, payerType, SubscriptionStatus.PENDING);
    }

    /**
     * Get any subscription (active or pending) for a payer
     */
    @Transactional(readOnly = true)
    public Optional<Subscription> getActiveOrPendingSubscription(Long payerId, PayerType payerType) {
        Optional<Subscription> active = subscriptionRepository.findByPayerIdAndPayerTypeAndStatus(payerId, payerType, SubscriptionStatus.ACTIVE);
        if (active.isPresent()) {
            return active;
        }
        return subscriptionRepository.findByPayerIdAndPayerTypeAndStatus(payerId, payerType, SubscriptionStatus.PENDING);
    }

    @Transactional
    public Subscription activateSubscription(Long id) {
        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Subscription not found with id: " + id));
        subscription.activate();
        return subscriptionRepository.save(subscription);
    }

    @Transactional
    public Subscription cancelSubscription(Long id) {
        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Subscription not found with id: " + id));
        subscription.cancel();
        return subscriptionRepository.save(subscription);
    }

    @Transactional
    public Subscription updateSubscriptionStatus(Long id, SubscriptionStatus status) {
        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Subscription not found with id: " + id));
        subscription.setStatus(status);
        return subscriptionRepository.save(subscription);
    }

    @Transactional
    public Subscription updateStripeSubscriptionId(Long id, String stripeSubscriptionId) {
        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Subscription not found with id: " + id));
        subscription.setStripeSubscriptionId(stripeSubscriptionId);
        return subscriptionRepository.save(subscription);
    }

    @Transactional(readOnly = true)
    public Optional<Subscription> getByStripeSubscriptionId(String stripeSubscriptionId) {
        return subscriptionRepository.findByStripeSubscriptionId(stripeSubscriptionId);
    }
}
