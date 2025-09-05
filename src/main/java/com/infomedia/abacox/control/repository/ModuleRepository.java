package com.infomedia.abacox.control.repository;

import com.infomedia.abacox.control.constants.ModuleType;
import com.infomedia.abacox.control.entity.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ModuleRepository extends JpaRepository<Module, UUID>, JpaSpecificationExecutor<Module> {

    boolean existsByType(ModuleType type);

    Optional<Module> findByType(ModuleType type);

    List<Module> findByTypeNotAndActive(ModuleType type, boolean active);

    boolean existsByTypeAndActive(ModuleType type, boolean active);

    boolean existsByTypeAndIdNot(ModuleType type, UUID id);

    Optional<Module> findByPrefixAndActive(String prefix, boolean active);

    List<Module> findByActive(boolean active);


}