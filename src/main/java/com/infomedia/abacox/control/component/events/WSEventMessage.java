package com.infomedia.abacox.control.component.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class WSEventMessage extends WSMessage{
    private EventType eventType;
    private String channel;
    private String target;
    private String content;

    public WSEventMessage(String source, EventType eventType, String channel, String target, String content) {
        super(UUID.randomUUID(), source, LocalDateTime.now(), MessageType.WS_EVENT);
        this.eventType = eventType;
        this.content = content;
        this.channel = channel;
        this.target = target;
    }
}
