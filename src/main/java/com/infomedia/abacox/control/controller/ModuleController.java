package com.infomedia.abacox.control.controller;

import com.infomedia.abacox.control.component.modeltools.ModelConverter;
import com.infomedia.abacox.control.component.springfilter.boot.Filter;
import com.infomedia.abacox.control.dto.module.CreateModuleUrl;
import com.infomedia.abacox.control.dto.module.ModuleConnectedDto;
import com.infomedia.abacox.control.dto.module.ModuleDto;
import com.infomedia.abacox.control.dto.module.ModuleEndpointDto;
import com.infomedia.abacox.control.dto.superclass.ActivationDto;
import com.infomedia.abacox.control.entity.Module;
import com.infomedia.abacox.control.entity.ModuleEndpoint;
import com.infomedia.abacox.control.service.ModuleService;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@SecurityRequirements({
        @SecurityRequirement(name = "JWT_Token"),
        @SecurityRequirement(name = "Internal_Api_Key")
})
@Tag(name = "Module", description = "Module controller")
@RequestMapping("/control/api/module")
public class ModuleController {

    private final ModuleService moduleService;
    private final ModelConverter modelConverter;


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ModuleDto create(@Valid @RequestBody CreateModuleUrl createModule) {
        return modelConverter.map(moduleService.create(createModule), ModuleDto.class);
    }

    @GetMapping(value = "/connected/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ModuleConnectedDto connected(@PathVariable UUID id) {
        return new ModuleConnectedDto(moduleService.isModuleConnected(id));
    }

    @PatchMapping(value = "/status/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ModuleDto active(@PathVariable UUID id, @Valid @RequestBody ActivationDto activationDto) {
        return modelConverter.map(moduleService.changeActivation(id, activationDto.getActive()), ModuleDto.class);
    }

    @PatchMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ModuleDto update(@PathVariable UUID id) {
        return modelConverter.map(moduleService.update(id), ModuleDto.class);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable UUID id) {
        moduleService.delete(id);
    }

    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    private ModuleDto get(@PathVariable("id") UUID id) {
        return modelConverter.map(moduleService.get(id), ModuleDto.class);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<ModuleDto> find(@Parameter(hidden = true) @Filter Specification<Module> spec
            , @Parameter(hidden = true) Pageable pageable
            , @RequestParam(required = false) String filter, @RequestParam(required = false) Integer page
            , @RequestParam(required = false) Integer size, @RequestParam(required = false) String sort) {
        return modelConverter.mapPage(moduleService.find(spec, pageable), ModuleDto.class);
    }

    @GetMapping(value = "endpoint", produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<ModuleEndpointDto> findEndpoint(@Parameter(hidden = true) @Filter Specification<ModuleEndpoint> spec
            , @Parameter(hidden = true) Pageable pageable
            , @RequestParam(required = false) String filter, @RequestParam(required = false) Integer page
            , @RequestParam(required = false) Integer size, @RequestParam(required = false) String sort) {
        return modelConverter.mapPage(moduleService.findEndpoint(spec, pageable), ModuleEndpointDto.class);
    }
}
