package com.infomedia.abacox.control.dto.module;

import com.infomedia.abacox.control.dto.superclass.ActivableDto;
import com.infomedia.abacox.control.entity.ModuleEndpoint;
import lombok.*;

/**
 * DTO for {@link ModuleEndpoint}
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModuleEndpointDto extends ActivableDto {
    private Long id;
    private String method;
    private String path;
    private boolean secured;
}