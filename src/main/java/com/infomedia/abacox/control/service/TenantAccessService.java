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
        // Try to find existing
        Optional<TenantModuleAccess> existing = getRepository()
                .findByTenantIdAndModulePrefix(dto.getTenantId(), dto.getModulePrefix());

        if (existing.isPresent()) {
            log.info("Rule already exists for {} -> {}. Ignoring creation.", dto.getTenantId(), dto.getModulePrefix());
            return existing.get();
        }

        TenantModuleAccess entity = TenantModuleAccess.builder()
                .tenantId(dto.getTenantId())
                .modulePrefix(dto.getModulePrefix())
                .build();

        TenantModuleAccess saved = save(entity);
        refreshCache(); 
        return saved;
    }

    @Transactional
    public List<TenantModuleAccess> createAll(List<CreateTenantModuleAccess> dtos) {
        List<TenantModuleAccess> toSave = new ArrayList<>();
        List<TenantModuleAccess> alreadyExisting = new ArrayList<>();

        for (CreateTenantModuleAccess dto : dtos) {
            // Check DB 
            Optional<TenantModuleAccess> existing = getRepository()
                    .findByTenantIdAndModulePrefix(dto.getTenantId(), dto.getModulePrefix());

            if (existing.isPresent()) {
                log.info("Rule already exists for {} -> {}. Skipping save.", dto.getTenantId(), dto.getModulePrefix());
                alreadyExisting.add(existing.get());
            } else {
                // Prepare new entity
                toSave.add(TenantModuleAccess.builder()
                        .tenantId(dto.getTenantId())
                        .modulePrefix(dto.getModulePrefix())
                        .build());
            }
        }

        List<TenantModuleAccess> saved = new ArrayList<>();
        if (!toSave.isEmpty()) {
            saved = getRepository().saveAll(toSave);
            refreshCache(); // Update Cache ONCE after batch if changes were made
        }

        // Return combined list of what was just saved + what was already there
        List<TenantModuleAccess> result = new ArrayList<>(saved);
        result.addAll(alreadyExisting);
        return result;
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
        refreshCache(); 
    }

    @Override
    @Transactional
    public TenantModuleAccess changeActivation(UUID id, boolean active) {
        TenantModuleAccess result = super.changeActivation(id, active);
        refreshCache();
        return result;
    }
}