package com.chronicare.platform.alerts.application.internal.services;

import com.chronicare.platform.alerts.domain.model.aggregates.Alert;
import com.chronicare.platform.alerts.domain.model.valueobjects.AlertStatus;
import com.chronicare.platform.alerts.domain.repository.AlertRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.HexFormat;
import java.util.Optional;

/**
 * Service for detecting and preventing duplicate alerts
 */
@Service
public class AlertDeduplicationService {
    
    private static final Logger logger = LoggerFactory.getLogger(AlertDeduplicationService.class);
    private static final int DEDUPLICATION_WINDOW_HOURS = 24;
    
    private final AlertRepository alertRepository;

    public AlertDeduplicationService(AlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    /**
     * Check if a similar alert already exists within the deduplication window
     * 
     * @param patientId Patient ID
     * @param type Alert type
     * @param sourceType Source type (e.g., "RULE", "DEVICE")
     * @param sourceId Source identifier
     * @param metadata Additional metadata
     * @return Optional containing existing alert if duplicate found
     */
    public Optional<Alert> findDuplicate(Long patientId, String type, String sourceType, 
                                        Long sourceId, String metadata) {
        try {
            String hash = generateHash(patientId, type, sourceType, sourceId, metadata);
            LocalDateTime windowStart = LocalDateTime.now().minusHours(DEDUPLICATION_WINDOW_HOURS);
            
            logger.debug("Checking for duplicate alerts with hash: {} since: {}", hash, windowStart);
            
            // Check for existing active alerts with same source in time window
            if (sourceType != null && sourceId != null) {
                return alertRepository.findBySourceTypeAndSourceIdAndStatusAndCreatedAtAfter(
                    sourceType,
                    sourceId,
                    AlertStatus.ACTIVE,
                    windowStart
                );
            }
            
            return Optional.empty();
        } catch (Exception ex) {
            logger.error("Error during deduplication check", ex);
            return Optional.empty();
        }
    }

    /**
     * Generate SHA-256 hash for alert deduplication
     */
    private String generateHash(Long patientId, String type, String sourceType, 
                               Long sourceId, String metadata) {
        try {
            String input = String.format("%d|%s|%s|%s|%s", 
                patientId, 
                type != null ? type : "",
                sourceType != null ? sourceType : "",
                sourceId != null ? sourceId.toString() : "",
                metadata != null ? metadata : ""
            );
            
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hashBytes);
        } catch (NoSuchAlgorithmException ex) {
            logger.error("SHA-256 algorithm not available", ex);
            return "";
        }
    }
}
