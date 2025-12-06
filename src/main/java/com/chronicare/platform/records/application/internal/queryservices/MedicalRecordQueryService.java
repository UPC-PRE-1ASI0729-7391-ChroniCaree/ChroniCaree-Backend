package com.chronicare.platform.records.application.internal.queryservices;

import com.chronicare.platform.records.domain.model.aggregates.MedicalRecord;
import com.chronicare.platform.records.domain.model.entities.RecordAuditLog;
import com.chronicare.platform.records.domain.model.entities.RecordConsent;
import com.chronicare.platform.records.domain.model.valueobjects.RecordType;
import com.chronicare.platform.records.domain.services.RecordAccessService;
import com.chronicare.platform.records.infrastructure.persistence.jpa.repositories.MedicalRecordRepository;
import com.chronicare.platform.records.infrastructure.persistence.jpa.repositories.RecordAuditLogRepository;
import com.chronicare.platform.records.infrastructure.persistence.jpa.repositories.RecordConsentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MedicalRecordQueryService {

    private final MedicalRecordRepository medicalRecordRepository;
    private final RecordAuditLogRepository auditLogRepository;
    private final RecordConsentRepository consentRepository;
    private final RecordAccessService accessService;

    @Transactional(readOnly = true)
    public Optional<MedicalRecord> getRecordById(Long recordId, Long userId, String userRole) {
        Optional<MedicalRecord> recordOpt = medicalRecordRepository.findByIdAndIsDeletedFalse(recordId);
        
        if (recordOpt.isEmpty()) {
            return Optional.empty();
        }

        MedicalRecord record = recordOpt.get();
        Optional<RecordConsent> consent = consentRepository.findValidConsentForUserAndRecord(recordId, userId);

        if (!accessService.canAccess(record, userId, userRole, consent)) {
            throw new SecurityException("Access denied to record: " + recordId);
        }

        // Log access
        logAccess(recordId, userId);

        return Optional.of(record);
    }

    @Transactional(readOnly = true)
    public Page<MedicalRecord> getRecordsByPatient(Long patientId, RecordType type, 
                                                    Long userId, String userRole, Pageable pageable) {
        Page<MedicalRecord> records;
        
        if (type != null) {
            records = medicalRecordRepository.findByPatientIdAndTypeAndIsDeletedFalse(patientId, type, pageable);
        } else {
            records = medicalRecordRepository.findByPatientIdAndIsDeletedFalse(patientId, pageable);
        }

        // Filter by access control - note: this keeps pagination counts but may have empty results
        return records.map(record -> {
            Optional<RecordConsent> consent = consentRepository.findValidConsentForUserAndRecord(record.getId(), userId);
            if (!accessService.canAccess(record, userId, userRole, consent)) {
                return null;
            }
            return record;
        });
    }

    @Transactional(readOnly = true)
    public Page<MedicalRecord> getRecordsWithFilters(Long patientId, RecordType type, 
                                                      LocalDateTime startDate, LocalDateTime endDate,
                                                      Long userId, String userRole, Pageable pageable) {
        Page<MedicalRecord> records = medicalRecordRepository.findWithFilters(
                patientId, type, startDate, endDate, pageable
        );

        // Filter by access control - note: this keeps pagination counts but may have empty results
        return records.map(record -> {
            Optional<RecordConsent> consent = consentRepository.findValidConsentForUserAndRecord(record.getId(), userId);
            if (!accessService.canAccess(record, userId, userRole, consent)) {
                return null;
            }
            return record;
        });
    }

    @Transactional(readOnly = true)
    public Page<MedicalRecord> searchRecords(Long patientId, String query, 
                                              Long userId, String userRole, Pageable pageable) {
        Page<MedicalRecord> records = medicalRecordRepository.searchByPatient(patientId, query, pageable);

        // Filter accessible records - note: this keeps pagination counts but may have empty results
        return records.map(record -> {
            Optional<RecordConsent> consent = consentRepository.findValidConsentForUserAndRecord(record.getId(), userId);
            if (!accessService.canAccess(record, userId, userRole, consent)) {
                return null;
            }
            return record;
        });
    }

    @Transactional(readOnly = true)
    public List<MedicalRecord> getVersionHistory(Long recordId, Long userId, String userRole) {
        MedicalRecord record = medicalRecordRepository.findById(recordId)
                .orElseThrow(() -> new IllegalArgumentException("Record not found: " + recordId));

        Optional<RecordConsent> consent = consentRepository.findValidConsentForUserAndRecord(recordId, userId);
        if (!accessService.canAccess(record, userId, userRole, consent)) {
            throw new SecurityException("Access denied");
        }

        return medicalRecordRepository.findAllVersions(recordId);
    }

    @Transactional(readOnly = true)
    public List<RecordAuditLog> getAuditTrail(Long recordId, Long userId, String userRole) {
        MedicalRecord record = medicalRecordRepository.findById(recordId)
                .orElseThrow(() -> new IllegalArgumentException("Record not found: " + recordId));

        Optional<RecordConsent> consent = consentRepository.findValidConsentForUserAndRecord(recordId, userId);
        if (!accessService.canAccess(record, userId, userRole, consent)) {
            throw new SecurityException("Access denied");
        }

        return auditLogRepository.findByRecordIdOrderByTimestampDesc(recordId);
    }

    @Transactional(readOnly = true)
    public long countRecordsByPatient(Long patientId) {
        return medicalRecordRepository.countByPatientIdAndIsDeletedFalse(patientId);
    }

    @Transactional(readOnly = true)
    public Page<MedicalRecord> getRecordsByTag(Long patientId, String tag, 
                                                Long userId, String userRole, Pageable pageable) {
        Page<MedicalRecord> records = medicalRecordRepository.findByPatientIdAndTag(patientId, tag, pageable);

        // Filter accessible records - note: this keeps pagination counts but may have empty results
        return records.map(record -> {
            Optional<RecordConsent> consent = consentRepository.findValidConsentForUserAndRecord(record.getId(), userId);
            if (!accessService.canAccess(record, userId, userRole, consent)) {
                return null;
            }
            return record;
        });
    }

    private void logAccess(Long recordId, Long userId) {
        RecordAuditLog log = RecordAuditLog.builder()
                .recordId(recordId)
                .userId(userId)
                .action("VIEWED")
                .details("Record accessed")
                .ipAddress("unknown")
                .timestamp(LocalDateTime.now())
                .build();

        auditLogRepository.save(log);
    }
}
