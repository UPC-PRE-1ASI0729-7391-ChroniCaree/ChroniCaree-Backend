package com.chronicare.platform.payments.interfaces.rest.transform;

import com.chronicare.platform.payments.domain.model.aggregates.Subscription;
import com.chronicare.platform.payments.domain.model.valueobjects.PayerType;
import com.chronicare.platform.payments.domain.model.valueobjects.PaymentMethod;
import com.chronicare.platform.payments.domain.model.valueobjects.SubscriptionStatus;
import com.chronicare.platform.payments.interfaces.rest.resources.CreateSubscriptionResource;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class CreateSubscriptionCommandFromResourceAssembler {

    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;

    /**
     * Converts a CreateSubscriptionResource to a Subscription entity.
     * Note: The planId in the resource can be either a numeric ID or a string plan ID.
     * The service layer will handle resolving the string plan ID to the actual entity.
     */
    public static Subscription toCommandFromResource(CreateSubscriptionResource resource) {
        PayerType payerType = PayerType.valueOf(resource.payerType().toUpperCase());
        
        Subscription subscription = new Subscription();
        subscription.setPayerId(resource.payerId());
        subscription.setPayerType(payerType);
        
        // CRITICAL: Use the status from the request, default to ACTIVE if not provided
        // This allows frontend to set 'active' after successful payment
        if (resource.status() != null && !resource.status().isEmpty()) {
            try {
                subscription.setStatus(SubscriptionStatus.valueOf(resource.status().toUpperCase()));
            } catch (IllegalArgumentException e) {
                subscription.setStatus(SubscriptionStatus.ACTIVE); // Default to ACTIVE
            }
        } else {
            subscription.setStatus(SubscriptionStatus.ACTIVE); // Default to ACTIVE
        }
        
        // Set patient ID for patient subscriptions
        if (resource.patientId() != null) {
            subscription.setPatientId(resource.patientId());
        }
        
        // Set dates from request
        if (resource.startDate() != null && !resource.startDate().isEmpty()) {
            subscription.setStartDate(parseDateTime(resource.startDate()));
        } else {
            subscription.setStartDate(LocalDateTime.now());
        }
        
        if (resource.endDate() != null && !resource.endDate().isEmpty()) {
            subscription.setEndDate(parseDateTime(resource.endDate()));
        } else {
            // Default to 1 month from start date
            subscription.setEndDate(subscription.getStartDate().plusMonths(1));
        }
        
        if (resource.nextBillingDate() != null && !resource.nextBillingDate().isEmpty()) {
            subscription.setNextBillingDate(parseDateTime(resource.nextBillingDate()));
        } else {
            subscription.setNextBillingDate(subscription.getEndDate());
        }
        
        if (resource.lastPaymentDate() != null && !resource.lastPaymentDate().isEmpty()) {
            subscription.setLastPaymentDate(parseDateTime(resource.lastPaymentDate()));
        } else {
            subscription.setLastPaymentDate(LocalDateTime.now());
        }
        
        if (resource.lastPaymentAmount() != null) {
            subscription.setLastPaymentAmount(BigDecimal.valueOf(resource.lastPaymentAmount()));
        }
        
        // Set auto-renew (default true if not provided)
        subscription.setAutoRenew(resource.autoRenew() != null ? resource.autoRenew() : true);
        
        // Set payment method if provided
        if (resource.paymentMethod() != null && !resource.paymentMethod().isEmpty()) {
            try {
                // Handle common payment method formats
                String method = resource.paymentMethod().toUpperCase().replace("-", "_").replace(" ", "_");
                if (method.equals("CARD") || method.equals("CREDITCARD")) {
                    method = "CREDIT_CARD";
                }
                subscription.setPaymentMethod(PaymentMethod.valueOf(method));
            } catch (IllegalArgumentException e) {
                subscription.setPaymentMethod(PaymentMethod.CREDIT_CARD); // Default to CREDIT_CARD
            }
        } else {
            subscription.setPaymentMethod(PaymentMethod.CREDIT_CARD); // Default to CREDIT_CARD
        }
        
        // Set billing email
        if (resource.billingEmail() != null) {
            subscription.setBillingEmail(resource.billingEmail());
        }
        
        // Set Stripe subscription ID
        if (resource.stripeSubscriptionId() != null) {
            subscription.setStripeSubscriptionId(resource.stripeSubscriptionId());
        }
        
        return subscription;
    }
    
    /**
     * Parses a date string in ISO 8601 format to LocalDateTime
     */
    private static LocalDateTime parseDateTime(String dateString) {
        try {
            // Handle various ISO formats
            if (dateString.endsWith("Z")) {
                dateString = dateString.substring(0, dateString.length() - 1);
            }
            return LocalDateTime.parse(dateString, ISO_FORMATTER);
        } catch (DateTimeParseException e) {
            try {
                // Try parsing just the date part
                return LocalDateTime.parse(dateString.substring(0, 19));
            } catch (Exception ex) {
                return LocalDateTime.now();
            }
        }
    }
    
    /**
     * Extracts the plan ID from the resource.
     * Returns the string planId that can be used to look up the plan.
     */
    public static String extractPlanId(CreateSubscriptionResource resource) {
        return resource.planId();
    }
}
