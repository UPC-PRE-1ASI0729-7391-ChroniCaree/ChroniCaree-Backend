package com.chronicare.platform.patients.application.internal.queryservices;

import com.chronicare.platform.patients.domain.model.aggregates.Patient;
import com.chronicare.platform.patients.domain.model.aggregates.PatientDashboard;
import com.chronicare.platform.patients.domain.model.queries.GetPatientDashboardQuery;
import com.chronicare.platform.patients.domain.model.valueobjects.AlertSummary;
import com.chronicare.platform.patients.domain.model.valueobjects.AppointmentSummary;
import com.chronicare.platform.patients.domain.model.valueobjects.MedicationSummary;
import com.chronicare.platform.patients.domain.model.valueobjects.VitalSignsSummary;
import com.chronicare.platform.patients.domain.queries.GetAllPatientsQuery;
import com.chronicare.platform.patients.domain.queries.GetPatientByIdQuery;
import com.chronicare.platform.patients.domain.queries.GetPatientByUserIdQuery;
import com.chronicare.platform.patients.domain.repository.PatientRepository;
import com.chronicare.platform.patients.domain.services.PatientQueryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Implementation of the PatientQueryService.
 *
 * @summary
 * Provides read-side operations for retrieving patient data, supporting both
 * simple queries and complex dashboard aggregation using SQL-based projections.
 * Responsibilities:
 * - Fetch patient entities by ID, userId, and tenant with pagination support.
 * - Build the PatientDashboard view by aggregating appointments, medications,
 *   alerts, vital signs, and patient statistics using optimized JDBC queries.
 * - Convert raw SQL result sets into strongly typed domain value objects.
 * Features:
 * - Efficient read-only transactions for all query operations.
 * - Uses JdbcTemplate for high-performance joins and summary projections.
 * - Ensures clean separation between command and query responsibilities (CQRS).
 * Notes:
 * - Dashboard enrichment includes: upcoming appointments, active medications,
 *   active alerts, latest vital signs, and several aggregate counters.
 * - All date, numeric, and null conversions are safely normalized to domain types.
 */


@Service
public class PatientQueryServiceImpl implements PatientQueryService {
    private final PatientRepository patientRepository;
    private final JdbcTemplate jdbcTemplate;

