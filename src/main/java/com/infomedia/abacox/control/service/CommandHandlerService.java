package com.infomedia.abacox.control.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infomedia.abacox.control.component.events.CommandResult;
import com.infomedia.abacox.control.component.events.EventsWebSocketClient;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CommandHandlerService {

    private final EventsWebSocketClient eventsWebSocketClient;
    private final ModuleService moduleService;
    private final ObjectMapper objectMapper;

    @PostConstruct
    public void init() {
        eventsWebSocketClient.setCommandHandler(commandHandler);
    }

    private EventsWebSocketClient.CommandHandler commandHandler = new EventsWebSocketClient.CommandHandler() {
        @Override
        public CommandResult handleCommand(String command, Map<String, Object> args) {
            try {
                Object commandResult = getCommandResult(command, args);
                return CommandResult.builder()
                        .success(true)
                        .result(objectMapper.valueToTree(commandResult))
                        .build();
            }catch (Exception e) {
                return CommandResult.builder()
                        .success(false)
                        .exception(e.getClass().getName())
                        .message(e.getMessage())
                        .build();
            }
        }
    };

    private Object getCommandResult(String command, Map<String, Object> args) {
        switch (command) {
            case "getModuleInfoByPrefix":
                return getModuleInfoByPrefix(args);
            default:
                throw new IllegalArgumentException("Unknown command: " + command);
        }
    }

    private Object getModuleInfoByPrefix(Map<String, Object> args) {
        return moduleService.getInfoByPrefix((String) args.get("prefix"));
    }
}
