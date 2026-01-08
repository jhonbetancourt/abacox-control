package com.infomedia.abacox.control.service;

import com.infomedia.abacox.control.dto.gateway.RouteDefinition;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;

@RequiredArgsConstructor
@Service
@Log4j2
public class GatewayService implements RouteLocator {

    private final RouteLocatorBuilder builder;
    private final AtomicReference<List<Route>> routes = new AtomicReference<>(new ArrayList<>());
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public Flux<Route> getRoutes() {
        return Flux.fromIterable(routes.get());
    }

    public void updateRoutes(List<RouteDefinition> definitions) {

        List<Route> newRoutes;
        try {
            newRoutes = Flux.fromIterable(definitions)
                    .flatMap(def -> {
                        // FIX: Use a unique variable name '{gatewayTenantId}' to avoid collision
                        // if the downstream 'def.getPath()' also contains a {tenantId} variable.
                        String matchPath = "/{gatewayTenantId}/" + def.getPrefix() + def.getPath();

                        // Regex to strip the prefix. Matches: /any-id/prefix/segment
                        String rewriteRegex = "/(?<gTId>[^/]+)/" + def.getPrefix() + "(?<segment>.*)";

                        return Mono.just(builder.routes()
                                .route(def.getId(), r -> r.path(matchPath)
                                        .and()
                                        .method(HttpMethod.valueOf(def.getMethod()))
                                        .filters(f -> f
                                                // 1. Extract tenantId from URL and add X-Tenant-Id Header
                                                .filter((exchange, chain) -> {
                                                    String path = exchange.getRequest().getURI().getPath();
                                                    // Path structure: /{tenantId}/{prefix}/...
                                                    // Split: ["", "tenantId", ...]
                                                    String[] parts = path.split("/");
                                                    if (parts.length > 1) {
                                                        String tenantId = parts[1];
                                                        ServerHttpRequest request = exchange.getRequest().mutate()
                                                                .header("X-Tenant-Id", tenantId)
                                                                .build();
                                                        return chain.filter(exchange.mutate().request(request).build());
                                                    }
                                                    return chain.filter(exchange);
                                                })
                                                // 2. Rewrite path: Keep only the ${segment}
                                                .rewritePath(rewriteRegex, "${segment}")
                                        )
                                        .uri(def.getBaseUrl()))
                                .build()
                                .getRoutes());
                    })
                    .flatMap(Flux::from)
                    .collectList()
                    .toFuture().get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Route update was interrupted", e);
        } catch (ExecutionException e) {
            throw new RuntimeException("Failed to update routes", e.getCause());
        }

        routes.set(newRoutes);
        eventPublisher.publishEvent(new RefreshRoutesEvent(this));

        log.info("Gateway routes updated. Total count: {}", newRoutes.size());
    }
}