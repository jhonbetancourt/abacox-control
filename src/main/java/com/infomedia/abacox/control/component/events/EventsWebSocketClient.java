package com.infomedia.abacox.control.component.events;

import com.infomedia.abacox.control.component.functiontools.FunctionResult;
import com.infomedia.abacox.control.entity.Module;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.infomedia.abacox.control.service.LocalFunctionService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Log4j2
public class EventsWebSocketClient {

    private final WebSocketClient client;
    private final Map<UUID, ModuleWebSocketSession> moduleSessions;
    private final Map<UUID, Integer> reconnectAttempts;
    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    @Autowired
    private LocalFunctionService localFunctionService;

    private static final int INITIAL_RECONNECT_DELAY = 1;
    private static final int MAX_RECONNECT_DELAY = 60;

    public EventsWebSocketClient(ObjectMapper objectMapper) {
        this.client = new ReactorNettyWebSocketClient();
        this.moduleSessions = new ConcurrentHashMap<>();
        this.reconnectAttempts = new ConcurrentHashMap<>();
        this.webClient = WebClient.create();
        this.objectMapper = objectMapper;
    }

    public void connect(Module module) {
        reconnectAttempts.put(module.getId(), 0);
        URL url;
        try {
            url = new URL(module.getUrl());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        String host = url.getHost();
        int port = url.getPort() == -1 ? url.getDefaultPort() : url.getPort();
        String websocketUrl = "ws://" + host + ":" + port + "/websocket/module";
        ModuleWebSocketSession moduleWebSocketSession = ModuleWebSocketSession.builder()
                .id(module.getId())
                .name(module.getName())
                .websocketUrl(websocketUrl)
                .url(module.getUrl())
                .build();
        doConnect(moduleWebSocketSession);
    }

    private void doConnect(ModuleWebSocketSession moduleWebSocketSession) {
        UUID sessionId = moduleWebSocketSession.getId();
        if (moduleSessions.containsKey(sessionId) && moduleSessions.get(sessionId).getSession() != null
                && moduleSessions.get(sessionId).getSession().isOpen()) {
            log.info("WebSocket session {} is already connected", sessionId);
            return;
        }

        String websocketUrl = moduleWebSocketSession.getWebsocketUrl();
        log.info("Initiating WebSocket connection for session {} to: {}", sessionId, websocketUrl);

        getEventTypesInfo(moduleWebSocketSession.getUrl())
                .flatMap(eventTypesInfo -> {
                    moduleWebSocketSession.setProduces(eventTypesInfo.getProduces());
                    moduleWebSocketSession.setConsumes(eventTypesInfo.getConsumes());
                    return client.execute(URI.create(websocketUrl), createWebSocketHandler(moduleWebSocketSession));
                })
                .doOnSubscribe(subscription -> {
                    moduleSessions.put(sessionId, moduleWebSocketSession);
                })
                .doOnError(error -> {
                    log.error("Error connecting WebSocket session {}: {}", sessionId, error.getMessage());
                    scheduleReconnect(sessionId);
                })
                .subscribe();
    }

    private WebSocketHandler createWebSocketHandler(ModuleWebSocketSession moduleWebSocketSession) {
        return session -> {
            UUID sessionId = moduleWebSocketSession.getId();
            moduleWebSocketSession.setSession(session);
            log.info("WebSocket connection established for session: {}", sessionId);

            // Reset reconnect attempts on successful connection
            reconnectAttempts.remove(sessionId);

            return session.receive()
                    .doOnNext(message -> handleTextMessage(moduleWebSocketSession, message))
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
                .onStatus(status -> !status.is2xxSuccessful(), response -> response.bodyToMono(String.class)
                        .flatMap(error -> {
                            String detail = null;
                            try {
                                JsonNode errorNode = objectMapper.readTree(error);
                                detail = errorNode.get("detail").asText();
                            } catch (Exception ignored) {
                            }
                            return Mono.error(new RuntimeException(detail != null ? detail : "Error while getting event types info"));
                        }))
                .bodyToMono(EventTypesInfo.class);
    }

    private void scheduleReconnect(UUID sessionId) {
        ModuleWebSocketSession moduleWebSocketSession = moduleSessions.get(sessionId);
        if (moduleWebSocketSession == null) {
            return;
        }
        int attempts = reconnectAttempts.getOrDefault(sessionId, 0);

        int delay = calculateReconnectDelay(attempts);
        reconnectAttempts.put(sessionId, attempts + 1);

        log.info("Scheduling reconnection for session {} in {} seconds (attempt {})",
                sessionId, delay, attempts + 1);

        Mono.delay(Duration.ofSeconds(delay))
                .doOnNext(ignored -> {
                    log.info("Attempting to reconnect session {}", sessionId);
                    doConnect(moduleWebSocketSession);
                })
                .subscribe();
    }

    private int calculateReconnectDelay(int attempts) {
        return Math.min(INITIAL_RECONNECT_DELAY * (1 << Math.min(attempts, 30)), MAX_RECONNECT_DELAY);
    }

    public void sendMessage(UUID sessionId, WSMessage wsMessage) {
        ModuleWebSocketSession moduleSession = moduleSessions.get(sessionId);
        if (moduleSession != null && moduleSession.getSession() != null && moduleSession.getSession().isOpen()) {
            try {
                String jsonMessage = objectMapper.writeValueAsString(wsMessage);
                moduleSession.getSession().send(Mono.just(moduleSession.getSession().textMessage(jsonMessage)))
                        .doOnSuccess(ignored -> log.info("Sent WSMessage to session {}: {}", sessionId, jsonMessage))
                        .doOnError(error -> {
                            log.error("Error sending WSMessage to session {}: {}", sessionId, error.getMessage());
                            scheduleReconnect(sessionId);
                        })
                        .subscribe();
            } catch (Exception e) {
                log.error("Error preparing WSMessage for session {}: {}", sessionId, e.getMessage());
            }
        } else {
            log.warn("WebSocket session {} is not open", sessionId);
            if(moduleSession!=null) scheduleReconnect(sessionId);
        }
    }

    public void sendEventMessageAll(EventMessage eventMessage) {
        moduleSessions.values().forEach(moduleSession -> sendMessage(moduleSession.getId(), eventMessage));
    }

    public void sendEventMessageAllExcept(UUID sessionId, EventMessage eventMessage) {
        moduleSessions.values().stream()
                .filter(moduleSession -> !moduleSession.getId().equals(sessionId))
                .forEach(moduleSession -> sendMessage(moduleSession.getId(), eventMessage));
    }

    public void sendEventMessageAllExceptAndConsumes(UUID sessionId, EventMessage eventMessage) {
        moduleSessions.values().stream()
                .filter(moduleSession -> !moduleSession.getId().equals(sessionId)
                        && moduleSession.getConsumes().contains(eventMessage.getEventType().name()))
                .forEach(moduleSession -> sendMessage(moduleSession.getId(), eventMessage));
    }

    public void disconnect(UUID sessionId) {
        ModuleWebSocketSession moduleSession = moduleSessions.get(sessionId);
        if (moduleSession != null && moduleSession.getSession() != null) {
            moduleSessions.remove(sessionId);
            reconnectAttempts.remove(sessionId);
            moduleSession.getSession().close()
                    .doOnSuccess(ignored -> {
                        log.info("WebSocket session {} disconnected", sessionId);
                    })
                    .doOnError(error -> log.error("Error disconnecting WebSocket session {}: {}", sessionId, error.getMessage()))
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
        moduleSessions.keySet().forEach(this::disconnect);
    }

    private void handleTextMessage(ModuleWebSocketSession moduleSession, WebSocketMessage message) {
        String payload = message.getPayloadAsText();
        new Thread(() -> processMessage(moduleSession, payload)).start();
    }

    private void processMessage(ModuleWebSocketSession moduleSession, String payload) {
        log.info("Received message for session {}: {}", moduleSession.getId(), payload);
        WSMessage wsMessage = null;
        try {
            wsMessage = objectMapper.readValue(payload, WSMessage.class);
        } catch (Exception e) {
            log.error("Error processing message: " + e.getMessage(), e);
        }

        if(wsMessage!=null){
            if(wsMessage.getMessagetype().equals(MessageType.EVENT)) {
                try {
                    EventMessage eventMessage = objectMapper.readValue(payload, EventMessage.class);
                    log.info("Received EventMessage for session {}: {}", moduleSession.getId(), eventMessage);
                    if (moduleSession.getProduces().contains(eventMessage.getEventType().name())) {
                        sendEventMessageAllExceptAndConsumes(moduleSession.getId(), eventMessage);
                    }
                } catch (Exception e) {
                    log.error("Error handling received message: {}", e.getMessage());
                }
            }
            if (wsMessage.getMessagetype().equals(MessageType.REQUEST)) {
                try {
                    RequestMessage requestMessage = objectMapper.readValue(payload, RequestMessage.class);
                    log.info("Received RequestMessage for session {}: {}", moduleSession.getId(), requestMessage);

                    FunctionResult functionResult = localFunctionService.callFunction(requestMessage.getService(), requestMessage.getFunction(), requestMessage.getArguments());

                    ResponseMessage responseMessage = ResponseMessage.builder()
                            .id(requestMessage.getId())
                            .timestamp(requestMessage.getTimestamp())
                            .source("control")
                            .messagetype(MessageType.RESPONSE)
                            .success(functionResult.isSuccess())
                            .result(functionResult.getResult())
                            .exception(functionResult.getException())
                            .errorMessage(functionResult.getMessage())
                            .build();
                    sendMessage(moduleSession.getId(), responseMessage);
                } catch (Exception e) {
                    log.error("Error handling received message: {}", e.getMessage());
                }
            }
        }
    }

    public boolean isConnected(UUID sessionId) {
        ModuleWebSocketSession moduleSession = moduleSessions.get(sessionId);
        return moduleSession != null && moduleSession.getSession() != null && moduleSession.getSession().isOpen();
    }

    public boolean isConnected(Module module) {
        return isConnected(module.getId());
    }
}