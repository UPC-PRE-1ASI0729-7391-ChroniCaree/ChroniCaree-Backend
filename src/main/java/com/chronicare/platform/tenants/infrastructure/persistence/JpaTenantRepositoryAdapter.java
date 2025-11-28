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

    @Override
    public List<Tenant> findAll() {
        return springRepo.findAll();
    }

    @Override
    public Optional<Tenant> findById(Long id) {
        return springRepo.findById(id);
    }

    @Override
    public Optional<Tenant> findByName(String name) {
        return springRepo.findByName(name);
    }

    @Override
    public Tenant save(Tenant tenant) {
        return springRepo.save(tenant);
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
