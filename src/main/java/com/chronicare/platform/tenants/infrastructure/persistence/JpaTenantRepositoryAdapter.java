/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.chronicare.platform.tenants.infrastructure.persistence;

import com.chronicare.platform.tenants.domain.aggregates.Tenant;
import com.chronicare.platform.tenants.domain.repository.TenantRepository;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adaptador que implementa el puerto TenantRepository usando
 * SpringDataTenantEntityRepository.
 */
@Component
public class JpaTenantRepositoryAdapter implements TenantRepository {

    private final SpringDataTenantEntityRepository springRepo;

    public JpaTenantRepositoryAdapter(SpringDataTenantEntityRepository springRepo) {
        this.springRepo = springRepo;
    }

    private Tenant toAggregate(Tenant e) {
        var name = e.getName();
        var aggName = new com.chronicare.platform.tenants.domain.valueobjects.TenantName(name.value()); ;
        var t = new Tenant(e.getId(), aggName);
        return t;
    }

    private Tenant toEntity(Tenant t) {
        return Tenant.builder()
                .id(t.getId())
                .name(t.getName().value())
                .build();
    }

    @Override
    public List<Tenant> findAll() {
        return springRepo.findAll().stream().map(this::toAggregate).collect(Collectors.toList());
    }

    @Override
    public Optional<Tenant> findById(Long id) {
        return springRepo.findById(id).map(this::toAggregate);
    }

    @Override
    public Optional<Tenant> findByName(String name) {
        return springRepo.findByName(name).map(this::toAggregate);
    }

    @Override
    public Tenant save(Tenant tenant) {
        Tenant entity = toEntity(tenant);
        Tenant saved = springRepo.save(entity);
        return toAggregate(saved);
    }

    @Override
    public void deleteById(Long id) {
        springRepo.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return springRepo.existsById(id);
    }
}
