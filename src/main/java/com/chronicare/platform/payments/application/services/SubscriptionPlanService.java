package com.chronicare.platform.payments.application.services;

import com.chronicare.platform.payments.domain.model.aggregates.SubscriptionPlan;
import com.chronicare.platform.payments.domain.model.valueobjects.PlanType;
import com.chronicare.platform.payments.infrastructure.persistence.jpa.repositories.SubscriptionPlanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SubscriptionPlanService {

    private final SubscriptionPlanRepository subscriptionPlanRepository;

    public SubscriptionPlanService(SubscriptionPlanRepository subscriptionPlanRepository) {
        this.subscriptionPlanRepository = subscriptionPlanRepository;
    }

    @Transactional(readOnly = true)
    public List<SubscriptionPlan> getAllActivePlans() {
        return subscriptionPlanRepository.findByIsActiveTrue();
    }

    @Transactional(readOnly = true)
    public List<SubscriptionPlan> getPlansByType(PlanType type) {
        return subscriptionPlanRepository.findByTypeAndIsActiveTrue(type);
    }

    @Transactional(readOnly = true)
    public Optional<SubscriptionPlan> getPlanById(Long id) {
        return subscriptionPlanRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<SubscriptionPlan> getPlanByPlanId(String planId) {
        return subscriptionPlanRepository.findByPlanId(planId);
    }

    @Transactional
    public SubscriptionPlan createPlan(SubscriptionPlan plan) {
        if (subscriptionPlanRepository.existsByPlanId(plan.getPlanId())) {
            throw new IllegalArgumentException("Plan with id " + plan.getPlanId() + " already exists");
        }
        return subscriptionPlanRepository.save(plan);
    }

    @Transactional
    public SubscriptionPlan updatePlan(Long id, SubscriptionPlan updatedPlan) {
        SubscriptionPlan existingPlan = subscriptionPlanRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Plan not found with id: " + id));
        
        existingPlan.setName(updatedPlan.getName());
        existingPlan.setPrice(updatedPlan.getPrice());
        existingPlan.setCurrency(updatedPlan.getCurrency());
        existingPlan.setBillingPeriod(updatedPlan.getBillingPeriod());
        existingPlan.setFeatures(updatedPlan.getFeatures());
        existingPlan.setIsActive(updatedPlan.getIsActive());
        
        return subscriptionPlanRepository.save(existingPlan);
    }

    @Transactional
    public void deactivatePlan(Long id) {
        SubscriptionPlan plan = subscriptionPlanRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Plan not found with id: " + id));
        plan.setIsActive(false);
        subscriptionPlanRepository.save(plan);
    }

    @Transactional
    public SubscriptionPlan saveOrUpdatePlan(SubscriptionPlan plan) {
        Optional<SubscriptionPlan> existingPlan = subscriptionPlanRepository.findByPlanId(plan.getPlanId());
        if (existingPlan.isPresent()) {
            SubscriptionPlan existing = existingPlan.get();
            existing.setName(plan.getName());
            existing.setPrice(plan.getPrice());
            existing.setCurrency(plan.getCurrency());
            existing.setBillingPeriod(plan.getBillingPeriod());
            existing.setFeatures(plan.getFeatures());
            existing.setType(plan.getType());
            existing.setIsActive(plan.getIsActive());
            return subscriptionPlanRepository.save(existing);
        }
        return subscriptionPlanRepository.save(plan);
    }
}
