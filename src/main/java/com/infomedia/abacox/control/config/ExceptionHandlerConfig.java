package com.infomedia.abacox.control.config;

import com.infomedia.abacox.control.exception.ResourceAlreadyExistsException;
import com.infomedia.abacox.control.exception.ResourceDeletionException;
import com.infomedia.abacox.control.exception.ResourceDisabledException;
import com.infomedia.abacox.control.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.postgresql.util.PSQLException;
import org.postgresql.util.ServerErrorMessage;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import org.springframework.web.reactive.resource.NoResourceFoundException;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

import java.net.ConnectException;
import java.net.URI;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Log4j2
@Component
@Order(-2)
public class ExceptionHandlerConfig extends AbstractErrorWebExceptionHandler {

    private final Map<Class<? extends Throwable>, Function<ErrorInfo, Mono<ServerResponse>>> exceptionHandlers;

    public ExceptionHandlerConfig(ErrorAttributes errorAttributes, WebProperties webProperties,
                                  ApplicationContext applicationContext, ServerCodecConfigurer serverCodecConfigurer) {
        super(errorAttributes, webProperties.getResources(), applicationContext);
        super.setMessageWriters(serverCodecConfigurer.getWriters());
        super.setMessageReaders(serverCodecConfigurer.getReaders());

        this.exceptionHandlers = new HashMap<>();
        initializeExceptionHandlers();
    }

    private void initializeExceptionHandlers() {
        exceptionHandlers.put(RestClientException.class,
                errorInfo -> createErrorResponse(errorInfo.getPath(), HttpStatus.INTERNAL_SERVER_ERROR, "Remote Service Error"
                        , "remote-service-error", errorInfo.getError().getMessage()));
        exceptionHandlers.put(ConnectException.class,
                errorInfo -> createErrorResponse(errorInfo.getPath(), HttpStatus.INTERNAL_SERVER_ERROR, "Remote Service Error"
                        , "remote-service-error", errorInfo.getError().getMessage()));
        exceptionHandlers.put(NoResourceFoundException.class,
                errorInfo -> createErrorResponse(errorInfo.getPath(), HttpStatus.NOT_FOUND, "Resource Not Found"
                        , "resource-not-found", errorInfo.getError().getMessage()));
        exceptionHandlers.put(IllegalArgumentException.class,
                errorInfo -> createErrorResponse(errorInfo.getPath(), HttpStatus.BAD_REQUEST, "Invalid Argument"
                        , "invalid-argument", errorInfo.getError().getMessage()));
        exceptionHandlers.put(ResourceNotFoundException.class,
                errorInfo -> createErrorResponse(errorInfo.getPath(), HttpStatus.NOT_FOUND, "Resource Not Found"
                        , "resource-not-found", errorInfo.getError().getMessage()));
        exceptionHandlers.put(ResourceAlreadyExistsException.class,
                errorInfo -> createErrorResponse(errorInfo.getPath(), HttpStatus.CONFLICT, "Resource Already Exists"
                        , "resource-already-exists", errorInfo.getError().getMessage()));
        exceptionHandlers.put(ResourceDeletionException.class,
                errorInfo -> createErrorResponse(errorInfo.getPath(), HttpStatus.CONFLICT, "Resource Deletion Error"
                        , "resource-deletion-error", errorInfo.getError().getMessage()));
        exceptionHandlers.put(ResourceDisabledException.class,
                errorInfo -> createErrorResponse(errorInfo.getPath(), HttpStatus.CONFLICT, "Resource Disabled"
                        , "resource-disabled", errorInfo.getError().getMessage()));
        exceptionHandlers.put(ServerWebInputException.class,
                errorInfo -> createErrorResponse(errorInfo.getPath(), HttpStatus.BAD_REQUEST, "Invalid Input"
                        , "invalid-input", ((ServerWebInputException) errorInfo.getError()).getReason()));
        exceptionHandlers.put(ResourceAccessException.class,
                errorInfo -> createErrorResponse(errorInfo.getPath(), HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error"
                        , "internal-error", errorInfo.getError().getMessage()));
        exceptionHandlers.put(DataIntegrityViolationException.class,
                errorInfo -> {
                    if(NestedExceptionUtils.getRootCause(errorInfo.getError()) instanceof PSQLException psqlException){
                        ServerErrorMessage serverErrorMessage = psqlException.getServerErrorMessage();
                        if(serverErrorMessage!=null){
                            return createErrorResponse(errorInfo.getPath(), HttpStatus.CONFLICT, "Data Integrity Violation"
                                    , "data-integrity-violation", serverErrorMessage.getMessage()+": "+serverErrorMessage.getDetail());
                        }
                    }
                    return createErrorResponse(errorInfo.getPath(), HttpStatus.CONFLICT, "Data Integrity Violation"
                            , "data-integrity-violation", errorInfo.getError().getMessage());
                });
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    private Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
        Throwable error = getError(request);
        Throwable rootError = NestedExceptionUtils.getRootCause(error);
        log.error(error);

        // First, try to find a handler for the original error
        Function<ErrorInfo, Mono<ServerResponse>> handler = exceptionHandlers.get(error.getClass());

        // If no handler found for the original error, try to find a handler for the root error
        if (handler == null && rootError != null) {
            handler = exceptionHandlers.get(rootError.getClass());
            // If a handler is found for the root error, use the root error instead
            if (handler != null) {
                error = rootError;
            }
        }

        // If still no handler found, use the default error handler
        if (handler == null) {
            handler = this::handleDefaultError;
        }

        return handler.apply(new ErrorInfo(request, error));
    }

    private Mono<ServerResponse> handleDefaultError(ErrorInfo errorInfo) {
        log.error("Unhandled exception", errorInfo.getError());
        return createErrorResponse(errorInfo.getPath(), HttpStatus.INTERNAL_SERVER_ERROR
                , "Internal Server Error", "internal-error", "An unexpected error occurred");
    }

    private Mono<ServerResponse> createErrorResponse(String path, HttpStatus status, String title, String type, String detail) {
        Map<String, Object> problemDetail = new HashMap<>();
        problemDetail.put("status", status.value());
        problemDetail.put("title", title);
        problemDetail.put("type", URI.create(type));
        problemDetail.put("detail", detail);
        problemDetail.put("instance", URI.create(path));

        Map<String, Object> properties = new HashMap<>();
        properties.put("timestamp", LocalDateTime.now());

        problemDetail.put("properties", properties);

        return ServerResponse.status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(problemDetail));
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class ErrorInfo {
        private ServerRequest request;
        private Throwable error;

        public String getPath() {
            return request.path();
        }
    }
}