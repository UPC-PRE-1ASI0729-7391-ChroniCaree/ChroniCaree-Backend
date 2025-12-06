package com.chronicare.platform.tenants.application.internal.queryservices;

import com.chronicare.platform.tenants.domain.aggregates.Tenant;
import com.chronicare.platform.tenants.domain.model.aggregates.DashboardStats;
import com.chronicare.platform.tenants.domain.model.queries.GetTenantDashboardStatsQuery;
import com.chronicare.platform.tenants.domain.model.valueobjects.QuotaUsage;
import com.chronicare.platform.tenants.domain.repository.TenantRepository;
import com.chronicare.platform.tenants.domain.services.TenantQueryService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * TenantQueryServiceImpl
 *
 * @summary
 * Provides aggregated dashboard metrics for tenants by executing optimized
 * SQL-based queries and combining domain information with real-time usage data.
 * Key responsibilities:
 * - Retrieve tenant profile and validate existence
 * - Aggregate doctors, patients, appointments, and alerts statistics
 * - Compute monthly and total revenue for tenant accounts
 * - Build quota usage information from tenant configuration
 * - Provide a unified DashboardStats response for frontend analytics
 * Notes:
 * - All metrics are fetched using direct SQL for performance
 * - Null-safe defaults are applied for tenant fields and maximum quotas
 * - Uses JdbcTemplate to efficiently query relational data
 */

@Service
public class TenantQueryServiceImpl implements TenantQueryService {
    
    private final TenantRepository tenantRepository;
    private final JdbcTemplate jdbcTemplate;
    
    public TenantQueryServiceImpl(TenantRepository tenantRepository, JdbcTemplate jdbcTemplate) {
        this.tenantRepository = tenantRepository;
        this.jdbcTemplate = jdbcTemplate;
    }
    
    @Override
    public Optional<DashboardStats> handle(GetTenantDashboardStatsQuery query) {
        Long tenantId = query.tenantId();
        
        // Get tenant data
        Optional<Tenant> tenantOpt = tenantRepository.findById(tenantId);
        if (tenantOpt.isEmpty()) {
            return Optional.empty();
        }
        
        Tenant tenant = tenantOpt.get();
        
        // Execute all queries
        Integer totalDoctors = getTotalDoctors(tenantId);
        Integer activeDoctors = getActiveDoctors(tenantId);
        Integer inactiveDoctors = totalDoctors - activeDoctors;
        Integer totalPatients = getTotalPatients(tenantId);
        Integer activePatients = getActivePatients(tenantId);
        Integer newPatientsThisMonth = getNewPatientsThisMonth(tenantId);
        Integer totalAppointments = getTotalAppointments(tenantId);
        Integer pendingAppointments = getPendingAppointments(tenantId);
        Integer completedAppointments = getCompletedAppointments(tenantId);
        Integer cancelledAppointments = getCancelledAppointments(tenantId);
        Integer todayAppointments = getTodayAppointments(tenantId);
        Integer completedAppointmentsThisMonth = getCompletedAppointmentsThisMonth(tenantId);
        Integer activeAlerts = getActiveAlerts(tenantId);
        Integer criticalAlerts = getCriticalAlerts(tenantId);
        Integer highPriorityAlerts = getHighPriorityAlerts(tenantId);
        BigDecimal totalRevenue = getTotalRevenue(tenantId);
        BigDecimal monthlyRevenue = getMonthlyRevenue(tenantId);
        
        // Build quota usage
        Integer maxDoctors = tenant.getMaxDoctors() != null ? tenant.getMaxDoctors() : 50;
        QuotaUsage quotaUsage = QuotaUsage.of(totalDoctors, maxDoctors, totalPatients, null);
        
        DashboardStats stats = new DashboardStats(
            tenantId,
            tenant.getName() != null ? tenant.getName().value() : "Unknown",
            totalDoctors,
            activeDoctors,
            inactiveDoctors,
            totalPatients,
            activePatients,
            newPatientsThisMonth,
            totalAppointments,
            pendingAppointments,
            completedAppointments,
            cancelledAppointments,
            todayAppointments,
            completedAppointmentsThisMonth,
            activeAlerts,
            criticalAlerts,
            highPriorityAlerts,
            tenant.getStatus() != null ? tenant.getStatus() : "active",
            "professional",
            LocalDateTime.now().plusMonths(1),
            totalRevenue,
            monthlyRevenue,
            quotaUsage
        );
        
        return Optional.of(stats);
    }
    
    private Integer getTotalDoctors(Long tenantId) {
        String sql = "SELECT COUNT(*) FROM doctors WHERE tenant_id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, tenantId);
    }
    
    private Integer getActiveDoctors(Long tenantId) {
        String sql = "SELECT COUNT(*) FROM doctors WHERE tenant_id = ? AND status = 'active'";
        return jdbcTemplate.queryForObject(sql, Integer.class, tenantId);
    }
    
