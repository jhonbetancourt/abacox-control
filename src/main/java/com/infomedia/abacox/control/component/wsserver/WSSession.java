package com.infomedia.abacox.control.component.wsserver;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.reactive.socket.WebSocketSession;

import java.util.Map;

@Data
@AllArgsConstructor
@Builder
public class WSSession {
    private final String owner;
    private final String clientId;
    private final WebSocketSession session;
    private final String module;
    private final String channel;
    private final String target;
    private final Map<String, String> params;
}