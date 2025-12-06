package com.chronicare.platform.alerts.application.internal.services;

import com.chronicare.platform.alerts.domain.model.aggregates.Alert;
import com.chronicare.platform.alerts.domain.model.valueobjects.AlertSeverity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service for routing alert notifications based on severity and configuration
 */
@Service
public class NotificationRoutingService {
    
    private static final Logger logger = LoggerFactory.getLogger(NotificationRoutingService.class);
    private static final String CHANNEL_EMAIL = "EMAIL";
    private static final String CHANNEL_DASHBOARD = "DASHBOARD";

    /**
     * Determine notification channels based on alert severity
     * 
     * @param alert The alert to route
     * @return List of notification channels (e.g., "PUSH", "SMS", "EMAIL", "DASHBOARD")
     */
    public List<String> determineChannels(Alert alert) {
        List<String> channels = new ArrayList<>();
        
        AlertSeverity severity = alert.getSeverity();
        
        switch (severity) {
            case CRITICAL:
                // CRITICAL: push + SMS + email (immediate)
                channels.add("PUSH");
                channels.add("SMS");
                channels.add(CHANNEL_EMAIL);
                channels.add(CHANNEL_DASHBOARD);
                logger.info("CRITICAL alert {} routed to: PUSH, SMS, EMAIL, DASHBOARD", alert.getId());
                break;
                
            case HIGH:
                // HIGH: push + email
                channels.add("PUSH");
                channels.add(CHANNEL_EMAIL);
                channels.add(CHANNEL_DASHBOARD);
                logger.info("HIGH alert {} routed to: PUSH, EMAIL, DASHBOARD", alert.getId());
                break;
                
            case MEDIUM:
                // MEDIUM: dashboard + digest email
                channels.add(CHANNEL_DASHBOARD);
                channels.add("EMAIL_DIGEST");
                logger.info("MEDIUM alert {} routed to: DASHBOARD, EMAIL_DIGEST", alert.getId());
                break;
                
            case LOW:
                // LOW: dashboard only
                channels.add(CHANNEL_DASHBOARD);
                logger.info("LOW alert {} routed to: DASHBOARD", alert.getId());
                break;
                
            default:
                channels.add(CHANNEL_DASHBOARD);
                break;
        }
        
        return channels;
    }

    /**
     * Route notification to configured channels
     * This method would integrate with actual notification services
     */
    public void routeNotification(Alert alert, List<String> channels) {
        logger.info("Routing alert {} notifications to channels: {}", alert.getId(), channels);
        
        for (String channel : channels) {
            try {
                switch (channel) {
                    case "PUSH":
                        sendPushNotification(alert);
                        break;
                    case "SMS":
                        sendSmsNotification(alert);
                        break;
                    case CHANNEL_EMAIL:
                        sendEmailNotification(alert);
                        break;
                    case "EMAIL_DIGEST":
                        addToEmailDigest(alert);
                        break;
                    case CHANNEL_DASHBOARD:
                        updateDashboard(alert);
                        break;
                    default:
                        logger.warn("Unknown notification channel: {}", channel);
                }
            } catch (Exception ex) {
                logger.error("Error routing to channel {}: {}", channel, ex.getMessage());
            }
        }
    }

    private void sendPushNotification(Alert alert) {
        // Integration with push notification service (e.g., FCM, APNs)
        logger.info("Sending PUSH notification for alert: {}", alert.getId());
    }

    private void sendSmsNotification(Alert alert) {
        // Integration with SMS service (e.g., Twilio)
        logger.info("Sending SMS notification for alert: {}", alert.getId());
    }

    private void sendEmailNotification(Alert alert) {
        // Integration with email service
        logger.info("Sending EMAIL notification for alert: {}", alert.getId());
    }

    private void addToEmailDigest(Alert alert) {
        // Add to digest queue for batch processing
        logger.info("Adding alert {} to EMAIL_DIGEST queue", alert.getId());
    }

    private void updateDashboard(Alert alert) {
        // Trigger dashboard update via WebSocket
        logger.info("Updating DASHBOARD for alert: {}", alert.getId());
    }
}
