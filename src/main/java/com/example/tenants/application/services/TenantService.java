package com.example.tenants.application.services;

import com.example.tenants.domain.aggregates.Tenant;
import com.example.tenants.domain.commands.CreateTenantCommand;
import com.example.tenants.domain.commands.DeleteTenantCommand;
import com.example.tenants.domain.commands.UpdateTenantCommand;
import com.example.tenants.domain.events.TenantCreatedEvent;
import com.example.tenants.domain.repository.TenantRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.example.tenants.domain.repository.TenantRepository;
import java.util.List;
import java.util.Optional;

/**
 * TenantService - Lógica de aplicación para Tenants.
 */
@Service
@Transactional
public class TenantService {

    private final TenantRepository repository;
    private final ApplicationEventPublisher eventPublisher;

    public TenantService(TenantRepository repository, ApplicationEventPublisher eventPublisher) {
        this.repository = repository;
        this.eventPublisher = eventPublisher;
    }

    public List<Tenant> getAllTenants() {
        return repository.findAll();
    }

    public Optional<Tenant> getTenantById(Long id) {
        return repository.findById(id);
    }

    public Tenant createTenant(CreateTenantCommand command) {
        // Validar unicidad
        if (repository.findByName(command.name().value()).isPresent()) {
            throw new IllegalArgumentException("Tenant name already exists");
        }
        Tenant tenant = new Tenant(command.name());
        Tenant saved = repository.save(tenant);

        // Publicar evento
        eventPublisher.publishEvent(new TenantCreatedEvent(this, saved.getId(), saved.getName()));
        return saved;
    }

    public Tenant updateTenant(UpdateTenantCommand command) {
        Tenant existing = repository.findById(command.tenantId())
                .orElseThrow(() -> new IllegalArgumentException("Tenant not found"));

        // Validar si el nuevo nombre está en uso por otro tenant
        var maybeByName = repository.findByName(command.name().value());
        if (maybeByName.isPresent() && !maybeByName.get().getId().equals(existing.getId())) {
            throw new IllegalArgumentException("Tenant name already in use by another tenant");
        }

        existing.updateName(command.name());
        return repository.save(existing);
    }

    public void deleteTenant(DeleteTenantCommand command) {
        if (!repository.existsById(command.tenantId())) {
            throw new IllegalArgumentException("Tenant not found");
        }
        repository.deleteById(command.tenantId());
        // podrías publicar TenantDeletedEvent si lo deseas
    }
}
