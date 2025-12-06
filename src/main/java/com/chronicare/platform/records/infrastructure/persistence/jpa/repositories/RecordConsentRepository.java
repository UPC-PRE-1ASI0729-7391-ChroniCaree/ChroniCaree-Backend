package com.chronicare.platform.records.infrastructure.persistence.jpa.repositories;

import com.chronicare.platform.records.domain.model.entities.RecordConsent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecordConsentRepository extends JpaRepository<RecordConsent, Long> {

    // Find consents for a record
    List<RecordConsent> findByRecordIdAndRevokedFalse(Long recordId);

    // Find valid consent for user to access record
    @Query("SELECT c FROM RecordConsent c WHERE c.recordId = :recordId " +
            "AND c.grantedToUserId = :userId " +
            "AND c.revoked = false " +
            "AND (c.expiresAt IS NULL OR c.expiresAt > CURRENT_TIMESTAMP)")
    Optional<RecordConsent> findValidConsentForUserAndRecord(
            @Param("recordId") Long recordId,
            @Param("userId") Long userId
    );

    // Find all consents granted to user
    List<RecordConsent> findByGrantedToUserIdAndRevokedFalse(Long userId);

    // Find consents by patient
    List<RecordConsent> findByPatientId(Long patientId);
}
