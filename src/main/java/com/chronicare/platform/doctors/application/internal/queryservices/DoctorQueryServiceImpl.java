package com.chronicare.platform.doctors.application.internal.queryservices;

import com.chronicare.platform.doctors.domain.model.aggregates.Doctor;
import com.chronicare.platform.doctors.domain.model.aggregates.DoctorDashboard;
import com.chronicare.platform.doctors.domain.model.queries.*;
import com.chronicare.platform.doctors.domain.model.valueobjects.AppointmentDetail;
import com.chronicare.platform.doctors.domain.model.valueobjects.PatientSummary;
import com.chronicare.platform.doctors.domain.model.valueobjects.ScheduleEntry;
import com.chronicare.platform.doctors.domain.services.DoctorQueryService;
import com.chronicare.platform.doctors.infrastructure.persistence.jpa.repositories.DoctorRepository;
import com.chronicare.platform.patients.domain.model.aggregates.Patient;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Summary: Doctor Query Service Implementation
 */
@Service
public class DoctorQueryServiceImpl implements DoctorQueryService {
    
    private final DoctorRepository doctorRepository;
    private final JdbcTemplate jdbcTemplate;
    
    public DoctorQueryServiceImpl(DoctorRepository doctorRepository, JdbcTemplate jdbcTemplate) {
        this.doctorRepository = doctorRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Doctor> handle(GetAllDoctorsQuery query) {
        return doctorRepository.findAll();
    }

    @Override
    public Optional<Doctor> handle(GetDoctorByIdQuery query) {
        return doctorRepository.findById(query.doctorId());
    }

    @Override
    public Optional<Doctor> handle(GetDoctorByUserIdQuery query) {
        return doctorRepository.findByUserId(query.userId());
    }

    @Override
    public List<Doctor> handle(GetDoctorsByTenantQuery query) {
        return doctorRepository.findByTenantId(query.tenantId());
    }

    @Override
    public List<Doctor> handle(GetDoctorsBySpecialtyQuery query) {
        return doctorRepository.findBySpecialty_Value(query.specialty());
    }

    @Override
    public List<Patient> handle(GetAssignedPatientsQuery query) {
        String sql = "SELECT * FROM patients WHERE assigned_doctor_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> Patient.builder()
                .userId(rs.getLong("user_id"))
                .tenantId(rs.getObject("tenant_id") != null ? rs.getLong("tenant_id") : null)
                .assignedDoctorId(rs.getLong("assigned_doctor_id"))
                .firstName(rs.getString("first_name"))
                .lastName(rs.getString("last_name"))
                .email(rs.getString("email"))
                .gender(rs.getString("gender"))
                .phone(rs.getString("phone"))
                .address(rs.getString("address"))
                .weight(rs.getObject("weight") != null ? rs.getDouble("weight") : null)
                .height(rs.getObject("height") != null ? rs.getDouble("height") : null)
                .photoUrl(rs.getString("photo_url"))
                .build(), 
            query.doctorId());
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<DoctorDashboard> handle(GetDoctorDashboardQuery query) {
        Long doctorId = query.doctorId();
        
        // Get doctor info
        var doctorOpt = doctorRepository.findById(doctorId);
        if (doctorOpt.isEmpty()) {
            return Optional.empty();
        }
        
        var doctor = doctorOpt.get();
        String doctorName = doctor.getFullName();
        
        // Get today's appointments
        List<AppointmentDetail> todayAppointments = getTodayAppointments(doctorId);
        
        // Get recent patients (last 10)
        List<PatientSummary> recentPatients = getRecentPatients(doctorId);
        
        // Get weekly schedule (hardcoded for now, should come from schedule table)
        List<ScheduleEntry> weeklySchedule = getWeeklySchedule(doctorId);
        
        // Get statistics
        Integer totalPatientsToday = getTotalPatientsToday(doctorId);
        Integer completedAppointmentsToday = getCompletedAppointmentsToday(doctorId);
        Integer pendingAppointmentsToday = getPendingAppointmentsToday(doctorId);
        Integer totalActivePatients = getTotalActivePatients(doctorId);
        Integer criticalAlerts = getCriticalAlerts(doctorId);
        Integer pendingTasks = getPendingTasks(doctorId);
        
        DoctorDashboard dashboard = new DoctorDashboard(
            doctorId,
            doctorName,
            todayAppointments,
            recentPatients,
            weeklySchedule,
            totalPatientsToday,
            completedAppointmentsToday,
            pendingAppointmentsToday,
            totalActivePatients,
            criticalAlerts,
            pendingTasks
        );
        
        return Optional.of(dashboard);
    }
    
    private List<AppointmentDetail> getTodayAppointments(Long doctorId) {
        String sql = """
            SELECT a.id, a.patient_id, CONCAT(p.first_name, ' ', p.last_name) as patient_name,
                   TIMESTAMPDIFF(YEAR, p.date_of_birth, CURDATE()) as patient_age,
                   STR_TO_DATE(CONCAT(a.date, ' ', a.time), '%Y-%m-%d %H:%i:%s') as appointment_datetime,
                   a.status, a.appointment_type, a.reason, a.location, a.notes,
                   CASE WHEN a.appointment_type = 'EMERGENCY' THEN 1 ELSE 0 END as is_urgent
            FROM appointments a
            INNER JOIN patients p ON p.id = a.patient_id
            WHERE a.doctor_id = ?
              AND DATE(CONCAT(a.date, ' ', a.time)) = CURDATE()
            ORDER BY appointment_datetime ASC
        """;
        
        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql, doctorId);
        List<AppointmentDetail> appointments = new ArrayList<>();
        
        for (Map<String, Object> row : results) {
            AppointmentDetail apt = new AppointmentDetail(
                ((Number) row.get("id")).longValue(),
                ((Number) row.get("patient_id")).longValue(),
                (String) row.get("patient_name"),
                row.get("patient_age") != null ? ((Number) row.get("patient_age")).intValue() : null,
                toLocalDateTime(row.get("appointment_datetime")),
                (String) row.get("status"),
                (String) row.get("appointment_type"),
                (String) row.get("reason"),
                (String) row.get("location"),
                (String) row.get("notes"),
                ((Number) row.get("is_urgent")).intValue() == 1
            );
            appointments.add(apt);
        }
        
        return appointments;
    }
    
