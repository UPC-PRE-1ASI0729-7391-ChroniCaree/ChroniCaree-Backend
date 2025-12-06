package com.chronicare.platform.records.infrastructure.persistence.jpa.repositories;

import com.chronicare.platform.records.domain.model.aggregates.MedicalRecord;
import com.chronicare.platform.records.domain.model.valueobjects.RecordType;
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
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {

    // Find by patient
    Page<MedicalRecord> findByPatientIdAndIsDeletedFalse(Long patientId, Pageable pageable);

    Page<MedicalRecord> findByPatientIdAndTypeAndIsDeletedFalse(Long patientId, RecordType type, Pageable pageable);

    // Find by tenant
    Page<MedicalRecord> findByTenantIdAndIsDeletedFalse(Long tenantId, Pageable pageable);

    // Find by author
    Page<MedicalRecord> findByAuthorIdAndIsDeletedFalse(Long authorId, Pageable pageable);

    // Find versions
    List<MedicalRecord> findByParentRecordIdOrderByVersionDesc(Long parentRecordId);

    @Query("SELECT r FROM MedicalRecordAggregate r WHERE r.id = :recordId OR r.parentRecordId = :recordId ORDER BY r.version DESC")
    List<MedicalRecord> findAllVersions(@Param("recordId") Long recordId);

    // Find with filters
    @Query("SELECT r FROM MedicalRecordAggregate r WHERE r.patientId = :patientId " +
            "AND r.isDeleted = false " +
            "AND (:type IS NULL OR r.type = :type) " +
            "AND (:startDate IS NULL OR r.createdAt >= :startDate) " +
            "AND (:endDate IS NULL OR r.createdAt <= :endDate)")
    Page<MedicalRecord> findWithFilters(
            @Param("patientId") Long patientId,
            @Param("type") RecordType type,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable
    );

    // Search
    @Query("SELECT r FROM MedicalRecordAggregate r WHERE r.isDeleted = false " +
            "AND r.patientId = :patientId " +
            "AND (LOWER(r.title) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(r.content) LIKE LOWER(CONCAT('%', :query, '%')))")
    Page<MedicalRecord> searchByPatient(
            @Param("patientId") Long patientId,
            @Param("query") String query,
            Pageable pageable
    );

    @Query("SELECT r FROM MedicalRecordAggregate r WHERE r.isDeleted = false " +
            "AND r.tenantId = :tenantId " +
            "AND (LOWER(r.title) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(r.content) LIKE LOWER(CONCAT('%', :query, '%')))")
    Page<MedicalRecord> searchByTenant(
            @Param("tenantId") Long tenantId,
            @Param("query") String query,
            Pageable pageable
    );

    // Find by tags
    @Query("SELECT r FROM MedicalRecordAggregate r JOIN r.tags t WHERE r.isDeleted = false " +
            "AND r.patientId = :patientId AND t = :tag")
    Page<MedicalRecord> findByPatientIdAndTag(
            @Param("patientId") Long patientId,
            @Param("tag") String tag,
            Pageable pageable
    );

    // Count by patient
    long countByPatientIdAndIsDeletedFalse(Long patientId);

    // Check if record exists and is not deleted
    Optional<MedicalRecord> findByIdAndIsDeletedFalse(Long id);
}
