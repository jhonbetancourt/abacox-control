package com.infomedia.abacox.control.repository;

import com.infomedia.abacox.control.entity.ModuleEndpoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ModuleEndpointRepository extends JpaRepository<ModuleEndpoint, Long>
        , JpaSpecificationExecutor<ModuleEndpoint> {


  List<ModuleEndpoint> findBySecuredAndActive(boolean secured, boolean active);
}