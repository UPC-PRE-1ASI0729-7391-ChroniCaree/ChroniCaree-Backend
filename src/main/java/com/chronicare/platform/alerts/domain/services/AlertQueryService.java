package com.chronicare.platform.alerts.domain.services;

import com.chronicare.platform.alerts.domain.model.aggregates.Alert;
import com.chronicare.platform.alerts.domain.model.queries.*;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

/**
 * Alert Query Service Interface
 * Handles all queries related to alerts
 */
public interface AlertQueryService {
    
    Optional<Alert> handle(GetAlertByIdQuery query);
    
    List<Alert> handle(GetAlertsByPatientIdQuery query);
    
    Page<Alert> handle(GetAlertsByDoctorIdQuery query);
    
    List<Alert> handle(GetAlertsByTenantIdQuery query);
    
    List<Alert> getAllAlerts();
    
    long countActiveAlertsByTenantId(Long tenantId);
    
    long countCriticalAlertsByTenantId(Long tenantId);
}
