package com.infomedia.abacox.control.component.events;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.infomedia.abacox.control.config.JsonConfig;
import io.swagger.v3.core.util.Json;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class EventMessage extends WSMessage{
    private EventType eventType;
    private JsonNode content;

    public EventMessage(String source, EventType eventType, String content) {
        super(UUID.randomUUID(), source, LocalDateTime.now(), MessageType.EVENT);
        this.eventType = eventType;
        try {
            this.content = JsonConfig.getObjectMapper().readTree(content);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
