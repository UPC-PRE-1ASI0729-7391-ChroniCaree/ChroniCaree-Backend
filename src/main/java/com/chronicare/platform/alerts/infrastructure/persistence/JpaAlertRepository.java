package com.chronicare.platform.alerts.infrastructure.persistence;

import com.chronicare.platform.alerts.domain.repository.AlertRepository;
import org.springframework.stereotype.Repository;

/**
 * JPA Repository for Alert entity
 * Extends AlertRepository which already includes JpaRepository
 */
@Repository
public interface JpaAlertRepository extends AlertRepository {
    // All methods inherited from AlertRepository (which extends JpaRepository)
}
