package com.chronicare.platform.payments.interfaces.rest;

import com.chronicare.platform.payments.application.services.SubscriptionService;
import com.chronicare.platform.payments.application.services.SubscriptionPlanService;
import com.chronicare.platform.payments.domain.model.aggregates.Subscription;
import com.chronicare.platform.payments.domain.model.aggregates.SubscriptionPlan;
import com.chronicare.platform.payments.domain.model.valueobjects.PayerType;
import com.chronicare.platform.payments.domain.model.valueobjects.SubscriptionStatus;
import com.chronicare.platform.payments.interfaces.rest.resources.CreateSubscriptionResource;
import com.chronicare.platform.payments.interfaces.rest.resources.SubscriptionResource;
import com.chronicare.platform.payments.interfaces.rest.resources.SubscriptionPlanResource;
import com.chronicare.platform.payments.interfaces.rest.transform.CreateSubscriptionCommandFromResourceAssembler;
import com.chronicare.platform.payments.interfaces.rest.transform.SubscriptionResourceFromEntityAssembler;
import com.chronicare.platform.payments.interfaces.rest.transform.SubscriptionPlanResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/subscriptions")
@Tag(name = "Subscriptions", description = "Subscription Management Endpoints")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;
    private final SubscriptionPlanService subscriptionPlanService;

    public SubscriptionController(SubscriptionService subscriptionService, SubscriptionPlanService subscriptionPlanService) {
        this.subscriptionService = subscriptionService;
        this.subscriptionPlanService = subscriptionPlanService;
    }

    /**
     * Response that includes subscription with embedded plan details
     */
    public record SubscriptionWithPlanResponse(
        Long id,
        Long payerId,
        String payerType,
        Long patientId,
        String planId,          // String plan ID like "patient_standard"
        Long planIdNumeric,     // Numeric plan ID
        String status,
        String stripeSubscriptionId,
        String startDate,
        String endDate,
        String nextBillingDate,
        Boolean autoRenew,
        String paymentMethod,
        String billingEmail,
        String lastPaymentDate,
        String lastPaymentAmount,
        SubscriptionPlanResource plan
    ) {}

    @PostMapping
    @Operation(summary = "Create subscription", description = "Creates a new subscription for a payer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Subscription created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<SubscriptionResource> createSubscription(@RequestBody CreateSubscriptionResource resource) {
        try {
            Subscription subscription = CreateSubscriptionCommandFromResourceAssembler.toCommandFromResource(resource);
            String planIdString = CreateSubscriptionCommandFromResourceAssembler.extractPlanId(resource);
            
            Subscription createdSubscription = subscriptionService.createSubscriptionWithPlanId(subscription, planIdString);
            return new ResponseEntity<>(
                    SubscriptionResourceFromEntityAssembler.toResourceFromEntity(createdSubscription), 
                    HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get subscription by ID", description = "Retrieves a subscription by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subscription retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Subscription not found")
    })
    public ResponseEntity<SubscriptionResource> getSubscriptionById(@PathVariable Long id) {
        return subscriptionService.getSubscriptionById(id)
                .map(subscription -> new ResponseEntity<>(
                        SubscriptionResourceFromEntityAssembler.toResourceFromEntity(subscription), 
                        HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    @Operation(summary = "Get subscriptions", description = "Retrieves subscriptions with optional filtering and pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subscriptions retrieved successfully")
    })
    public ResponseEntity<List<SubscriptionResource>> getSubscriptions(
            @RequestParam(required = false) Long payerId,
            @RequestParam(required = false) String payerType,
            @RequestParam(required = false) String status,
            @RequestParam(name = "_sort", required = false) String sort,
            @RequestParam(name = "_order", required = false) String order,
            @RequestParam(name = "_limit", required = false) Integer limit) {
        
        // If payerId and payerType are provided, filter by payer
        if (payerId != null && payerType != null) {
            PayerType type = PayerType.valueOf(payerType.toUpperCase());
            List<Subscription> subscriptions = subscriptionService.getSubscriptionsByPayer(payerId, type);
            
            // Apply status filter if provided
            if (status != null && !status.isEmpty()) {
                SubscriptionStatus statusEnum = SubscriptionStatus.valueOf(status.toUpperCase());
                subscriptions = subscriptions.stream()
                        .filter(s -> s.getStatus() == statusEnum)
                        .toList();
            }
            
            List<SubscriptionResource> resources = subscriptions.stream()
                    .map(SubscriptionResourceFromEntityAssembler::toResourceFromEntity)
                    .toList();
            return new ResponseEntity<>(resources, HttpStatus.OK);
        }
        
        // Otherwise, return all subscriptions (with optional pagination support)
        List<Subscription> subscriptions = subscriptionService.getAllSubscriptions();
        
        // Apply status filter if provided
        if (status != null && !status.isEmpty()) {
            SubscriptionStatus statusEnum = SubscriptionStatus.valueOf(status.toUpperCase());
            subscriptions = subscriptions.stream()
                    .filter(s -> s.getStatus() == statusEnum)
                    .toList();
        }
        
        List<SubscriptionResource> resources = new java.util.ArrayList<>(subscriptions.stream()
                .map(SubscriptionResourceFromEntityAssembler::toResourceFromEntity)
                .toList());
        
        // Apply sorting if requested (currently only supports sorting by id)
        if (sort != null && order != null && "id".equals(sort)) {
            resources.sort((a, b) -> {
                int comparison = Long.compare(a.id(), b.id());
                return "desc".equalsIgnoreCase(order) ? -comparison : comparison;
            });
        }
        
        // Apply limit if requested
        if (limit != null && limit > 0) {
            resources = resources.stream().limit(limit).toList();
        }
        
        return new ResponseEntity<>(resources, HttpStatus.OK);
    }

    @GetMapping("/active")
    @Operation(summary = "Get active subscription", description = "Gets the active subscription for a payer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Active subscription found"),
            @ApiResponse(responseCode = "404", description = "No active subscription found")
    })
    public ResponseEntity<SubscriptionResource> getActiveSubscription(
            @RequestParam Long payerId,
            @RequestParam String payerType) {
        PayerType type = PayerType.valueOf(payerType.toUpperCase());
        return subscriptionService.getActiveSubscription(payerId, type)
                .map(subscription -> new ResponseEntity<>(
                        SubscriptionResourceFromEntityAssembler.toResourceFromEntity(subscription), 
                        HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update subscription status", description = "Updates the status of a subscription")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subscription updated successfully"),
            @ApiResponse(responseCode = "404", description = "Subscription not found")
    })
    public ResponseEntity<SubscriptionResource> updateSubscriptionStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> updates) {
        String statusStr = updates.get("status");
        if (statusStr == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        
        SubscriptionStatus status = SubscriptionStatus.valueOf(statusStr.toUpperCase());
        Subscription updatedSubscription = subscriptionService.updateSubscriptionStatus(id, status);
        return new ResponseEntity<>(
                SubscriptionResourceFromEntityAssembler.toResourceFromEntity(updatedSubscription), 
                HttpStatus.OK);
    }

    @PostMapping("/{id}/cancel")
    @Operation(summary = "Cancel subscription", description = "Cancels a subscription")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subscription cancelled successfully"),
            @ApiResponse(responseCode = "404", description = "Subscription not found")
    })
    public ResponseEntity<SubscriptionResource> cancelSubscription(@PathVariable Long id) {
        Subscription cancelledSubscription = subscriptionService.cancelSubscription(id);
        return new ResponseEntity<>(
                SubscriptionResourceFromEntityAssembler.toResourceFromEntity(cancelledSubscription), 
                HttpStatus.OK);
    }

    @PostMapping("/{id}/activate")
    @Operation(summary = "Activate subscription", description = "Activates a subscription")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subscription activated successfully"),
            @ApiResponse(responseCode = "404", description = "Subscription not found")
    })
    public ResponseEntity<SubscriptionResource> activateSubscription(@PathVariable Long id) {
        Subscription activatedSubscription = subscriptionService.activateSubscription(id);
        return new ResponseEntity<>(
                SubscriptionResourceFromEntityAssembler.toResourceFromEntity(activatedSubscription), 
                HttpStatus.OK);
    }

    @GetMapping("/{id}/with-plan")
    @Operation(summary = "Get subscription with plan details", description = "Retrieves a subscription by ID with embedded plan information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subscription with plan retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Subscription not found")
    })
    public ResponseEntity<SubscriptionWithPlanResponse> getSubscriptionWithPlan(@PathVariable Long id) {
        return subscriptionService.getSubscriptionById(id)
                .map(subscription -> {
                    SubscriptionPlanResource planResource = null;
                    if (subscription.getPlanId() != null) {
                        planResource = subscriptionPlanService.getPlanById(subscription.getPlanId())
                                .map(SubscriptionPlanResourceFromEntityAssembler::toResourceFromEntity)
                                .orElse(null);
                    }
                    
                    SubscriptionWithPlanResponse response = new SubscriptionWithPlanResponse(
                        subscription.getId(),
                        subscription.getPayerId(),
                        subscription.getPayerType().name().toLowerCase(),
                        subscription.getPatientId(),
                        subscription.getPlanIdString(),
                        subscription.getPlanId(),
                        subscription.getStatus().name().toLowerCase(),
                        subscription.getStripeSubscriptionId(),
                        subscription.getStartDate() != null ? subscription.getStartDate().toString() : null,
                        subscription.getEndDate() != null ? subscription.getEndDate().toString() : null,
                        subscription.getNextBillingDate() != null ? subscription.getNextBillingDate().toString() : null,
                        subscription.getAutoRenew(),
                        subscription.getPaymentMethod() != null ? subscription.getPaymentMethod().name() : null,
                        subscription.getBillingEmail(),
                        subscription.getLastPaymentDate() != null ? subscription.getLastPaymentDate().toString() : null,
                        subscription.getLastPaymentAmount() != null ? subscription.getLastPaymentAmount().toString() : null,
                        planResource
                    );
                    return new ResponseEntity<>(response, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/active/with-plan")
    @Operation(summary = "Get active subscription with plan details", description = "Gets the active subscription for a payer with embedded plan information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Active subscription with plan found"),
            @ApiResponse(responseCode = "404", description = "No active subscription found")
    })
    public ResponseEntity<SubscriptionWithPlanResponse> getActiveSubscriptionWithPlan(
            @RequestParam Long payerId,
            @RequestParam String payerType) {
        PayerType type = PayerType.valueOf(payerType.toUpperCase());
        return subscriptionService.getActiveSubscription(payerId, type)
                .map(subscription -> {
                    SubscriptionPlanResource planResource = null;
                    if (subscription.getPlanId() != null) {
                        planResource = subscriptionPlanService.getPlanById(subscription.getPlanId())
                                .map(SubscriptionPlanResourceFromEntityAssembler::toResourceFromEntity)
                                .orElse(null);
                    }
                    
                    SubscriptionWithPlanResponse response = new SubscriptionWithPlanResponse(
                        subscription.getId(),
                        subscription.getPayerId(),
                        subscription.getPayerType().name().toLowerCase(),
                        subscription.getPatientId(),
                        subscription.getPlanIdString(),
                        subscription.getPlanId(),
                        subscription.getStatus().name().toLowerCase(),
                        subscription.getStripeSubscriptionId(),
                        subscription.getStartDate() != null ? subscription.getStartDate().toString() : null,
                        subscription.getEndDate() != null ? subscription.getEndDate().toString() : null,
                        subscription.getNextBillingDate() != null ? subscription.getNextBillingDate().toString() : null,
                        subscription.getAutoRenew(),
                        subscription.getPaymentMethod() != null ? subscription.getPaymentMethod().name() : null,
                        subscription.getBillingEmail(),
                        subscription.getLastPaymentDate() != null ? subscription.getLastPaymentDate().toString() : null,
                        subscription.getLastPaymentAmount() != null ? subscription.getLastPaymentAmount().toString() : null,
                        planResource
                    );
                    return new ResponseEntity<>(response, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
