package com.infomedia.abacox.control.dto.gateway;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RouteDefinition {
    private String id;
    private String baseUrl;
    private String prefix;
    private String method;
    private String path;
}