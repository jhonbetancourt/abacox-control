package com.infomedia.abacox.control.component.wsserver;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Component
public class MessageBroadcaster {
    private final Sinks.Many<String> broadcaster = Sinks.many().multicast().onBackpressureBuffer();

    public void broadcast(String message) {
        broadcaster.tryEmitNext(message);
    }

    public void announceConnection(String clientId) {
        broadcast(String.format("User %s joined the server", clientId));
    }

    public void announceDisconnection(String clientId) {
        broadcast(String.format("User %s left the server", clientId));
    }

    public Flux<String> getMessageFlux() {
        return broadcaster.asFlux();
    }
}