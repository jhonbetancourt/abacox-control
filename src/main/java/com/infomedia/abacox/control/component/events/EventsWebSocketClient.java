package com.infomedia.abacox.control.component.events;

import com.infomedia.abacox.control.component.wsserver.WebSocketServer;
import com.infomedia.abacox.control.entity.Module;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import reactor.core.publisher.Mono;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Log4j2
public class EventsWebSocketClient {
    private static final int INITIAL_RECONNECT_DELAY = 1;
    private static final int MAX_RECONNECT_DELAY = 60;

    private final WebSocketServer server;
    private final WebSocketClient client;
    private final Map<UUID, ModuleWebSocketSession> moduleSessions;
    private final Map<UUID, Integer> reconnectAttempts;
    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    @Setter
    private CommandHandler commandHandler;

    @Value("${spring.application.prefix}")
    private String source;

    public EventsWebSocketClient(WebSocketServer server, ObjectMapper objectMapper) {
        this.server = server;
        this.client = new ReactorNettyWebSocketClient();
        this.moduleSessions = new ConcurrentHashMap<>();
        this.reconnectAttempts = new ConcurrentHashMap<>();
        this.webClient = WebClient.create();
        this.objectMapper = objectMapper;
    }

    public void connect(Module module) {
        reconnectAttempts.put(module.getId(), 0);
        String websocketUrl = buildWebsocketUrl(module.getUrl());
        ModuleWebSocketSession moduleSession = createModuleSession(module, websocketUrl);
        doConnect(moduleSession);
    }

    private void doConnect(ModuleWebSocketSession moduleSession) {
        UUID sessionId = moduleSession.getId();
        if (isValidSession(moduleSessions.get(sessionId))) {
            log.info("WebSocket session {} is already connected", sessionId);
            return;
        }

        log.info("Initiating WebSocket connection for session {} to: {}", 
                sessionId, moduleSession.getWebsocketUrl());

        getEventTypesInfo(moduleSession.getUrl())
                .flatMap(eventTypesInfo -> setupSessionAndConnect(moduleSession, eventTypesInfo))
                .doOnSubscribe(subscription -> moduleSessions.put(sessionId, moduleSession))
                .doOnError(error -> handleConnectionError(sessionId, error))
                .subscribe();
    }

    private Mono<Void> setupSessionAndConnect(ModuleWebSocketSession moduleSession, EventTypesInfo eventTypesInfo) {
        moduleSession.setProduces(eventTypesInfo.getProduces());
        moduleSession.setConsumes(eventTypesInfo.getConsumes());
        return client.execute(
                URI.create(moduleSession.getWebsocketUrl()),
                createWebSocketHandler(moduleSession)
        );
    }

    private WebSocketHandler createWebSocketHandler(ModuleWebSocketSession moduleSession) {
        return session -> {
            UUID sessionId = moduleSession.getId();
            moduleSession.setSession(session);
            log.info("WebSocket connection established for session: {}", sessionId);
            reconnectAttempts.remove(sessionId);

            return session.receive()
                    .doOnNext(message -> handleTextMessage(moduleSession, message))
                    .doOnComplete(() -> {
                        log.info("WebSocket session {} completed", sessionId);
                        scheduleReconnect(sessionId);
                    })
                    .doOnError(error -> {
                        log.error("Error in WebSocket session {}: {}", sessionId, error.getMessage());
                        scheduleReconnect(sessionId);
                    })
                    .then();
        };
    }

    public Mono<EventTypesInfo> getEventTypesInfo(String baseUrl) {
        return webClient.get()
                .uri(baseUrl + "/api/module/eventTypes")
                .retrieve()
                .onStatus(status -> !status.is2xxSuccessful(),
                        response -> response.bodyToMono(String.class)
                                .flatMap(error -> {
                                    String detail = null;
                                    try {
                                        JsonNode errorNode = objectMapper.readTree(error);
                                        detail = errorNode.get("detail").asText();
                                    } catch (Exception ignored) {
                                    }
                                    return Mono.error(new RuntimeException(
                                            detail != null ? detail : "Error while getting event types info"));
                                }))
                .bodyToMono(EventTypesInfo.class);
    }