    public PatientQueryServiceImpl(PatientRepository patientRepository, JdbcTemplate jdbcTemplate) {
        this.patientRepository = patientRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Patient> handle(GetAllPatientsQuery query) {
        return patientRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Patient> handle(GetPatientByIdQuery query) {
        return patientRepository.findById(query.patientId());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Patient> handle(GetPatientByUserIdQuery query) {
        return patientRepository.findByUserId(query.userId());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Patient> handleByTenantId(Long tenantId, int page, int limit) {
        return patientRepository.findByTenantId(tenantId, PageRequest.of(page, limit));
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<PatientDashboard> handle(GetPatientDashboardQuery query) {
        Long patientId = query.patientId();
        
        // Get patient basic info
        Optional<Patient> patientOpt = patientRepository.findById(patientId);
        if (patientOpt.isEmpty()) {
            return Optional.empty();
        }
        
        Patient patient = patientOpt.get();
        String patientName = patient.getFirstName() + " " + patient.getLastName();
        
        // Get upcoming appointments (next 30 days, limit 5)
        List<AppointmentSummary> upcomingAppointments = getUpcomingAppointments(patientId);
        
        // Get active medications (limit 10)
        List<MedicationSummary> activeMedications = getActiveMedications(patientId);
        
        // Get active alerts (limit 5)
        List<AlertSummary> activeAlerts = getActiveAlerts(patientId);
        
        // Get latest vital signs
        VitalSignsSummary latestVitalSigns = getLatestVitalSigns(patientId);
        
        // Get statistics
        Integer totalAppointments = getTotalAppointments(patientId);
        Integer pendingAppointments = getPendingAppointments(patientId);
        Integer totalMedications = getTotalMedications(patientId);
        Integer criticalAlerts = getCriticalAlerts(patientId);
        
        PatientDashboard dashboard = new PatientDashboard(
            patientId,
            patientName,
            upcomingAppointments,
            activeMedications,
            activeAlerts,
            latestVitalSigns,
            totalAppointments,
            pendingAppointments,
            totalMedications,
            criticalAlerts
        );
        
        return Optional.of(dashboard);
    }
    
    private List<AppointmentSummary> getUpcomingAppointments(Long patientId) {
        String sql = """
            SELECT a.id, a.doctor_id, CONCAT(d.first_name, ' ', d.last_name) as doctor_name,
                   d.specialty, STR_TO_DATE(CONCAT(a.date, ' ', a.time), '%Y-%m-%d %H:%i:%s') as appointment_datetime,
                   a.status, a.appointment_type, a.location, a.notes
            FROM appointments a
            INNER JOIN doctors d ON d.id = a.doctor_id
            WHERE a.patient_id = ?
              AND STR_TO_DATE(CONCAT(a.date, ' ', a.time), '%Y-%m-%d %H:%i:%s') >= NOW()
              AND a.status IN ('SCHEDULED', 'CONFIRMED')
            ORDER BY appointment_datetime ASC
            LIMIT 5
        """;
        
        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql, patientId);
        List<AppointmentSummary> appointments = new ArrayList<>();
        
        for (Map<String, Object> row : results) {
            AppointmentSummary apt = new AppointmentSummary(
                ((Number) row.get("id")).longValue(),
                ((Number) row.get("doctor_id")).longValue(),
                (String) row.get("doctor_name"),
                (String) row.get("specialty"),
                toLocalDateTime(row.get("appointment_datetime")),
                (String) row.get("status"),
                (String) row.get("appointment_type"),
                (String) row.get("location"),
                (String) row.get("notes")
            );
            appointments.add(apt);
        }
        
        return appointments;
    }
    
    private List<MedicationSummary> getActiveMedications(Long patientId) {
        String sql = """
            SELECT m.id, m.medication_name, m.dosage, m.frequency, m.start_date, m.end_date,
                   CONCAT(d.first_name, ' ', d.last_name) as prescribed_by, m.instructions,
                   CASE WHEN m.status = 'ACTIVE' THEN 1 ELSE 0 END as is_active
            FROM medications m
            INNER JOIN doctors d ON d.id = m.doctor_id
            WHERE m.patient_id = ?
              AND m.status = 'ACTIVE'
            ORDER BY m.start_date DESC
            LIMIT 10
        """;
        
        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql, patientId);
        List<MedicationSummary> medications = new ArrayList<>();
        
        for (Map<String, Object> row : results) {
            MedicationSummary med = new MedicationSummary(
                ((Number) row.get("id")).longValue(),
                (String) row.get("medication_name"),
                (String) row.get("dosage"),
                (String) row.get("frequency"),
                toLocalDateTime(row.get("start_date")),
                toLocalDateTime(row.get("end_date")),
                (String) row.get("prescribed_by"),
                (String) row.get("instructions"),
                ((Number) row.get("is_active")).intValue() == 1
            );
            medications.add(med);
        }
        
        return medications;
    }
    
    private List<AlertSummary> getActiveAlerts(Long patientId) {
        String sql = """
            SELECT id, alert_type, severity, message, triggered_at, is_read, action_required, dismissed_at
            FROM alerts
            WHERE patient_id = ?
              AND status = 'ACTIVE'
            ORDER BY severity DESC, triggered_at DESC
            LIMIT 5
        """;
        
        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql, patientId);
        List<AlertSummary> alerts = new ArrayList<>();
        
        for (Map<String, Object> row : results) {
            AlertSummary alert = new AlertSummary(
                ((Number) row.get("id")).longValue(),
                (String) row.get("alert_type"),
                (String) row.get("severity"),
                (String) row.get("message"),
                toLocalDateTime(row.get("triggered_at")),
                (Boolean) row.get("is_read"),
                (String) row.get("action_required"),
                toLocalDateTime(row.get("dismissed_at"))
            );
            alerts.add(alert);
        }
        
        return alerts;
    }
    
    private VitalSignsSummary getLatestVitalSigns(Long patientId) {
        String sql = """
            SELECT id, blood_pressure_systolic, blood_pressure_diastolic, heart_rate, temperature,
                   weight, height, bmi, measured_at, measured_by, notes
            FROM vital_signs
            WHERE patient_id = ?
            ORDER BY measured_at DESC
            LIMIT 1
        """;
        
        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql, patientId);
        if (results.isEmpty()) {
            return null;
        }
        
        Map<String, Object> row = results.get(0);
        return new VitalSignsSummary(
            ((Number) row.get("id")).longValue(),
            toBigDecimal(row.get("blood_pressure_systolic")),
            toBigDecimal(row.get("blood_pressure_diastolic")),
            toBigDecimal(row.get("heart_rate")),
            toBigDecimal(row.get("temperature")),
            toBigDecimal(row.get("weight")),
            toBigDecimal(row.get("height")),
            toBigDecimal(row.get("bmi")),
            toLocalDateTime(row.get("measured_at")),
            (String) row.get("measured_by"),
            (String) row.get("notes")
        );
    }
    
    private Integer getTotalAppointments(Long patientId) {
        String sql = "SELECT COUNT(*) FROM appointments WHERE patient_id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, patientId);
    }
    
    private Integer getPendingAppointments(Long patientId) {
        String sql = "SELECT COUNT(*) FROM appointments WHERE patient_id = ? AND status IN ('SCHEDULED', 'CONFIRMED')";
        return jdbcTemplate.queryForObject(sql, Integer.class, patientId);
    }
    
    private Integer getTotalMedications(Long patientId) {
        String sql = "SELECT COUNT(*) FROM medications WHERE patient_id = ? AND status = 'ACTIVE'";
        return jdbcTemplate.queryForObject(sql, Integer.class, patientId);
    }
    
    private Integer getCriticalAlerts(Long patientId) {
        String sql = "SELECT COUNT(*) FROM alerts WHERE patient_id = ? AND status = 'ACTIVE' AND severity = 'CRITICAL'";
        return jdbcTemplate.queryForObject(sql, Integer.class, patientId);
    }
    
    private LocalDateTime toLocalDateTime(Object obj) {
        if (obj == null) return null;
        if (obj instanceof Timestamp) {
            return ((Timestamp) obj).toLocalDateTime();
        }
        if (obj instanceof LocalDateTime) {
            return (LocalDateTime) obj;
        }
        return null;
    }
    
    private BigDecimal toBigDecimal(Object obj) {
        if (obj == null) return null;
        if (obj instanceof BigDecimal) {
            return (BigDecimal) obj;
        }
        if (obj instanceof Number) {
            return BigDecimal.valueOf(((Number) obj).doubleValue());
        }
        return null;
    }
}
