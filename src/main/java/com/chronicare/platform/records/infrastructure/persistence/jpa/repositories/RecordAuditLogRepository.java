package com.chronicare.platform.records.infrastructure.persistence.jpa.repositories;

import com.chronicare.platform.records.domain.model.entities.RecordAuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RecordAuditLogRepository extends JpaRepository<RecordAuditLog, Long> {

    // Find audit logs by record
    Page<RecordAuditLog> findByRecordIdOrderByTimestampDesc(Long recordId, Pageable pageable);

    List<RecordAuditLog> findByRecordIdOrderByTimestampDesc(Long recordId);

    // Find by user
    Page<RecordAuditLog> findByUserIdOrderByTimestampDesc(Long userId, Pageable pageable);

    // Find by action
    List<RecordAuditLog> findByRecordIdAndAction(Long recordId, String action);

    // Find by date range
    List<RecordAuditLog> findByRecordIdAndTimestampBetween(Long recordId, LocalDateTime start, LocalDateTime end);

    // Count actions
    long countByRecordIdAndAction(Long recordId, String action);
}
