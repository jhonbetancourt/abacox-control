package com.infomedia.abacox.control.dto.moduleext;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ModuleInfo {
    private String name;
    private String type;
    private String description;
    private String version;
    private String prefix;
}
