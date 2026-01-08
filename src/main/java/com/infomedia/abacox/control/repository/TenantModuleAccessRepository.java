package com.infomedia.abacox.control.repository;

import com.infomedia.abacox.control.entity.TenantModuleAccess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TenantModuleAccessRepository extends JpaRepository<TenantModuleAccess, UUID>, JpaSpecificationExecutor<TenantModuleAccess> {
    boolean existsByTenantIdAndModulePrefix(String tenantId, String modulePrefix);
    List<TenantModuleAccess> findByActiveTrue();
}