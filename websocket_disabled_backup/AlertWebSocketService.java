package com.chronicare.platform.alerts.infrastructure.websocket;

import com.chronicare.platform.alerts.domain.model.aggregates.Alert;
import com.chronicare.platform.alerts.interfaces.rest.resources.AlertResource;
import com.chronicare.platform.alerts.interfaces.rest.transform.AlertResourceFromEntityAssembler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

/**
 * Service for broadcasting alert updates via WebSocket
 */
@Service
public class AlertWebSocketService {
    
    private static final Logger logger = LoggerFactory.getLogger(AlertWebSocketService.class);
    
    private final SimpMessagingTemplate messagingTemplate;

    public AlertWebSocketService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * Broadcast alert created event to all subscribers
     */
    public void broadcastAlertCreated(Alert alert) {
        try {
            AlertResource resource = AlertResourceFromEntityAssembler.toResourceFromEntity(alert);
            String destination = String.format("/topic/alerts/%d", alert.getTenantId());
            
            messagingTemplate.convertAndSend(destination, new WebSocketMessage(
                "alert.created",
                resource,
                alert.getTenantId(),
                alert.getPatientId()
            ));
            
            logger.info("Broadcasted alert.created event for alert: {}", alert.getId());
        } catch (Exception ex) {
            logger.error("Error broadcasting alert created event", ex);
        }
    }

    /**
     * Broadcast alert status changed event
     */
    public void broadcastAlertStatusChanged(Alert alert, String previousStatus) {
        try {
            AlertResource resource = AlertResourceFromEntityAssembler.toResourceFromEntity(alert);
            String destination = String.format("/topic/alerts/%d", alert.getTenantId());
            
            messagingTemplate.convertAndSend(destination, new WebSocketMessage(
                "alert.status.changed",
                resource,
                alert.getTenantId(),
                alert.getPatientId()
            ));
            
            logger.info("Broadcasted alert.status.changed event for alert: {}", alert.getId());
        } catch (Exception ex) {
            logger.error("Error broadcasting alert status changed event", ex);
        }
    }

    /**
     * Broadcast alert assigned event
     */
    public void broadcastAlertAssigned(Alert alert) {
        try {
            AlertResource resource = AlertResourceFromEntityAssembler.toResourceFromEntity(alert);
            String destination = String.format("/topic/alerts/%d", alert.getTenantId());
            
            messagingTemplate.convertAndSend(destination, new WebSocketMessage(
                "alert.assigned",
                resource,
                alert.getTenantId(),
                alert.getPatientId()
            ));
            
            logger.info("Broadcasted alert.assigned event for alert: {}", alert.getId());
        } catch (Exception ex) {
            logger.error("Error broadcasting alert assigned event", ex);
        }
    }

    /**
     * WebSocket message wrapper
     */
    public record WebSocketMessage(
        String eventType,
        AlertResource alert,
        Long tenantId,
        Long patientId
    ) {}
}
