package com.chronicare.platform.payments.interfaces.rest;

import com.chronicare.platform.payments.application.services.SubscriptionPlanService;
import com.chronicare.platform.payments.domain.model.aggregates.SubscriptionPlan;
import com.chronicare.platform.payments.domain.model.valueobjects.PlanType;
import com.chronicare.platform.payments.interfaces.rest.resources.SubscriptionPlanResource;
import com.chronicare.platform.payments.interfaces.rest.transform.SubscriptionPlanResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/subscriptionPlans")
@Tag(name = "Subscription Plans", description = "Subscription Plans Management Endpoints")
public class SubscriptionPlanController {

    private final SubscriptionPlanService subscriptionPlanService;

    public SubscriptionPlanController(SubscriptionPlanService subscriptionPlanService) {
        this.subscriptionPlanService = subscriptionPlanService;
    }

    @GetMapping
    @Operation(summary = "Get all subscription plans", description = "Retrieves all active subscription plans")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Plans retrieved successfully")
    })
    public ResponseEntity<List<SubscriptionPlanResource>> getAllPlans(
            @RequestParam(required = false) String type) {
        List<SubscriptionPlan> plans;
        
        if (type != null && !type.isEmpty()) {
            PlanType planType = PlanType.valueOf(type.toUpperCase());
            plans = subscriptionPlanService.getPlansByType(planType);
        } else {
            plans = subscriptionPlanService.getAllActivePlans();
        }
        
        List<SubscriptionPlanResource> resources = plans.stream()
                .map(SubscriptionPlanResourceFromEntityAssembler::toResourceFromEntity)
                .collect(Collectors.toList());
        
        return new ResponseEntity<>(resources, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get subscription plan by ID", description = "Retrieves a subscription plan by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Plan retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Plan not found")
    })
    public ResponseEntity<SubscriptionPlanResource> getPlanById(@PathVariable Long id) {
        return subscriptionPlanService.getPlanById(id)
                .map(plan -> new ResponseEntity<>(
                        SubscriptionPlanResourceFromEntityAssembler.toResourceFromEntity(plan), 
                        HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/by-plan-id/{planId}")
    @Operation(summary = "Get subscription plan by plan ID", description = "Retrieves a subscription plan by its plan ID (e.g., 'patient_free')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Plan retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Plan not found")
    })
    public ResponseEntity<SubscriptionPlanResource> getPlanByPlanId(@PathVariable String planId) {
        return subscriptionPlanService.getPlanByPlanId(planId)
                .map(plan -> new ResponseEntity<>(
                        SubscriptionPlanResourceFromEntityAssembler.toResourceFromEntity(plan), 
                        HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
