package com.infomedia.abacox.control.dto.module;

import com.infomedia.abacox.control.constants.ModuleType;
import com.infomedia.abacox.control.entity.Module;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

/**
 * DTO for {@link Module}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateModule {
    @NotBlank
    @Schema(description = "URL of the module", example = "http://localhost:8080")
    private String url;
    @NotBlank
    @Size(max = 50)
    @Schema(description = "Name of the module", example = "Module")
    private String name;
    @NotBlank
    @Size(max = 20)
    @Schema(description = "Prefix of the module", example = "module")
    private String prefix;
    @NotBlank
    @Size(max = 100)
    @Schema(description = "Description of the module", example = "Module description")
    private String description;
    @NotNull
    @Schema(description = "Type of the module", example = "STANDARD")
    private ModuleType type;
    @NotBlank
    @Size(max = 20)
    private String version;
    @Schema(description = "Endpoints of the module")
    private List<CreateModuleEndpoint> endpoints;
}