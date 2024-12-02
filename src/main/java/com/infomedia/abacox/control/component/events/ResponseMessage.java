package com.infomedia.abacox.control.component.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder(toBuilder = true)
public class ResponseMessage extends WSMessage {
    private UUID id;
    private String source;
    private Instant timestamp;
    private boolean success;
    private String exception;
    private String errorMessage;
    private Object result;
}
