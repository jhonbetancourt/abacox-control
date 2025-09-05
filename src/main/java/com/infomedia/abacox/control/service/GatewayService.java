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
                    .flatMap(def -> Mono.just(builder.routes()
                            .route(def.getId(), r -> r.path("/" + def.getPrefix() + def.getPath())
                                    .and()
                                    .method(HttpMethod.valueOf(def.getMethod()))
                                    .filters(f -> f.rewritePath("/" + def.getPrefix() + "(?<segment>.*)", "${segment}"))
                                    .uri(def.getBaseUrl()))
                            .build()
                            .getRoutes()))
                    .flatMap(Flux::from)
                    .collectList()
                            .toFuture().get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

        routes.set(newRoutes);

        // Publish event to refresh routes
        eventPublisher.publishEvent(new RefreshRoutesEvent(this));

        newRoutes.forEach(route -> log.info("Route: {}", route));

    }
}
