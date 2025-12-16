package com.infomedia.abacox.control.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infomedia.abacox.control.component.events.CommandResult;
import com.infomedia.abacox.control.component.events.EventsWebSocketClient;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import okhttp3.*; // Imported OkHttp classes
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class CommandHandlerService {

    private final EventsWebSocketClient eventsWebSocketClient;
    private final ModuleService moduleService;
    private final ObjectMapper objectMapper;
    private OkHttpClient httpClient;
    @Value("${abacox.orchestrator-url}")
    private String orchestratorUrl;
    private static final String AUTH_REFRESH_PATH = "/api/auth/refresh";
    // Define JSON media type for OkHttp
    private static final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");

    @PostConstruct
    public void init() {
        eventsWebSocketClient.setCommandHandler(commandHandler);
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        httpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .callTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
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

    private Object getCommandResult(String command, Map<String, Object> args) throws IOException {
        switch (command) {
            case "getModuleInfoByPrefix":
                return getModuleInfoByPrefix(args);
            case "suRefreshToken":
                return suRefreshToken(args);
            default:
                throw new IllegalArgumentException("Unknown command: " + command);
        }
    }

    private Object getModuleInfoByPrefix(Map<String, Object> args) {
        return moduleService.getInfoByPrefix((String) args.get("prefix"));
    }

    private Object suRefreshToken(Map<String, Object> args) throws IOException {
        // Construct URL
        String url = orchestratorUrl + AUTH_REFRESH_PATH;

        // Convert args to JSON string for the request body
        String jsonBody = objectMapper.writeValueAsString(args);
        RequestBody body = RequestBody.create(jsonBody, JSON_MEDIA_TYPE);

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (response.body() != null) {
                String responseString = response.body().string();
                
                if (!response.isSuccessful()) {
                    throw new IOException("Refresh failed with code " + response.code() + ": " + responseString);
                }
                
                // Return the raw object so the main handler converts it to a tree
                return objectMapper.readValue(responseString, Object.class);
            } else {
                throw new IOException("Empty response from orchestrator");
            }
        }
    }
}