package com.infomedia.abacox.control.component.wsserver;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SessionManager {
    private final Map<String, WSSession> sessions = new ConcurrentHashMap<>();

    public void addSession(WSSession session) {
        sessions.put(session.getClientId(), session);
    }

    public void removeSession(String clientId) {
        sessions.remove(clientId);
    }

    public void handleErrorForClient(String clientId, Throwable error) {
        WSSession wsSession = sessions.get(clientId);
        if (wsSession != null && wsSession.getSession().closeStatus() == null) {
            WebSocketSession session = wsSession.getSession();
            String errorMessage = String.format("Error occurred: %s", error.getMessage());
            session.send(Mono.just(session.textMessage(errorMessage)))
                    .subscribe();
        }
        removeSession(clientId);
    }

    public void sendMessageToClient(String clientId, String message) {
        WSSession wsSession = sessions.get(clientId);
        if (wsSession != null && wsSession.getSession().closeStatus() == null) {
            WebSocketSession session = wsSession.getSession();
            session.send(Mono.just(session.textMessage(message)))
                    .subscribe();
        }
    }

    public void sendMessageToModuleChannelTarget(String module, String channel, String target, String owner, String message) {
        sessions.values().stream()
                .filter(wsSession -> wsSession.getModule().equals(module)
                        && wsSession.getChannel().equals(channel)
                        && wsSession.getTarget().equals(target)
                        && wsSession.getOwner().equals(owner))
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
}