package com.infomedia.abacox.control.component.wsserver;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.net.URI;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Configuration
public class WebSocketServer implements WebSocketHandler {
    private static final Logger logger = Logger.getLogger(WebSocketServer.class.getName());
    private final Map<String, WSSession> sessions = new ConcurrentHashMap<>();
    private final Sinks.Many<String> broadcaster = Sinks.many().multicast().onBackpressureBuffer();

    @Bean
    public HandlerMapping handlerMapping() {
        // Support for path parameters using pattern
        Map<String, WebSocketHandler> map = Collections.singletonMap("/control/websocket/{module}/{channel}/{target}", this);
        SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
        mapping.setUrlMap(map);
        mapping.setOrder(-1);
        return mapping;
    }

    @Bean
    public WebSocketHandlerAdapter handlerAdapter() {
        return new WebSocketHandlerAdapter();
    }

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        URI uri = session.getHandshakeInfo().getUri();
        String path = uri.getPath();
        String query = uri.getQuery();

        // Extract query parameters
        Map<String, String> queryParams = parseQueryString(query);
        // Extract path parameters
        Map<String, String> pathParams = extractPathParameters(path);

        if(pathParams.isEmpty()) {
            logger.info(String.format("Invalid WebSocket path: %s", path));
            return session.close();
        }
        
        String clientId= session.getId();
        logger.info(String.format("New WebSocket connection - Client ID: %s, Path: %s, Query: %s, Path Params: %s"
                , clientId, path, query, pathParams));

        WSSession wsSession = WSSession.builder()
                .clientId(clientId)
                .session(session)
                .module(pathParams.get("module"))
                .channel(pathParams.get("channel"))
                .target(pathParams.get("target"))
                .params(queryParams)
                .build();

        sessions.put(clientId, wsSession);

        Mono<Void> input = session.receive()
            .doOnSubscribe(sub -> handleConnect(clientId, queryParams))
            .doOnNext(message -> handleMessage(message, clientId))
            .doOnError(error -> handleError(error, clientId))
            .doOnComplete(() -> handleDisconnect(clientId))
            .then();

        Mono<Void> output = session.send(
            broadcaster.asFlux()
                .map(session::textMessage)
                .doOnError(error -> handleError(error, clientId))
        );

        return Mono.zip(input, output)
            .doOnError(error -> handleError(error, clientId))
            .then();
    }

    private Map<String, String> parseQueryString(String query) {
        Map<String, String> queryParams = new ConcurrentHashMap<>();
        if (query != null && !query.isEmpty()) {
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=");
                if (keyValue.length == 2) {
                    queryParams.put(keyValue[0], keyValue[1]);
                }
            }
        }
        return queryParams;
    }

    private Map<String, String> extractPathParameters(String path) {
        Map<String, String> pathParams = new ConcurrentHashMap<>();

        // Define the pattern that matches your path template
        Pattern pattern = Pattern.compile("control/websocket/([^/]+)/([^/]+)/([^/]+)");
        Matcher matcher = pattern.matcher(path);

        if (matcher.find()) {
            pathParams.put("module", matcher.group(1));
            pathParams.put("channel", matcher.group(2));
            pathParams.put("target", matcher.group(3));
        }

        return pathParams;
    }


    private void handleConnect(String clientId, Map<String, String> queryParams) {
        logger.info(String.format("Client connected - ID: %s, Params: %s", clientId, queryParams));
        broadcaster.tryEmitNext(String.format("User %s joined the server", clientId));
        
        if (queryParams.containsKey("userType")) {
            String userType = queryParams.get("userType");
            logger.info(String.format("Client %s connected as %s", clientId, userType));
        }
    }

    private void handleMessage(WebSocketMessage message, String clientId) {
        try {
            String payload = message.getPayloadAsText();
            logger.info(String.format("Received message from %s: %s", clientId, payload));
            broadcaster.tryEmitNext(String.format("User %s: %s", clientId, payload));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error processing message", e);
            handleError(e, clientId);
        }
    }

    private void handleDisconnect(String clientId) {
        logger.info(String.format("Client disconnected: %s", clientId));
        sessions.remove(clientId);
        broadcaster.tryEmitNext(String.format("User %s left the server", clientId));
    }

    private void handleError(Throwable error, String clientId) {
        logger.log(Level.SEVERE, String.format("Error for client %s", clientId), error);
        
        WSSession wsSession = sessions.get(clientId);
        if (wsSession != null && wsSession.getSession().closeStatus() == null) {
            WebSocketSession session = wsSession.getSession();
            String errorMessage = String.format("Error occurred: %s", error.getMessage());
            session.send(Mono.just(session.textMessage(errorMessage)))
                .subscribe();
        }

        sessions.remove(clientId);
    }

    // Public methods to interact with WebSocket sessions

    public void broadcastMessage(String message) {
        broadcaster.tryEmitNext(message);
    }

    public void sendMessageToClient(String clientId, String message) {
        WSSession wsSession = sessions.get(clientId);
        if (wsSession != null && wsSession.getSession().closeStatus() == null) {
            WebSocketSession session = wsSession.getSession();
            session.send(Mono.just(session.textMessage(message)))
                .subscribe();
        }
    }

    public void sendMessageToModuleChannelTarget(String module, String channel, String target, String message) {
        sessions.values().stream()
            .filter(wsSession -> wsSession.getModule().equals(module)
                    && wsSession.getChannel().equals(channel) && wsSession.getTarget().equals(target))
            .forEach(wsSession -> {
                WebSocketSession session = wsSession.getSession();
                session.send(Mono.just(session.textMessage(message)))
                    .subscribe();
            });
    }

    public boolean isClientConnected(String clientId) {
        WSSession wsSession = sessions.get(clientId);
        return wsSession != null && wsSession.getSession().closeStatus() == null;
    }

    public int getConnectedClientsCount() {
        return sessions.size();
    }

    @Data
    @AllArgsConstructor
    @Builder
    public static class WSSession {
        private final String clientId;
        private final WebSocketSession session;
        private final String module;
        private final String channel;
        private final String target;
        private final Map<String, String> params;
    }
}