package com.chronicare.platform.payments.interfaces.rest;

import com.chronicare.platform.payments.application.services.PaymentService;
import com.chronicare.platform.payments.domain.model.aggregates.Payment;
import com.chronicare.platform.payments.domain.model.valueobjects.PayerType;
import com.chronicare.platform.payments.domain.model.valueobjects.PaymentStatus;
import com.chronicare.platform.payments.interfaces.rest.resources.CreatePaymentResource;
import com.chronicare.platform.payments.interfaces.rest.resources.PaymentResource;
import com.chronicare.platform.payments.interfaces.rest.transform.CreatePaymentCommandFromResourceAssembler;
import com.chronicare.platform.payments.interfaces.rest.transform.PaymentResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/payments")
@Tag(name = "Payments", description = "Payment Management Endpoints")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    @Operation(summary = "Create a new payment")
    public ResponseEntity<PaymentResource> createPayment(@RequestBody CreatePaymentResource resource) {
        Payment payment = CreatePaymentCommandFromResourceAssembler.toCommandFromResource(resource);
        Payment createdPayment = paymentService.createPayment(payment);
        return new ResponseEntity<>(PaymentResourceFromEntityAssembler.toResourceFromEntity(createdPayment), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get payment by ID")
    public ResponseEntity<PaymentResource> getPaymentById(@PathVariable Long id) {
        return paymentService.getPaymentById(id)
                .map(payment -> new ResponseEntity<>(PaymentResourceFromEntityAssembler.toResourceFromEntity(payment), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    @Operation(summary = "Get payments with optional filters")
    public ResponseEntity<List<PaymentResource>> getPayments(
            @RequestParam(required = false) Long subscriptionId,
            @RequestParam(required = false) Long payerId,
            @RequestParam(required = false) String payerType,
            @RequestParam(required = false, defaultValue = "id") String _sort,
            @RequestParam(required = false, defaultValue = "desc") String _order,
            @RequestParam(required = false, defaultValue = "10") Integer _limit) {
        
        List<Payment> payments;
        
        if (subscriptionId != null) {
            // Filter by subscription
            payments = paymentService.getPaymentsBySubscription(subscriptionId);
        } else if (payerId != null && payerType != null) {
            // Filter by payer and type
            PayerType type = PayerType.valueOf(payerType.toUpperCase());
            payments = paymentService.getPaymentsByPayerAndType(payerId, type);
        } else if (payerId != null) {
            // Filter by payer only
            payments = paymentService.getPaymentsByPayer(payerId);
        } else {
            // Get all with pagination (limited)
            Page<Payment> pagedPayments = paymentService.getAllPayments(0, _limit);
            payments = pagedPayments.getContent();
        }
        
        List<PaymentResource> resources = payments.stream()
                .map(PaymentResourceFromEntityAssembler::toResourceFromEntity)
                .collect(Collectors.toList());
        return new ResponseEntity<>(resources, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update payment status")
    public ResponseEntity<PaymentResource> updatePaymentStatus(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) {
        
        String statusStr = (String) updates.get("status");
        String stripePaymentIntentId = (String) updates.get("stripePaymentIntentId");
        
        PaymentStatus status = null;
        if (statusStr != null) {
            status = PaymentStatus.valueOf(statusStr.toUpperCase());
        }

        if (status == null) {
             return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Payment updatedPayment = paymentService.updatePaymentStatus(id, status, stripePaymentIntentId);
        return new ResponseEntity<>(PaymentResourceFromEntityAssembler.toResourceFromEntity(updatedPayment), HttpStatus.OK);
    }
}
