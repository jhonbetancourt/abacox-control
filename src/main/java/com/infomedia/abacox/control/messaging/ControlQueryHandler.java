package com.infomedia.abacox.control.messaging;

import com.infomedia.abacox.control.service.ModuleService;
import com.infomedia.abacox.control.service.TenantAccessService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Handles inbound RPC queries directed at the control module.
 *
 * <p>Query types supported:
 * <ul>
 *   <li>{@code MODULE_INFO_BY_PREFIX_QUERY} — returns module info for a given prefix.
 *       Payload: {@code { "prefix": "users" }}
 *   </li>
 *   <li>{@code MODULE_ACCESS_QUERY} — returns the set of module prefixes a tenant has access to.
 *       Payload: {@code { "tenant": "schema_abc" }}
 *   </li>
 * </ul>
 */
@Component
@Log4j2
@RequiredArgsConstructor
public class ControlQueryHandler {

    private final ModuleService moduleService;
    private final TenantAccessService tenantAccessService;

    @RabbitListener(queues = RabbitMQConfig.CONTROL_QUERIES_QUEUE)
    public Object handleQuery(InternalMessage request) {
        log.debug("Received query [{}] from [{}]", request.getType(), request.getSourceModule());
        try {
            return switch (request.getType()) {
                case "MODULE_INFO_BY_PREFIX_QUERY" -> handleModuleInfoByPrefix(request);
                case "MODULE_ACCESS_QUERY" -> handleModuleAccess(request);
                default -> {
                    log.warn("Unknown query type [{}] from [{}]", request.getType(), request.getSourceModule());
                    yield Map.of("error", "Unknown query type: " + request.getType());
                }
            };
        } catch (Exception e) {
            log.error("Error handling query [{}]: {}", request.getType(), e.getMessage(), e);
            return Map.of("error", e.getMessage());
        }
    }

    private Object handleModuleInfoByPrefix(InternalMessage request) {
        String prefix = extractString(request, "prefix");
        if (prefix == null) {
            return Map.of("error", "prefix is required for MODULE_INFO_BY_PREFIX_QUERY");
        }
        return moduleService.getInfoByPrefix(prefix);
    }

    private Object handleModuleAccess(InternalMessage request) {
        String tenant = request.getTenant() != null
                ? request.getTenant()
                : extractString(request, "tenant");
        if (tenant == null) {
            return Map.of("error", "tenant is required for MODULE_ACCESS_QUERY");
        }
        return tenantAccessService.getModuleAccess(tenant);
    }

    @SuppressWarnings("unchecked")
    private String extractString(InternalMessage request, String key) {
        if (request.getPayload() instanceof Map<?, ?> map) {
            Object val = map.get(key);
            return val != null ? val.toString() : null;
        }
        return null;
    }
}
