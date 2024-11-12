package com.infomedia.abacox.control.dto.module;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ModuleConnectedDto {
    private boolean connected;
}
