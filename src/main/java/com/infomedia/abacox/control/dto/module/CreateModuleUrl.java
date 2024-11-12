package com.infomedia.abacox.control.dto.module;


import com.infomedia.abacox.control.entity.Module;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * DTO for {@link Module}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateModuleUrl {
    @NotBlank
    @Schema(description = "URL of the module", example = "http://localhost:8080")
    private String url;
}