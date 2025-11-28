package com.chronicare.platform.payments.interfaces.rest;

import com.chronicare.platform.payments.application.services.PaymentService;
import com.chronicare.platform.payments.domain.model.aggregates.Payment;
import com.chronicare.platform.payments.domain.model.valueobjects.PaymentStatus;
import com.chronicare.platform.payments.interfaces.rest.resources.CreatePaymentResource;
import com.chronicare.platform.payments.interfaces.rest.resources.PaymentResource;
import com.chronicare.platform.payments.interfaces.rest.transform.CreatePaymentCommandFromResourceAssembler;
import com.chronicare.platform.payments.interfaces.rest.transform.PaymentResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
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
    public ResponseEntity<PaymentResource> createPayment(@RequestBody CreatePaymentResource resource) {
        Payment payment = CreatePaymentCommandFromResourceAssembler.toCommandFromResource(resource);
        Payment createdPayment = paymentService.createPayment(payment);
        return new ResponseEntity<>(PaymentResourceFromEntityAssembler.toResourceFromEntity(createdPayment), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResource> getPaymentById(@PathVariable Long id) {
        return paymentService.getPaymentById(id)
                .map(payment -> new ResponseEntity<>(PaymentResourceFromEntityAssembler.toResourceFromEntity(payment), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<PaymentResource>> getPaymentsBySubscription(@RequestParam Long subscriptionId) {
        List<Payment> payments = paymentService.getPaymentsBySubscription(subscriptionId);
        List<PaymentResource> resources = payments.stream()
                .map(PaymentResourceFromEntityAssembler::toResourceFromEntity)
                .collect(Collectors.toList());
        return new ResponseEntity<>(resources, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PaymentResource> updatePaymentStatus(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) {
        
        String statusStr = (String) updates.get("status");
        String stripePaymentIntentId = (String) updates.get("stripePaymentIntentId");
        
        PaymentStatus status = null;
        if (statusStr != null) {
            status = PaymentStatus.valueOf(statusStr.toUpperCase());
        }

        // Note: This is a simplified update. In a real app, you might want a specific command object.
        // If status is null, we might just be updating the intent ID, but the service method expects status.
        // For now assuming status is always sent if we are updating.
        
        if (status == null) {
             return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Payment updatedPayment = paymentService.updatePaymentStatus(id, status, stripePaymentIntentId);
        return new ResponseEntity<>(PaymentResourceFromEntityAssembler.toResourceFromEntity(updatedPayment), HttpStatus.OK);
    }
}
