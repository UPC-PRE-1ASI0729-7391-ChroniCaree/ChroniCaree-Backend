package com.chronicare.platform.payments.infrastructure.persistence.jpa.repositories;

import com.chronicare.platform.payments.domain.model.aggregates.Payment;
import com.chronicare.platform.payments.domain.model.valueobjects.PayerType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findBySubscriptionId(Long subscriptionId);
    
    List<Payment> findByPayerId(Long payerId);
    
    List<Payment> findByPayerIdAndPayerType(Long payerId, PayerType payerType);
    
    Page<Payment> findAllByOrderByIdDesc(Pageable pageable);
    
    Page<Payment> findByPayerIdOrderByIdDesc(Long payerId, Pageable pageable);
}