    private void scheduleReconnect(UUID sessionId) {
        ModuleWebSocketSession moduleSession = moduleSessions.get(sessionId);
        if (moduleSession == null) return;

        int attempts = reconnectAttempts.getOrDefault(sessionId, 0);
        int delay = calculateReconnectDelay(attempts);
        reconnectAttempts.put(sessionId, attempts + 1);

        log.info("Scheduling reconnection for session {} in {} seconds (attempt {})",
                sessionId, delay, attempts + 1);

        Mono.delay(Duration.ofSeconds(delay))
                .doOnNext(ignored -> {
                    log.info("Attempting to reconnect session {}", sessionId);
                    doConnect(moduleSession);
                })
                .subscribe();
    }

    public void sendMessage(UUID sessionId, WSMessage wsMessage) {
        ModuleWebSocketSession moduleSession = moduleSessions.get(sessionId);
        if (!isValidSession(moduleSession)) {
            log.warn("WebSocket session {} is not open", sessionId);
            if (moduleSession != null) scheduleReconnect(sessionId);
            return;
        }

        try {
            String jsonMessage = objectMapper.writeValueAsString(wsMessage);
            moduleSession.getSession()
                    .send(Mono.just(moduleSession.getSession().textMessage(jsonMessage)))
                    .doOnSuccess(ignored -> log.info("Sent WSMessage to session {}: {}", sessionId, jsonMessage))
                    .doOnError(error -> {
                        log.error("Error sending WSMessage to session {}: {}", sessionId, error.getMessage());
                        scheduleReconnect(sessionId);
                    })
                    .subscribe();
        } catch (Exception e) {
            log.error("Error preparing WSMessage for session {}: {}", sessionId, e.getMessage());
        }
    }

    public void sendEventMessageAll(EventMessage eventMessage) {
        moduleSessions.values().forEach(session -> 
            sendMessage(session.getId(), eventMessage));
    }

    public void sendEventMessageAllExcept(UUID sessionId, EventMessage eventMessage) {
        moduleSessions.values().stream()
                .filter(session -> !session.getId().equals(sessionId))
                .forEach(session -> sendMessage(session.getId(), eventMessage));
    }

    public void sendEventMessageAllExceptAndConsumes(UUID sessionId, EventMessage eventMessage) {
        moduleSessions.values().stream()
                .filter(session -> !session.getId().equals(sessionId)
                        && session.getConsumes().contains(eventMessage.getEventType().name()))
                .forEach(session -> sendMessage(session.getId(), eventMessage));
    }

    private void handleTextMessage(ModuleWebSocketSession moduleSession, WebSocketMessage message) {
        String payload = message.getPayloadAsText();
        CompletableFuture.runAsync(() -> processMessage(moduleSession, payload));
    }

    private void processMessage(ModuleWebSocketSession moduleSession, String payload) {
        try {
            WSMessage wsMessage = objectMapper.readValue(payload, WSMessage.class);
            switch (wsMessage.getMessagetype()) {
                case EVENT -> handleEventMessage(moduleSession, payload);
                case WS_EVENT -> handleWSEventMessage(moduleSession, payload);
                case COMMAND_REQUEST -> handleCommandRequestMessage(moduleSession, payload);
            }
        } catch (Exception e) {
            log.error("Error processing message: " + e.getMessage(), e);
        }
    }

    private void handleEventMessage(ModuleWebSocketSession moduleSession, String payload) {
        try {
            EventMessage eventMessage = objectMapper.readValue(payload, EventMessage.class);
            log.info("Received Event Message for session {}: {}", moduleSession.getId(), eventMessage);
            if (moduleSession.getProduces().contains(eventMessage.getEventType().name())) {
                sendEventMessageAllExceptAndConsumes(moduleSession.getId(), eventMessage);
            }
        } catch (Exception e) {
            log.error("Error handling event message: {}", e.getMessage());
        }
    }

