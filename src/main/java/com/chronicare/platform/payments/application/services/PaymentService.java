package com.chronicare.platform.payments.application.services;

import com.chronicare.platform.payments.domain.model.aggregates.Payment;
import com.chronicare.platform.payments.domain.model.valueobjects.PayerType;
import com.chronicare.platform.payments.domain.model.valueobjects.PaymentStatus;
import com.chronicare.platform.payments.infrastructure.persistence.jpa.repositories.PaymentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Transactional
    public Payment createPayment(Payment payment) {
        return paymentRepository.save(payment);
    }

    @Transactional(readOnly = true)
    public Optional<Payment> getPaymentById(Long id) {
        return paymentRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Payment> getPaymentsBySubscription(Long subscriptionId) {
        return paymentRepository.findBySubscriptionId(subscriptionId);
    }

    @Transactional(readOnly = true)
    public List<Payment> getPaymentsByPayer(Long payerId) {
        return paymentRepository.findByPayerId(payerId);
    }

    @Transactional(readOnly = true)
    public List<Payment> getPaymentsByPayerAndType(Long payerId, PayerType payerType) {
        return paymentRepository.findByPayerIdAndPayerType(payerId, payerType);
    }

    @Transactional(readOnly = true)
    public Page<Payment> getAllPayments(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return paymentRepository.findAllByOrderByIdDesc(pageable);
    }

    @Transactional(readOnly = true)
    public Page<Payment> getPaymentsByPayerPaginated(Long payerId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return paymentRepository.findByPayerIdOrderByIdDesc(payerId, pageable);
    }

    @Transactional
    public Payment updatePaymentStatus(Long id, PaymentStatus status, String stripePaymentIntentId) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found with id: " + id));
        
        payment.setStatus(status);
        if (stripePaymentIntentId != null) {
            payment.setStripePaymentIntentId(stripePaymentIntentId);
        }
        
        return paymentRepository.save(payment);
    }
}
