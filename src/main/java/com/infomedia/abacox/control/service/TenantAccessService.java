package com.infomedia.abacox.control.service;

import com.infomedia.abacox.control.dto.tenantaccess.CreateTenantModuleAccess;
import com.infomedia.abacox.control.entity.TenantModuleAccess;
import com.infomedia.abacox.control.repository.TenantModuleAccessRepository;
import com.infomedia.abacox.control.service.common.CrudService;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@Log4j2
public class TenantAccessService extends CrudService<TenantModuleAccess, UUID, TenantModuleAccessRepository> {

    // Cache: Map<TenantId, Set<ModulePrefix>>
    private final Map<String, Set<String>> accessCache = new ConcurrentHashMap<>();

    public TenantAccessService(TenantModuleAccessRepository repository) {
        super(repository);
    }

    @PostConstruct
    public void initCache() {
        refreshCache();
    }

    public void refreshCache() {
        log.info("Refreshing Tenant Access Cache...");
        Map<String, Set<String>> newCache = getRepository().findByActiveTrue().stream()
                .collect(Collectors.groupingBy(
                        TenantModuleAccess::getTenantId,
                        Collectors.mapping(TenantModuleAccess::getModulePrefix, Collectors.toSet())
                ));
        
        // Clear and replace to ensure atomicity for readers
        accessCache.clear();
        accessCache.putAll(newCache);
        log.info("Tenant Access Cache Refreshed. Loaded rules for {} tenants.", accessCache.size());
    }

    public boolean isAccessAllowed(String tenantId, String modulePrefix) {
        Set<String> allowedModules = accessCache.get(tenantId);
        return allowedModules != null && allowedModules.contains(modulePrefix);
    }

    @Transactional
    public TenantModuleAccess create(CreateTenantModuleAccess dto) {
        // Prevent duplicates
        if (getRepository().existsByTenantIdAndModulePrefix(dto.getTenantId(), dto.getModulePrefix())) {
            // Depending on preference, you can throw exception or return existing. 
            // For single create, throwing exception is better.
            throw new IllegalArgumentException("Rule already exists for " + dto.getTenantId() + " -> " + dto.getModulePrefix());
        }

        TenantModuleAccess entity = TenantModuleAccess.builder()
                .tenantId(dto.getTenantId())
                .modulePrefix(dto.getModulePrefix())
                .build();

        TenantModuleAccess saved = save(entity);
        refreshCache(); // Update Cache immediately
        return saved;
    }

    @Transactional
    public List<TenantModuleAccess> createAll(List<CreateTenantModuleAccess> dtos) {
        List<TenantModuleAccess> toSave = new ArrayList<>();
        
        for (CreateTenantModuleAccess dto : dtos) {
            // Check DB to avoid UniqueConstraint violations
            if (!getRepository().existsByTenantIdAndModulePrefix(dto.getTenantId(), dto.getModulePrefix())) {
                toSave.add(TenantModuleAccess.builder()
                        .tenantId(dto.getTenantId())
                        .modulePrefix(dto.getModulePrefix())
                        .build());
            } else {
                log.warn("Skipping duplicate rule creation for Tenant: {} Prefix: {}", dto.getTenantId(), dto.getModulePrefix());
            }
        }

        if (toSave.isEmpty()) {
            return Collections.emptyList();
        }

        List<TenantModuleAccess> saved = getRepository().saveAll(toSave);
        refreshCache(); // Update Cache ONCE after batch
        return saved;
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        super.deleteById(id);
        refreshCache();
    }

    @Transactional
    public void deleteAll(List<UUID> ids) {
        getRepository().deleteAllById(ids);
        refreshCache(); // Update Cache ONCE after batch
    }

    @Override
    @Transactional
    public TenantModuleAccess changeActivation(UUID id, boolean active) {
        TenantModuleAccess result = super.changeActivation(id, active);
        refreshCache();
        return result;
    }
}