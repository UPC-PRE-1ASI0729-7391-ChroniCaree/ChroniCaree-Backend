/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.chronicare.platform.tenants.domain.aggregates;

import com.chronicare.platform.tenants.domain.valueobjects.TenantName;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "tenants", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"name"})
})
public class Tenant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(name = "admin_user_id")
    private Long adminUserId;

    @Column(length = 255)
    private String email;

    @Column(length = 255)
    private String address;

    @Column(length = 50)
    private String phone;

    @Column(length = 50)
    private String status;

    @Column(name = "subscription_id")
    private Long subscriptionId;

    @Column(name = "registration_date")
    private LocalDateTime registrationDate;

    // Settings fields
    @Column(name = "allow_independent_doctors")
    private Boolean allowIndependentDoctors;

    @Column(name = "require_patient_approval")
    private Boolean requirePatientApproval;

    @Column(name = "max_doctors")
    private Integer maxDoctors;

    public Tenant() {
    }

    public Tenant(Long id, String name, Long adminUserId, String email, String address, String phone, String status, Long subscriptionId, LocalDateTime registrationDate, Boolean allowIndependentDoctors, Boolean requirePatientApproval, Integer maxDoctors) {
        this.id = id;
        this.name = name;
        this.adminUserId = adminUserId;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.status = status;
        this.subscriptionId = subscriptionId;
        this.registrationDate = registrationDate;
        this.allowIndependentDoctors = allowIndependentDoctors;
        this.requirePatientApproval = requirePatientApproval;
        this.maxDoctors = maxDoctors;
    }

    public static TenantBuilder builder() {
        return new TenantBuilder();
    }

    public static class TenantBuilder {
        private Long id;
        private String name;
        private Long adminUserId;
        private String email;
        private String address;
        private String phone;
        private String status;
        private Long subscriptionId;
        private LocalDateTime registrationDate;
        private Boolean allowIndependentDoctors;
        private Boolean requirePatientApproval;
        private Integer maxDoctors;

        TenantBuilder() {
        }

        public TenantBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public TenantBuilder name(String name) {
            this.name = name;
            return this;
        }

        public TenantBuilder adminUserId(Long adminUserId) {
            this.adminUserId = adminUserId;
            return this;
        }

        public TenantBuilder email(String email) {
            this.email = email;
            return this;
        }

        public TenantBuilder address(String address) {
            this.address = address;
            return this;
        }

        public TenantBuilder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public TenantBuilder status(String status) {
            this.status = status;
            return this;
        }

        public TenantBuilder subscriptionId(Long subscriptionId) {
            this.subscriptionId = subscriptionId;
            return this;
        }

        public TenantBuilder registrationDate(LocalDateTime registrationDate) {
            this.registrationDate = registrationDate;
            return this;
        }

        public TenantBuilder allowIndependentDoctors(Boolean allowIndependentDoctors) {
            this.allowIndependentDoctors = allowIndependentDoctors;
            return this;
        }

        public TenantBuilder requirePatientApproval(Boolean requirePatientApproval) {
            this.requirePatientApproval = requirePatientApproval;
            return this;
        }

        public TenantBuilder maxDoctors(Integer maxDoctors) {
            this.maxDoctors = maxDoctors;
            return this;
        }

        public Tenant build() {
            return new Tenant(id, name, adminUserId, email, address, phone, status, subscriptionId, registrationDate, allowIndependentDoctors, requirePatientApproval, maxDoctors);
        }
    }

    public Tenant(Long id, TenantName name) {
        this.id = id;
        this.name = name.value();
    }

    public Tenant(TenantName name) {
        this(null, name);
    }

    public Long getId() {
        return id;
    }

    public TenantName getName() {
        return new TenantName(this.name);
    }

    public Tenant updateName(TenantName newName) {
        this.name = Objects.requireNonNull(newName, "name cannot be null").value(); 
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAdminUserId() {
        return adminUserId;
    }

    public void setAdminUserId(Long adminUserId) {
        this.adminUserId = adminUserId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(Long subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Boolean getAllowIndependentDoctors() {
        return allowIndependentDoctors;
    }

    public void setAllowIndependentDoctors(Boolean allowIndependentDoctors) {
        this.allowIndependentDoctors = allowIndependentDoctors;
    }

    public Boolean getRequirePatientApproval() {
        return requirePatientApproval;
    }

    public void setRequirePatientApproval(Boolean requirePatientApproval) {
        this.requirePatientApproval = requirePatientApproval;
    }

    public Integer getMaxDoctors() {
        return maxDoctors;
    }

    public void setMaxDoctors(Integer maxDoctors) {
        this.maxDoctors = maxDoctors;
    }
}
