/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.chronicare.platform.tenants.infrastructure.persistence;

 
 
 
import com.chronicare.platform.tenants.domain.aggregates.Tenant;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data repository puro para TenantEntity.
 */
@Repository
public interface SpringDataTenantEntityRepository extends JpaRepository<Tenant, Long> {

    Optional<Tenant> findByName(String name);
    Optional<Tenant> findByAdminUserId(Long adminUserId);
}
