/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.tenants.domain.repository;

import com.example.tenants.domain.aggregates.Tenant;
import java.util.List;
import java.util.Optional;

/**
 * Puerto de repositorio para Tenant (domain repository).
 *
 * Implementaciones adaptarán entre TenantEntity <-> Tenant aggregate.
 */
public interface TenantRepository {

    List<Tenant> findAll();

    Optional<Tenant> findById(Long id);

    Optional<Tenant> findByName(String name);

    Tenant save(Tenant tenant);

    void deleteById(Long id);

    boolean existsById(Long id);
}
