package com.chronicare.platform.alerts.application.internal.commandservices;

import com.chronicare.platform.alerts.application.internal.services.AlertDeduplicationService;
import com.chronicare.platform.alerts.application.internal.services.NotificationRoutingService;
import com.chronicare.platform.alerts.domain.model.aggregates.Alert;
import com.chronicare.platform.alerts.domain.model.commands.*;
import com.chronicare.platform.alerts.domain.model.events.AlertCreatedEvent;
import com.chronicare.platform.alerts.domain.model.events.AlertStatusChangedEvent;
import com.chronicare.platform.alerts.domain.repository.AlertRepository;
import com.chronicare.platform.alerts.domain.services.AlertCommandService;
import com.chronicare.platform.alerts.infrastructure.events.DomainEventPublisher;
import com.chronicare.platform.alerts.infrastructure.ratelimit.RateLimitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Summary: Alert Command Service with event publishing, deduplication, rate limiting, and notification routing
 */
@Service
@Transactional
public class AlertCommandServiceImpl implements AlertCommandService {

    private static final Logger logger = LoggerFactory.getLogger(AlertCommandServiceImpl.class);

    private final AlertRepository alertRepository;
    private final DomainEventPublisher eventPublisher;
    private final NotificationRoutingService notificationRoutingService;
    private final AlertDeduplicationService deduplicationService;
    private final RateLimitService rateLimitService;

    public AlertCommandServiceImpl(AlertRepository alertRepository,
                                  DomainEventPublisher eventPublisher,
                                  NotificationRoutingService notificationRoutingService,
                                  AlertDeduplicationService deduplicationService,
                                  RateLimitService rateLimitService) {
        this.alertRepository = alertRepository;
        this.eventPublisher = eventPublisher;
        this.notificationRoutingService = notificationRoutingService;
        this.deduplicationService = deduplicationService;
        this.rateLimitService = rateLimitService;
    }

    @Override
    public Alert handle(CreateAlertCommand command) {
        // Rate limiting check
        if (!rateLimitService.isAllowed(command.source(), command.tenantId(), command.patientId())) {
            logger.warn("Rate limit exceeded for alert creation: tenant={}, patient={}", 
                command.tenantId(), command.patientId());
            throw new IllegalStateException("Rate limit exceeded for alert creation");
        }

        // Deduplication check
        Optional<Alert> duplicate = deduplicationService.findDuplicate(
            command.patientId(),
            command.type(),
            command.sourceType(),
            command.sourceId(),
            command.metadata()
        );
        
        if (duplicate.isPresent()) {
            logger.info("Duplicate alert found, returning existing alert: {}", duplicate.get().getId());
            return duplicate.get();
        }

        // Create new alert
        Alert alert = new Alert(command);
        Alert savedAlert = alertRepository.save(alert);
        
        // Publish domain event
        AlertCreatedEvent event = new AlertCreatedEvent(
            savedAlert.getId(),
            savedAlert.getPatientId(),
            savedAlert.getTenantId(),
            savedAlert.getType().name(),
            savedAlert.getSeverity().name(),
            savedAlert.getSource().name(),
            savedAlert.getDetectedAt()
        );
        eventPublisher.publish(event);
        
        // Route notifications
        List<String> channels = notificationRoutingService.determineChannels(savedAlert);
        notificationRoutingService.routeNotification(savedAlert, channels);
        
        logger.info("Alert created successfully: {}", savedAlert.getId());
        return savedAlert;
    }

    @Override
    public Optional<Alert> handle(AcknowledgeAlertCommand command) {
        return alertRepository.findById(command.alertId())
            .map(alert -> {
                String previousStatus = alert.getStatus().name();
                alert.acknowledge(command);
                Alert savedAlert = alertRepository.save(alert);
                
                // Publish status change event
                publishStatusChangeEvent(savedAlert, previousStatus);
                
                return savedAlert;
            });
    }

    @Override
    public Optional<Alert> handle(ResolveAlertCommand command) {
        return alertRepository.findById(command.alertId())
            .map(alert -> {
                String previousStatus = alert.getStatus().name();
                alert.resolve(command);
                Alert savedAlert = alertRepository.save(alert);
                
                // Publish status change event
                publishStatusChangeEvent(savedAlert, previousStatus);
                
                return savedAlert;
            });
    }

    @Override
    public Optional<Alert> handle(EscalateAlertCommand command) {
        return alertRepository.findById(command.alertId())
            .map(alert -> {
                String previousStatus = alert.getStatus().name();
                alert.escalate(command);
                Alert savedAlert = alertRepository.save(alert);
                
                // Publish status change event
                publishStatusChangeEvent(savedAlert, previousStatus);
                
                return savedAlert;
            });
    }

    @Override
    public Optional<Alert> handle(DismissAlertCommand command) {
        return alertRepository.findById(command.alertId())
            .map(alert -> {
                String previousStatus = alert.getStatus().name();
                alert.dismiss(command.dismissedBy(), command.notes());
                Alert savedAlert = alertRepository.save(alert);
                
                // Publish status change event
                publishStatusChangeEvent(savedAlert, previousStatus);
                
                return savedAlert;
            });
    }

    private void publishStatusChangeEvent(Alert alert, String previousStatus) {
        AlertStatusChangedEvent event = new AlertStatusChangedEvent(
            alert.getId(),
            alert.getPatientId(),
            alert.getTenantId(),
            previousStatus,
            alert.getStatus().name(),
            alert.getDoctorId()
        );
        eventPublisher.publish(event);
    }

    @Override
    public void deleteAlert(Long alertId) {
        alertRepository.findById(alertId).ifPresent(alertRepository::delete);
    }
}
