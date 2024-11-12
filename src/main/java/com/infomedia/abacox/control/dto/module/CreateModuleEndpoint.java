package com.infomedia.abacox.control.dto.module;

import com.infomedia.abacox.control.entity.ModuleEndpoint;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * DTO for {@link ModuleEndpoint}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateModuleEndpoint {
    @NotBlank
    @Size(max = 10)
    @Schema(description = "HTTP method", example = "GET")
    private String method;
    @NotBlank
    @Schema(description = "Path", example = "/api/test")
    private String path;
    @NotNull
    @Schema(description = "Secured", example = "false")
    private Boolean secured;
}