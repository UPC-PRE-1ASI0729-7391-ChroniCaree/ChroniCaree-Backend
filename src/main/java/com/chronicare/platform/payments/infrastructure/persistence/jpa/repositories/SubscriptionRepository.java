package com.chronicare.platform.payments.infrastructure.persistence.jpa.repositories;

import com.chronicare.platform.payments.domain.model.aggregates.Subscription;
import com.chronicare.platform.payments.domain.model.valueobjects.PayerType;
import com.chronicare.platform.payments.domain.model.valueobjects.SubscriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    List<Subscription> findByPayerId(Long payerId);
    List<Subscription> findByPayerIdAndPayerType(Long payerId, PayerType payerType);
    Optional<Subscription> findByPayerIdAndPayerTypeAndStatus(Long payerId, PayerType payerType, SubscriptionStatus status);
    List<Subscription> findByStatus(SubscriptionStatus status);
    Optional<Subscription> findByStripeSubscriptionId(String stripeSubscriptionId);
}
