package com.chronicare.platform.payments.infrastructure.seeders;

import com.chronicare.platform.payments.application.services.SubscriptionPlanService;
import com.chronicare.platform.payments.domain.model.aggregates.SubscriptionPlan;
import com.chronicare.platform.payments.domain.model.valueobjects.BillingPeriod;
import com.chronicare.platform.payments.domain.model.valueobjects.PlanType;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.logging.Logger;

@Component
@Order(1)
public class SubscriptionPlanSeeder implements CommandLineRunner {

    private static final Logger logger = Logger.getLogger(SubscriptionPlanSeeder.class.getName());
    private final SubscriptionPlanService subscriptionPlanService;

    public SubscriptionPlanSeeder(SubscriptionPlanService subscriptionPlanService) {
        this.subscriptionPlanService = subscriptionPlanService;
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("Seeding subscription plans...");
        seedPatientPlans();
        seedTenantPlans();
        logger.info("Subscription plans seeded successfully.");
    }

    private void seedPatientPlans() {
        // Patient Free Plan
        SubscriptionPlan patientFree = new SubscriptionPlan(
            "patient_free",
            PlanType.PATIENT,
            "Gratuito",
            BigDecimal.ZERO,
            "USD",
            BillingPeriod.MONTHLY,
            """
            {
                "doctorAccess": true,
                "symptomTracking": true,
                "medicationReminders": true,
                "basicReports": false,
                "advancedReports": false,
                "dataStorage": "6 months",
                "support": "community",
                "aiInsights": false,
                "familySharing": false,
                "availability": "Lunes a Viernes 24/7"
            }
            """
        );
        subscriptionPlanService.saveOrUpdatePlan(patientFree);

        // Patient Standard Plan
        SubscriptionPlan patientStandard = new SubscriptionPlan(
            "patient_standard",
            PlanType.PATIENT,
            "Estándar",
            new BigDecimal("19.99"),
            "USD",
            BillingPeriod.MONTHLY,
            """
            {
                "doctorAccess": true,
                "symptomTracking": true,
                "medicationReminders": true,
                "basicReports": true,
                "advancedReports": false,
                "dataStorage": "1 year",
                "support": "priority",
                "aiInsights": false,
                "familySharing": false,
                "availability": "24/7 Todos los días",
                "homeVisits": true
            }
            """
        );
        subscriptionPlanService.saveOrUpdatePlan(patientStandard);

        // Patient Premium Plan
        SubscriptionPlan patientPremium = new SubscriptionPlan(
            "patient_premium",
            PlanType.PATIENT,
            "Premium",
            new BigDecimal("49.99"),
            "USD",
            BillingPeriod.MONTHLY,
            """
            {
                "doctorAccess": true,
                "symptomTracking": true,
                "medicationReminders": true,
                "basicReports": true,
                "advancedReports": true,
                "dataStorage": "unlimited",
                "support": "priority",
                "aiInsights": true,
                "familySharing": true,
                "maxMembers": 5,
                "availability": "24/7 Todos los días",
                "homeVisits": true,
                "specialistAccess": true
            }
            """
        );
        subscriptionPlanService.saveOrUpdatePlan(patientPremium);
    }

    private void seedTenantPlans() {
        // Tenant Basic Plan
        SubscriptionPlan tenantBasic = new SubscriptionPlan(
            "tenant_basic",
            PlanType.TENANT,
            "Basic",
            new BigDecimal("299"),
            "USD",
            BillingPeriod.MONTHLY,
            """
            {
                "maxDoctors": 5,
                "maxPatients": 100,
                "basicAnalytics": true,
                "advancedAnalytics": false,
                "customBranding": false,
                "apiAccess": false,
                "support": "email",
                "dataRetention": "1 year"
            }
            """
        );
        subscriptionPlanService.saveOrUpdatePlan(tenantBasic);

        // Tenant Professional Plan
        SubscriptionPlan tenantProfessional = new SubscriptionPlan(
            "tenant_professional",
            PlanType.TENANT,
            "Professional",
            new BigDecimal("599"),
            "USD",
            BillingPeriod.MONTHLY,
            """
            {
                "maxDoctors": 20,
                "maxPatients": 500,
                "basicAnalytics": true,
                "advancedAnalytics": true,
                "customBranding": true,
                "apiAccess": true,
                "support": "phone",
                "dataRetention": "3 years"
            }
            """
        );
        subscriptionPlanService.saveOrUpdatePlan(tenantProfessional);

        // Tenant Enterprise Plan
        SubscriptionPlan tenantEnterprise = new SubscriptionPlan(
            "tenant_enterprise",
            PlanType.TENANT,
            "Enterprise",
            new BigDecimal("999"),
            "USD",
            BillingPeriod.MONTHLY,
            """
            {
                "maxDoctors": -1,
                "maxPatients": -1,
                "basicAnalytics": true,
                "advancedAnalytics": true,
                "customBranding": true,
                "apiAccess": true,
                "support": "dedicated",
                "dataRetention": "unlimited",
                "whiteLabel": true,
                "customIntegrations": true
            }
            """
        );
        subscriptionPlanService.saveOrUpdatePlan(tenantEnterprise);
    }
}
