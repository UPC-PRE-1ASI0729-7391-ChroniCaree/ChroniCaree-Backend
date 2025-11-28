/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.chronicare.platform.tenants.domain.aggregates;

import com.chronicare.platform.tenants.domain.valueobjects.TenantName;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.*;

@Entity
@Table(name = "tenants", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"name"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
}
