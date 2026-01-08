package com.infomedia.abacox.control.controller;

import com.infomedia.abacox.control.component.modeltools.ModelConverter;
import com.infomedia.abacox.control.component.springfilter.boot.Filter;
import com.infomedia.abacox.control.dto.superclass.ActivationDto;
import com.infomedia.abacox.control.dto.tenantaccess.CreateTenantModuleAccess;
import com.infomedia.abacox.control.dto.tenantaccess.TenantModuleAccessDto;
import com.infomedia.abacox.control.entity.TenantModuleAccess;
import com.infomedia.abacox.control.service.TenantAccessService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@SecurityRequirements({
        @SecurityRequirement(name = "JWT_Token"),
        @SecurityRequirement(name = "Internal_Api_Key")
})
@Tag(name = "Tenant Access", description = "Manage Tenant access to Module Prefixes")
@RequestMapping("/control/api/tenantAccess")
public class TenantAccessController {

    private final TenantAccessService tenantAccessService;
    private final ModelConverter modelConverter;

    // --- Single Operations ---

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public TenantModuleAccessDto create(@Valid @RequestBody CreateTenantModuleAccess createDto) {
        return modelConverter.map(tenantAccessService.create(createDto), TenantModuleAccessDto.class);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        tenantAccessService.deleteById(id);
    }

    // --- Batch Operations ---

    @Operation(summary = "Batch Create", description = "Add multiple access rules at once. Skips existing rules.")
    @PostMapping(value = "/batch", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public List<TenantModuleAccessDto> createBatch(@Valid @RequestBody List<CreateTenantModuleAccess> createDtos) {
        return modelConverter.mapList(tenantAccessService.createAll(createDtos), TenantModuleAccessDto.class);
    }

    @Operation(summary = "Batch Delete", description = "Delete multiple access rules by ID")
    @DeleteMapping(value = "/batch", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBatch(@RequestBody List<UUID> ids) {
        tenantAccessService.deleteAll(ids);
    }

    // --- Standard Operations ---

    @PatchMapping(value = "/status/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public TenantModuleAccessDto active(@PathVariable UUID id, @Valid @RequestBody ActivationDto activationDto) {
        return modelConverter.map(tenantAccessService.changeActivation(id, activationDto.getActive()), TenantModuleAccessDto.class);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<TenantModuleAccessDto> find(@Parameter(hidden = true) @Filter Specification<TenantModuleAccess> spec
            , @Parameter(hidden = true) Pageable pageable) {
        return modelConverter.mapPage(tenantAccessService.find(spec, pageable), TenantModuleAccessDto.class);
    }
}