package com.chronicare.platform.alerts.domain.repository;

import com.chronicare.platform.alerts.domain.model.aggregates.Alert;
import com.chronicare.platform.alerts.domain.model.valueobjects.AlertStatus;
import com.chronicare.platform.alerts.domain.model.valueobjects.AlertSeverity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {

    // Find by patient
    List<Alert> findByPatientIdAndDeletedAtIsNull(Long patientId);
    
    List<Alert> findByPatientIdAndStatusAndDeletedAtIsNull(Long patientId, AlertStatus status);
    
    Page<Alert> findByPatientIdAndDeletedAtIsNull(Long patientId, Pageable pageable);

    // Find by doctor
    List<Alert> findByDoctorIdAndDeletedAtIsNull(Long doctorId);
    
    List<Alert> findByDoctorIdAndStatusAndDeletedAtIsNull(Long doctorId, AlertStatus status);
    
    Page<Alert> findByDoctorIdAndDeletedAtIsNull(Long doctorId, Pageable pageable);

    // Find by tenant
    List<Alert> findByTenantIdAndDeletedAtIsNull(Long tenantId);
    
    List<Alert> findByTenantIdAndStatusAndDeletedAtIsNull(Long tenantId, AlertStatus status);
    
    Page<Alert> findByTenantIdAndDeletedAtIsNull(Long tenantId, Pageable pageable);

    // Find by status
    List<Alert> findByStatusAndDeletedAtIsNull(AlertStatus status);

    // Find critical and high alerts needing escalation
    @Query("SELECT a FROM Alert a WHERE a.status = 'ACTIVE' AND a.deletedAt IS NULL AND " +
           "((a.severity = 'CRITICAL' AND a.createdAt < :criticalThreshold) OR " +
           "(a.severity = 'HIGH' AND a.createdAt < :highThreshold))")
    List<Alert> findAlertsNeedingEscalation(
        @Param("criticalThreshold") LocalDateTime criticalThreshold,
        @Param("highThreshold") LocalDateTime highThreshold
    );

    // Find expired alerts
    @Query("SELECT a FROM Alert a WHERE a.status = 'ACTIVE' AND a.expiresAt IS NOT NULL AND a.expiresAt < :now AND a.deletedAt IS NULL")
    List<Alert> findExpiredAlerts(@Param("now") LocalDateTime now);

    // Count by tenant and status
    long countByTenantIdAndStatusAndDeletedAtIsNull(Long tenantId, AlertStatus status);
    
    long countByTenantIdAndDeletedAtIsNull(Long tenantId);

    // Count critical alerts by tenant
    long countByTenantIdAndSeverityAndStatusAndDeletedAtIsNull(Long tenantId, AlertSeverity severity, AlertStatus status);

    // Find by source (for deduplication)
    Optional<Alert> findBySourceTypeAndSourceIdAndStatusAndCreatedAtAfter(
        String sourceType, Long sourceId, AlertStatus status, LocalDateTime after
    );

    // Complex query for doctor alerts with filters
    @Query("SELECT a FROM Alert a WHERE a.doctorId = :doctorId AND a.deletedAt IS NULL " +
           "AND (:status IS NULL OR a.status = :status) " +
           "AND (:severity IS NULL OR a.severity = :severity) " +
           "AND (:patientId IS NULL OR a.patientId = :patientId)")
    Page<Alert> findByDoctorIdWithFilters(
        @Param("doctorId") Long doctorId,
        @Param("status") AlertStatus status,
        @Param("severity") AlertSeverity severity,
        @Param("patientId") Long patientId,
        Pageable pageable
    );
}
