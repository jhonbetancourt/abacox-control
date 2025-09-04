package com.infomedia.abacox.control.dto.module;

import com.infomedia.abacox.control.constants.ModuleType;
import com.infomedia.abacox.control.dto.superclass.ActivableDto;
import com.infomedia.abacox.control.entity.Module;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

/**
 * DTO for {@link Module}
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModuleDto extends ActivableDto {
    private UUID id;
    private String name;
    private String prefix;
    private String description;
    private ModuleType type;
    private String version;
    private String url;
}