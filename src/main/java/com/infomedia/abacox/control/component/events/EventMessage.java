package com.infomedia.abacox.control.component.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class EventMessage {
    private UUID id;
    private String source;
    private String type;
    private Instant timestamp;
    private String content;

    public EventMessage(String source, String type, String content) {
        this.id = UUID.randomUUID();
        this.type = type;
        this.timestamp = Instant.now();
        this.content = content;
        this.source = source;
    }
}
