package com.chronicare.platform.payments.infrastructure.persistence.jpa.repositories;

import com.chronicare.platform.payments.domain.model.aggregates.SubscriptionPlan;
import com.chronicare.platform.payments.domain.model.valueobjects.PlanType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlan, Long> {
    Optional<SubscriptionPlan> findByPlanId(String planId);
    List<SubscriptionPlan> findByType(PlanType type);
    List<SubscriptionPlan> findByIsActiveTrue();
    List<SubscriptionPlan> findByTypeAndIsActiveTrue(PlanType type);
    boolean existsByPlanId(String planId);
}
