/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.tenants.domain.aggregates;

import com.example.tenants.domain.valueobjects.TenantName;
import jakarta.persistence.*;
import java.util.Objects;
import lombok.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
