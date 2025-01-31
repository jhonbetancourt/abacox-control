package com.infomedia.abacox.control.component.wsserver;

import com.infomedia.abacox.control.service.AuthService;
import lombok.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Configuration
@RequiredArgsConstructor
public class WebSocketServer implements WebSocketHandler {
    private static final Logger logger = Logger.getLogger(WebSocketServer.class.getName());
    private static final String WS_PATH = "/control/websocket/{module}/{channel}/{target}";
    
    private final SessionManager sessionManager;
    private final MessageBroadcaster broadcaster;
    private final AuthService authService;

    @Bean
    public HandlerMapping handlerMapping() {
        Map<String, WebSocketHandler> map = Collections.singletonMap(WS_PATH, this);
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
        String clientId = session.getId();
        WebSocketConnection connection = WebSocketConnection.from(session);
        
        return authService.getUsername()
                .flatMap(username -> {
                    WSSession wsSession = createSession(clientId, connection, username);
                    sessionManager.addSession(wsSession);
                    logNewConnection(wsSession);

                    return handleWebSocketCommunication(wsSession);
                });
    }

    private WSSession createSession(String clientId, WebSocketConnection connection, String username) {
        return WSSession.builder()
                .clientId(clientId)
                .session(connection.getSession())
                .module(connection.getPathParams().get("module"))
                .channel(connection.getPathParams().get("channel"))
                .target(connection.getPathParams().get("target"))
                .owner(username)
                .params(connection.getQueryParams())
                .build();
    }

    private Mono<Void> handleWebSocketCommunication(WSSession wsSession) {
        WebSocketSession session = wsSession.getSession();
        String clientId = wsSession.getClientId();

        return session.receive()
                .doOnSubscribe(sub -> broadcaster.announceConnection(clientId))
                .doOnNext(message -> handleIncomingMessage(message, clientId))
                .doOnError(error -> handleError(error, clientId))
                .doOnComplete(() -> handleDisconnection(clientId))
                .then()
                .and(session.send(broadcaster.getMessageFlux()
                        .map(session::textMessage)
                        .doOnError(error -> handleError(error, clientId))));
    }

    private void handleIncomingMessage(WebSocketMessage message, String clientId) {
        try {
            String payload = message.getPayloadAsText();
            logger.info(String.format("Received message from %s: %s", clientId, payload));
            broadcaster.broadcast(String.format("User %s: %s", clientId, payload));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error processing message", e);
            handleError(e, clientId);
        }
    }

    private void handleError(Throwable error, String clientId) {
        logger.log(Level.SEVERE, String.format("Error for client %s", clientId), error);
        sessionManager.handleErrorForClient(clientId, error);
    }

    private void handleDisconnection(String clientId) {
        logger.info(String.format("Client disconnected: %s", clientId));
        sessionManager.removeSession(clientId);
        broadcaster.announceDisconnection(clientId);
    }

    // Public API
    public void broadcastMessage(String message) {
        broadcaster.broadcast(message);
    }

    public void sendMessageToClient(String clientId, String message) {
        sessionManager.sendMessageToClient(clientId, message);
    }

    public void sendMessageToModuleChannelTarget(String module, String channel, String target, String owner, String message) {
        sessionManager.sendMessageToModuleChannelTarget(module, channel, target, owner, message);
    }

    public boolean isClientConnected(String clientId) {
        return sessionManager.isClientConnected(clientId);
    }

    public int getConnectedClientsCount() {
        return sessionManager.getConnectedClientsCount();
    }

    private void logNewConnection(WSSession session) {
        logger.info(String.format("New WebSocket session - Client ID: %s, Module: %s, Channel: %s, Target: %s, Owner: %s, Params: %s",
                session.getClientId(), session.getModule(), session.getChannel(), 
                session.getTarget(), session.getOwner(), session.getParams()));
    }
}