package com.infomedia.abacox.control.component.wsserver;

import lombok.Getter;
import org.springframework.web.reactive.socket.WebSocketSession;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class WebSocketConnection {
    private final WebSocketSession session;
    private final Map<String, String> pathParams;
    private final Map<String, String> queryParams;

    private WebSocketConnection(WebSocketSession session) {
        this.session = session;
        URI uri = session.getHandshakeInfo().getUri();
        this.pathParams = extractPathParameters(uri.getPath());
        this.queryParams = parseQueryString(uri.getQuery());
    }

    public static WebSocketConnection from(WebSocketSession session) {
        return new WebSocketConnection(session);
    }

    private Map<String, String> parseQueryString(String query) {
        Map<String, String> params = new ConcurrentHashMap<>();
        if (query != null && !query.isEmpty()) {
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=");
                if (keyValue.length == 2) {
                    params.put(keyValue[0], keyValue[1]);
                }
            }
        }
        return params;
    }

    private Map<String, String> extractPathParameters(String path) {
        Map<String, String> params = new ConcurrentHashMap<>();
        String[] parts = path.split("/");
        if (parts.length >= 5) {
            params.put("module", parts[3]);
            params.put("channel", parts[4]);
            params.put("target", parts[5]);
        }
        return params;
    }
}