    private List<PatientSummary> getRecentPatients(Long doctorId) {
        String sql = """
            SELECT DISTINCT p.id, CONCAT(p.first_name, ' ', p.last_name) as patient_name,
                   TIMESTAMPDIFF(YEAR, p.date_of_birth, CURDATE()) as age,
                   p.gender, p.blood_type, p.contact_phone,
                   MAX(STR_TO_DATE(CONCAT(a.date, ' ', a.time), '%Y-%m-%d %H:%i:%s')) as last_visit,
                   COUNT(a.id) as total_visits,
                   CASE WHEN EXISTS(SELECT 1 FROM alerts al WHERE al.patient_id = p.id AND al.status = 'ACTIVE') THEN 1 ELSE 0 END as has_active_alerts,
                   (SELECT COUNT(*) FROM alerts al WHERE al.patient_id = p.id AND al.status = 'ACTIVE' AND al.severity = 'CRITICAL') as critical_alerts,
                   p.current_condition
            FROM patients p
            INNER JOIN appointments a ON a.patient_id = p.id
            WHERE a.doctor_id = ?
            GROUP BY p.id, p.first_name, p.last_name, p.date_of_birth, p.gender, p.blood_type, p.contact_phone, p.current_condition
            ORDER BY last_visit DESC
            LIMIT 10
        """;
        
        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql, doctorId);
        List<PatientSummary> patients = new ArrayList<>();
        
        for (Map<String, Object> row : results) {
            PatientSummary pat = new PatientSummary(
                ((Number) row.get("id")).longValue(),
                (String) row.get("patient_name"),
                row.get("age") != null ? ((Number) row.get("age")).intValue() : null,
                (String) row.get("gender"),
                (String) row.get("blood_type"),
                (String) row.get("contact_phone"),
                toLocalDateTime(row.get("last_visit")),
                ((Number) row.get("total_visits")).intValue(),
                ((Number) row.get("has_active_alerts")).intValue() == 1,
                ((Number) row.get("critical_alerts")).intValue(),
                (String) row.get("current_condition")
            );
            patients.add(pat);
        }
        
        return patients;
    }
    
    private List<ScheduleEntry> getWeeklySchedule(Long doctorId) {
        // For now, return empty list. In production, query doctor_schedules table
        return List.of();
    }
    
    private Integer getTotalPatientsToday(Long doctorId) {
        String sql = "SELECT COUNT(DISTINCT patient_id) FROM appointments WHERE doctor_id = ? AND DATE(CONCAT(date, ' ', time)) = CURDATE()";
        return jdbcTemplate.queryForObject(sql, Integer.class, doctorId);
    }
    
    private Integer getCompletedAppointmentsToday(Long doctorId) {
        String sql = "SELECT COUNT(*) FROM appointments WHERE doctor_id = ? AND DATE(CONCAT(date, ' ', time)) = CURDATE() AND status = 'COMPLETED'";
        return jdbcTemplate.queryForObject(sql, Integer.class, doctorId);
    }
    
    private Integer getPendingAppointmentsToday(Long doctorId) {
        String sql = "SELECT COUNT(*) FROM appointments WHERE doctor_id = ? AND DATE(CONCAT(date, ' ', time)) = CURDATE() AND status IN ('SCHEDULED', 'CONFIRMED')";
        return jdbcTemplate.queryForObject(sql, Integer.class, doctorId);
    }
    
    private Integer getTotalActivePatients(Long doctorId) {
        String sql = "SELECT COUNT(DISTINCT patient_id) FROM appointments WHERE doctor_id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, doctorId);
    }
    
    private Integer getCriticalAlerts(Long doctorId) {
        String sql = """
            SELECT COUNT(*) FROM alerts a
            INNER JOIN appointments ap ON ap.patient_id = a.patient_id
            WHERE ap.doctor_id = ? AND a.status = 'ACTIVE' AND a.severity = 'CRITICAL'
        """;
        return jdbcTemplate.queryForObject(sql, Integer.class, doctorId);
    }
    
    private Integer getPendingTasks(Long doctorId) {
        // For now, count pending appointments as tasks
        String sql = "SELECT COUNT(*) FROM appointments WHERE doctor_id = ? AND status IN ('SCHEDULED', 'CONFIRMED') AND STR_TO_DATE(CONCAT(date, ' ', time), '%Y-%m-%d %H:%i:%s') >= NOW()";
        return jdbcTemplate.queryForObject(sql, Integer.class, doctorId);
    }
    
    private LocalDateTime toLocalDateTime(Object obj) {
        if (obj == null) return null;
        if (obj instanceof Timestamp timestamp) {
            return timestamp.toLocalDateTime();
        }
        if (obj instanceof LocalDateTime localdatetime) {
            return localdatetime;
        }
        return null;
    }
}