    private void handleWSEventMessage(ModuleWebSocketSession moduleSession, String payload) {
        try {
            WSEventMessage wsEventMessage = objectMapper.readValue(payload, WSEventMessage.class);
            log.info("Received WS Event Message for session {}: {}", moduleSession.getId(), wsEventMessage);
            server.sendMessageToModuleChannelTarget(
                    wsEventMessage.getSource(),
                    wsEventMessage.getChannel(),
                    wsEventMessage.getTarget(),
                    wsEventMessage.getOwner(),
                    objectMapper.writeValueAsString(wsEventMessage)
            );
        } catch (Exception e) {
            log.error("Error handling WS event message: {}", e.getMessage());
        }
    }

    private void handleCommandRequestMessage(ModuleWebSocketSession moduleSession, String payload) {
        try {
            CommandRequestMessage commandRequestMessage = objectMapper.readValue(payload, CommandRequestMessage.class);
            log.info("Received Command Request Message for session {}: {}", 
                    moduleSession.getId(), commandRequestMessage);

            if (commandHandler != null) {
                CommandResult result = commandHandler.handleCommand(
                        commandRequestMessage.getCommand(),
                        commandRequestMessage.getArguments()
                );
                CommandResponseMessage response = result.isSuccess()
                        ? new CommandResponseMessage(commandRequestMessage, source, result.getResult())
                        : new CommandResponseMessage(commandRequestMessage, source,
                                result.getException(), result.getMessage());
                sendMessage(moduleSession.getId(), response);
            }
        } catch (Exception e) {
            log.error("Error handling command request message: {}", e.getMessage());
        }
    }

    public void disconnect(UUID sessionId) {
        ModuleWebSocketSession moduleSession = moduleSessions.get(sessionId);
        if (moduleSession != null && moduleSession.getSession() != null) {
            moduleSessions.remove(sessionId);
            reconnectAttempts.remove(sessionId);
            moduleSession.getSession().close()
                    .doOnSuccess(ignored -> log.info("WebSocket session {} disconnected", sessionId))
                    .doOnError(error -> log.error("Error disconnecting WebSocket session {}: {}", 
                            sessionId, error.getMessage()))
                    .subscribe();
        }
    }

    public void disconnect(Module module) {
        disconnect(module.getId());
    }

    @EventListener(ContextClosedEvent.class)
    public void onContextClosedEvent() {
        disconnectAll();
    }

    public void disconnectAll() {
        new ArrayList<>(moduleSessions.keySet()).forEach(this::disconnect);
    }

    public boolean isConnected(UUID sessionId) {
        return isValidSession(moduleSessions.get(sessionId));
    }

    public boolean isConnected(Module module) {
        return isConnected(module.getId());
    }

    private String buildWebsocketUrl(String moduleUrl) {
        try {
            URL url = new URL(moduleUrl);
            String host = url.getHost();
            int port = url.getPort() == -1 ? url.getDefaultPort() : url.getPort();
            return String.format("ws://%s:%d/websocket/module", host, port);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    private ModuleWebSocketSession createModuleSession(Module module, String websocketUrl) {
        return ModuleWebSocketSession.builder()
                .id(module.getId())
                .name(module.getName())
                .websocketUrl(websocketUrl)
                .url(module.getUrl())
                .build();
    }

    private boolean isValidSession(ModuleWebSocketSession session) {
        return session != null && 
               session.getSession() != null && 
               session.getSession().isOpen();
    }

    private void handleConnectionError(UUID sessionId, Throwable error) {
        log.error("Error connecting WebSocket session {}: {}", sessionId, error.getMessage());
        scheduleReconnect(sessionId);
    }

    private int calculateReconnectDelay(int attempts) {
        return Math.min(INITIAL_RECONNECT_DELAY * (1 << Math.min(attempts, 30)), MAX_RECONNECT_DELAY);
    }

    public interface CommandHandler {
        CommandResult handleCommand(String command, Map<String, Object> args);
    }
}