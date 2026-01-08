package com.infomedia.abacox.control.dto.tenantaccess;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateTenantModuleAccess {
    @NotBlank
    @Schema(description = "The Tenant Identifier", example = "client-a")
    private String tenantId;

    @NotBlank
    @Schema(description = "The Module Prefix", example = "users")
    private String modulePrefix;
}