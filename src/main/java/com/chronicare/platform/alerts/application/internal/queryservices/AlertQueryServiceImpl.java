package com.chronicare.platform.alerts.application.internal.queryservices;

import com.chronicare.platform.alerts.domain.model.aggregates.Alert;
import com.chronicare.platform.alerts.domain.model.queries.*;
import com.chronicare.platform.alerts.domain.model.valueobjects.AlertSeverity;
import com.chronicare.platform.alerts.domain.model.valueobjects.AlertStatus;
import com.chronicare.platform.alerts.domain.repository.AlertRepository;
import com.chronicare.platform.alerts.domain.services.AlertQueryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Summary: Implementation of Alert Query Service
 */
@Service
@Transactional(readOnly = true)
public class AlertQueryServiceImpl implements AlertQueryService {

    private final AlertRepository alertRepository;

    public AlertQueryServiceImpl(AlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    @Override
    public Optional<Alert> handle(GetAlertByIdQuery query) {
        return alertRepository.findById(query.alertId());
    }

    @Override
    public List<Alert> handle(GetAlertsByPatientIdQuery query) {
        if (query.status() != null) {
            return alertRepository.findByPatientIdAndStatusAndDeletedAtIsNull(
                query.patientId(),
                AlertStatus.fromCode(query.status())
            );
        }
        return alertRepository.findByPatientIdAndDeletedAtIsNull(query.patientId());
    }

    @Override
    public Page<Alert> handle(GetAlertsByDoctorIdQuery query) {
        int page = query.page() != null ? query.page() : 0;
        int limit = query.limit() != null ? query.limit() : 20;
        
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        if (query.sortBy() != null) {
            String[] parts = query.sortBy().split(" ");
            String property = parts[0];
            Sort.Direction direction = parts.length > 1 && parts[1].equalsIgnoreCase("ASC") 
                ? Sort.Direction.ASC : Sort.Direction.DESC;
            sort = Sort.by(direction, property);
        }
        
        Pageable pageable = PageRequest.of(page, limit, sort);
        
        AlertStatus status = query.status() != null ? AlertStatus.fromCode(query.status()) : null;
        AlertSeverity severity = query.severity() != null ? AlertSeverity.fromCode(query.severity()) : null;
        
        return alertRepository.findByDoctorIdWithFilters(
            query.doctorId(),
            status,
            severity,
            query.patientId(),
            pageable
        );
    }

    @Override
    public List<Alert> handle(GetAlertsByTenantIdQuery query) {
        if (query.status() != null) {
            return alertRepository.findByTenantIdAndStatusAndDeletedAtIsNull(
                query.tenantId(),
                AlertStatus.fromCode(query.status())
            );
        }
        return alertRepository.findByTenantIdAndDeletedAtIsNull(query.tenantId());
    }

    @Override
    public List<Alert> getAllAlerts() {
        return alertRepository.findAll();
    }

    @Override
    public long countActiveAlertsByTenantId(Long tenantId) {
        return alertRepository.countByTenantIdAndStatusAndDeletedAtIsNull(tenantId, AlertStatus.ACTIVE);
    }

    @Override
    public long countCriticalAlertsByTenantId(Long tenantId) {
        return alertRepository.countByTenantIdAndSeverityAndStatusAndDeletedAtIsNull(
            tenantId, AlertSeverity.CRITICAL, AlertStatus.ACTIVE
        );
    }
}
