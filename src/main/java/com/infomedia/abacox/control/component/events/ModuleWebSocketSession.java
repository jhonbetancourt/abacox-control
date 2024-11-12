package com.infomedia.abacox.control.component.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.reactive.socket.WebSocketSession;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ModuleWebSocketSession {
    private UUID id;
    private String name;
    private String url;
    private String websocketUrl;
    private WebSocketSession session;
    private List<String> produces;
    private List<String> consumes;
}
