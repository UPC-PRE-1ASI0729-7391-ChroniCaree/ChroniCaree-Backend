package com.chronicare.platform.alerts.infrastructure.ratelimit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service for rate limiting alert creation
 */
@Service
public class RateLimitService {
    
    private static final Logger logger = LoggerFactory.getLogger(RateLimitService.class);
    
    // Rate limits per source type (requests per minute)
    private static final int DEVICE_LIMIT = 60;
    private static final int SYSTEM_LIMIT = 120;
    private static final int MANUAL_LIMIT = 30;
    
    // Store request counts: key = "source:tenantId:patientId", value = RateLimitEntry
    private final Map<String, RateLimitEntry> requestCounts = new ConcurrentHashMap<>();

    /**
     * Check if request is within rate limit
     * 
     * @param source Alert source (DEVICE, SYSTEM, MANUAL)
     * @param tenantId Tenant ID
     * @param patientId Patient ID
     * @return true if allowed, false if rate limit exceeded
     */
    public boolean isAllowed(String source, Long tenantId, Long patientId) {
        String key = generateKey(source, tenantId, patientId);
        int limit = getLimitForSource(source);
        
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime windowStart = now.minusMinutes(1);
        
        requestCounts.entrySet().removeIf(entry -> 
            entry.getValue().timestamp.isBefore(windowStart)
        );
        
        RateLimitEntry entry = requestCounts.computeIfAbsent(key, 
            k -> new RateLimitEntry(now, 0));
        
        if (entry.timestamp.isBefore(windowStart)) {
            // Reset window
            entry.timestamp = now;
            entry.count = 1;
            return true;
        }
        
        if (entry.count >= limit) {
            logger.warn("Rate limit exceeded for key: {} (limit: {}, count: {})", 
                key, limit, entry.count);
            return false;
        }
        
        entry.count++;
        return true;
    }

    private String generateKey(String source, Long tenantId, Long patientId) {
        return String.format("%s:%d:%d", source, tenantId, patientId);
    }

    private int getLimitForSource(String source) {
        return switch (source != null ? source.toUpperCase() : "MANUAL") {
            case "DEVICE" -> DEVICE_LIMIT;
            case "SYSTEM" -> SYSTEM_LIMIT;
            case "MANUAL" -> MANUAL_LIMIT;
            default -> MANUAL_LIMIT;
        };
    }

    /**
     * Inner class to track rate limit entries
     */
    private static class RateLimitEntry {
        LocalDateTime timestamp;
        int count;

        RateLimitEntry(LocalDateTime timestamp, int count) {
            this.timestamp = timestamp;
            this.count = count;
        }
    }
}
