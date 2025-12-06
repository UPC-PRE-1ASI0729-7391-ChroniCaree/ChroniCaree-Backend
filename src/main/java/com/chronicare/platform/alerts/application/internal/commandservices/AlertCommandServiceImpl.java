package com.chronicare.platform.alerts.application.internal.commandservices;

import com.chronicare.platform.alerts.domain.model.aggregates.Alert;
import com.chronicare.platform.alerts.domain.model.commands.*;
import com.chronicare.platform.alerts.domain.model.valueobjects.AlertStatus;
import com.chronicare.platform.alerts.domain.repository.AlertRepository;
import com.chronicare.platform.alerts.domain.services.AlertCommandService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Summary: Implementation of Alert Command Service
 */
@Service
@Transactional
public class AlertCommandServiceImpl implements AlertCommandService {

    private final AlertRepository alertRepository;

    public AlertCommandServiceImpl(AlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    @Override
    public Alert handle(CreateAlertCommand command) {
        // Check for duplicate alerts (same source in last 24 hours)
        if (command.sourceType() != null && command.sourceId() != null) {
            Optional<Alert> existing = alertRepository.findBySourceTypeAndSourceIdAndStatusAndCreatedAtAfter(
                command.sourceType(),
                command.sourceId(),
                AlertStatus.ACTIVE,
                LocalDateTime.now().minusHours(24)
            );
            if (existing.isPresent()) {
                // Update existing alert instead of creating new
                Alert existingAlert = existing.get();
                // Could update metadata or priority if needed
                return alertRepository.save(existingAlert);
            }
        }

        Alert alert = new Alert(command);
        return alertRepository.save(alert);
    }

    @Override
    public Optional<Alert> handle(AcknowledgeAlertCommand command) {
        return alertRepository.findById(command.alertId())
            .map(alert -> {
                alert.acknowledge(command);
                return alertRepository.save(alert);
            });
    }

    @Override
    public Optional<Alert> handle(ResolveAlertCommand command) {
        return alertRepository.findById(command.alertId())
            .map(alert -> {
                alert.resolve(command);
                return alertRepository.save(alert);
            });
    }

    @Override
    public Optional<Alert> handle(EscalateAlertCommand command) {
        return alertRepository.findById(command.alertId())
            .map(alert -> {
                alert.escalate(command);
                return alertRepository.save(alert);
            });
    }

    @Override
    public Optional<Alert> handle(DismissAlertCommand command) {
        return alertRepository.findById(command.alertId())
            .map(alert -> {
                alert.dismiss(command.dismissedBy(), command.notes());
                return alertRepository.save(alert);
            });
    }

    @Override
    public void deleteAlert(Long alertId) {
        alertRepository.findById(alertId).ifPresent(alert -> {
            // Soft delete
            alertRepository.delete(alert);
        });
    }
}
