package com.infomedia.abacox.control.component.events;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class CommandResponseMessage extends WSMessage {
    private boolean success;
    private String exception;
    private String errorMessage;
    private Object result;

    public CommandResponseMessage(UUID id, String source, String exception, String errorMessage) {
        super(id, source, MessageType.COMMAND_RESPONSE);
        this.success = false;
        this.exception = exception;
        this.errorMessage = errorMessage;
    }

    public CommandResponseMessage(CommandRequestMessage commandRequestMessage, String source, String exception, String errorMessage) {
        super(source, MessageType.COMMAND_RESPONSE);
        this.success = false;
        this.exception = exception;
        this.errorMessage = errorMessage;
        setId(commandRequestMessage.getId());
    }

    public CommandResponseMessage(UUID id, String source, Object result) {
        super(id, source, MessageType.COMMAND_RESPONSE);
        this.success = true;
        this.result = result;
    }

    public CommandResponseMessage(CommandRequestMessage commandRequestMessage, String source, Object result) {
        super(source, MessageType.COMMAND_RESPONSE);
        this.success = true;
        this.result = result;
        setId(commandRequestMessage.getId());
    }
}
