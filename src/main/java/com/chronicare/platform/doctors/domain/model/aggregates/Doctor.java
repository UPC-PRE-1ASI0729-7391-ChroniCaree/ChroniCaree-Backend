package com.chronicare.platform.doctors.domain.model.aggregates;

import com.chronicare.platform.doctors.domain.model.valueobjects.*;
import com.chronicare.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Doctor Aggregate Root
 * Represents a doctor in the system (independent or tenant-based)
 */
@Entity
@Table(name = "doctors")
@Getter
public class Doctor extends AuditableAbstractAggregateRoot<Doctor> {

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "tenant_id", nullable = true)
    private Long tenantId;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "firstName", column = @Column(name = "first_name", nullable = false, length = 100)),
        @AttributeOverride(name = "lastName", column = @Column(name = "last_name", nullable = false, length = 100))
    })
    private PersonName name;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "dni", nullable = false, unique = true, length = 20))
    private DNI dni;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "specialty", nullable = false, length = 100))
    private Specialty specialty;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "license_number", nullable = false, unique = true, length = 20))
    private LicenseNumber licenseNumber;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "phone", nullable = false, length = 20))
    private PhoneNumber phone;

    @Column(name = "is_independent", nullable = false)
    private Boolean isIndependent;

    @Column(name = "is_verified", nullable = false)
    private Boolean isVerified;

    @Column(name = "accepting_patients", nullable = false)
    private Boolean acceptingPatients;

    @Column(name = "consultation_fee", nullable = true)
    private Double consultationFee;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "doctor_languages", joinColumns = @JoinColumn(name = "doctor_id"))
    @Column(name = "language")
    private List<String> languages = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "doctor_education", joinColumns = @JoinColumn(name = "doctor_id"))
    private List<Education> education = new ArrayList<>();

    protected Doctor() {
        this.isVerified = false;
        this.acceptingPatients = true;
        this.languages = new ArrayList<>();
        this.education = new ArrayList<>();
    }

    public Doctor(
        Long userId,
        Long tenantId,
        PersonName name,
        DNI dni,
        Specialty specialty,
        LicenseNumber licenseNumber,
        PhoneNumber phone,
        Double consultationFee,
        List<String> languages,
        List<Education> education
    ) {
        this();
        this.userId = userId;
        this.tenantId = tenantId;
        this.name = name;
        this.dni = dni;
        this.specialty = specialty;
        this.licenseNumber = licenseNumber;
        this.phone = phone;
        this.isIndependent = (tenantId == null);
        this.consultationFee = this.isIndependent ? consultationFee : null;
        this.languages = languages != null ? new ArrayList<>(languages) : new ArrayList<>();
        this.education = education != null ? new ArrayList<>(education) : new ArrayList<>();
    }

    // Business methods
    public void verify() {
        this.isVerified = true;
    }

    public void unverify() {
        this.isVerified = false;
        this.acceptingPatients = false;
    }

    public void enablePatientAcceptance() {
        if (!this.isVerified) {
            throw new IllegalStateException("Cannot accept patients: doctor is not verified");
        }
        this.acceptingPatients = true;
    }

    public void disablePatientAcceptance() {
        this.acceptingPatients = false;
    }

    public void updateProfile(
        PersonName name,
        PhoneNumber phone,
        Specialty specialty,
        Double consultationFee,
        List<String> languages
    ) {
        this.name = name;
        this.phone = phone;
        this.specialty = specialty;
        if (this.isIndependent && consultationFee != null) {
            this.consultationFee = consultationFee;
        }
        if (languages != null) {
            this.languages = new ArrayList<>(languages);
        }
    }

    public void addEducation(Education education) {
        this.education.add(education);
    }

    public void removeEducation(Education education) {
        this.education.remove(education);
    }

    public String getFullName() {
        return "Dr. " + name.getFullName();
    }

    public boolean canAcceptPatients() {
        return isVerified && acceptingPatients;
    }

    public boolean belongsToTenant() {
        return tenantId != null;
    }
}
