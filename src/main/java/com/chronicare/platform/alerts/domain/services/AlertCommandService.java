package com.chronicare.platform.alerts.domain.services;

import com.chronicare.platform.alerts.domain.model.aggregates.Alert;
import com.chronicare.platform.alerts.domain.model.commands.*;

import java.util.Optional;

/**
 * Alert Command Service Interface
 * Handles all commands related to alerts
 */
public interface AlertCommandService {
    
    Alert handle(CreateAlertCommand command);
    
    Optional<Alert> handle(AcknowledgeAlertCommand command);
    
    Optional<Alert> handle(ResolveAlertCommand command);
    
    Optional<Alert> handle(EscalateAlertCommand command);
    
    Optional<Alert> handle(DismissAlertCommand command);
    
    void deleteAlert(Long alertId);
}