    private Integer getTotalPatients(Long tenantId) {
        String sql = "SELECT COUNT(*) FROM patients WHERE tenant_id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, tenantId);
    }
    
    private Integer getActivePatients(Long tenantId) {
        String sql = "SELECT COUNT(*) FROM patients WHERE tenant_id = ? AND user_id IS NOT NULL";
        return jdbcTemplate.queryForObject(sql, Integer.class, tenantId);
    }
    
    private Integer getNewPatientsThisMonth(Long tenantId) {
        String sql = "SELECT COUNT(*) FROM patients WHERE tenant_id = ? AND created_at >= DATE_FORMAT(NOW(), '%Y-%m-01 00:00:00')";
        return jdbcTemplate.queryForObject(sql, Integer.class, tenantId);
    }
    
    private Integer getTotalAppointments(Long tenantId) {
        String sql = "SELECT COUNT(*) FROM appointments a INNER JOIN doctors d ON d.id = a.doctor_id WHERE d.tenant_id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, tenantId);
    }
    
    private Integer getPendingAppointments(Long tenantId) {
        String sql = "SELECT COUNT(*) FROM appointments a INNER JOIN doctors d ON d.id = a.doctor_id WHERE d.tenant_id = ? AND a.status = 'SCHEDULED'";
        return jdbcTemplate.queryForObject(sql, Integer.class, tenantId);
    }
    
    private Integer getCompletedAppointments(Long tenantId) {
        String sql = "SELECT COUNT(*) FROM appointments a INNER JOIN doctors d ON d.id = a.doctor_id WHERE d.tenant_id = ? AND a.status = 'COMPLETED'";
        return jdbcTemplate.queryForObject(sql, Integer.class, tenantId);
    }
    
    private Integer getCancelledAppointments(Long tenantId) {
        String sql = "SELECT COUNT(*) FROM appointments a INNER JOIN doctors d ON d.id = a.doctor_id WHERE d.tenant_id = ? AND a.status = 'CANCELLED'";
        return jdbcTemplate.queryForObject(sql, Integer.class, tenantId);
    }
    
    private Integer getTodayAppointments(Long tenantId) {
        String sql = "SELECT COUNT(*) FROM appointments a INNER JOIN doctors d ON d.id = a.doctor_id WHERE d.tenant_id = ? AND DATE(CONCAT(a.date, ' ', a.time)) = CURDATE()";
        return jdbcTemplate.queryForObject(sql, Integer.class, tenantId);
    }
    
    private Integer getCompletedAppointmentsThisMonth(Long tenantId) {
        String sql = "SELECT COUNT(*) FROM appointments a INNER JOIN doctors d ON d.id = a.doctor_id WHERE d.tenant_id = ? AND a.status = 'COMPLETED' AND STR_TO_DATE(CONCAT(a.date, ' ', a.time), '%Y-%m-%d %H:%i:%s') >= DATE_FORMAT(NOW(), '%Y-%m-01 00:00:00')";
        return jdbcTemplate.queryForObject(sql, Integer.class, tenantId);
    }
    
    private Integer getActiveAlerts(Long tenantId) {
        String sql = "SELECT COUNT(*) FROM alerts WHERE tenant_id = ? AND status = 'ACTIVE'";
        return jdbcTemplate.queryForObject(sql, Integer.class, tenantId);
    }
    
    private Integer getCriticalAlerts(Long tenantId) {
        String sql = "SELECT COUNT(*) FROM alerts WHERE tenant_id = ? AND status = 'ACTIVE' AND severity = 'CRITICAL'";
        return jdbcTemplate.queryForObject(sql, Integer.class, tenantId);
    }
    
    private Integer getHighPriorityAlerts(Long tenantId) {
        String sql = "SELECT COUNT(*) FROM alerts WHERE tenant_id = ? AND status = 'ACTIVE' AND severity = 'HIGH'";
        return jdbcTemplate.queryForObject(sql, Integer.class, tenantId);
    }
    
    private BigDecimal getTotalRevenue(Long tenantId) {
        String sql = "SELECT COALESCE(SUM(amount), 0) FROM payments WHERE payer_id = ? AND payer_type = 'TENANT' AND status = 'COMPLETED'";
        Double result = jdbcTemplate.queryForObject(sql, Double.class, tenantId);
        return result != null ? BigDecimal.valueOf(result) : BigDecimal.ZERO;
    }
    
    private BigDecimal getMonthlyRevenue(Long tenantId) {
        String sql = "SELECT COALESCE(SUM(amount), 0) FROM payments WHERE payer_id = ? AND payer_type = 'TENANT' AND status = 'COMPLETED' AND created_at >= DATE_FORMAT(NOW(), '%Y-%m-01 00:00:00')";
        Double result = jdbcTemplate.queryForObject(sql, Double.class, tenantId);
        return result != null ? BigDecimal.valueOf(result) : BigDecimal.ZERO;
    }
}
