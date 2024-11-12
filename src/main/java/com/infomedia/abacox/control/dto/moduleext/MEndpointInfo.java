package com.infomedia.abacox.control.dto.moduleext;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class MEndpointInfo {
    private String method;
    private String path;
    private boolean secured;
}
