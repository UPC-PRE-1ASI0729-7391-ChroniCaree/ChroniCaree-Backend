/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.tenants.infrastructure.persistence;

import com.example.tenants.domain.model.Tenant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.tenants.domain.repository.TenantRepository;


@Repository
public interface JpaTenantRepository extends JpaRepository<Tenant, Long>, TenantRepository {

    @Override
    Optional<Tenant> findByName(String name);

    // Los demás métodos ya están provistos por JpaRepository
    default List<Tenant> findAllTenants() {
        return findAll();
    }
}
