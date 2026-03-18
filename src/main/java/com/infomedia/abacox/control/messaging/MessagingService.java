package com.infomedia.abacox.control.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Central service for internal inter-module communication over RabbitMQ.
 *
 * <h3>Publishing events</h3>
 * <pre>
 *   messagingService.publishEvent("schema_abc", "USER_CREATED", payload);
 * </pre>
 * Routing key produced: {@code schema_abc.users.USER_CREATED}
 *
 * <h3>Sending queries (RPC)</h3>
 * <pre>
 *   InternalMessage response = messagingService.sendQuery("orchestrator", "TENANT_MODULES_QUERY", payload, 5000);
 * </pre>
 *
 * <h3>Replying to a query</h3>
 * <pre>
 *   messagingService.reply(request, responsePayload);
 * </pre>
 *
 * <h3>Subscribing to events</h3>
 * Declare a queue in your module's config and use {@code @RabbitListener}:
 * <pre>
 *   {@literal @}RabbitListener(queues = "abacox.mymodule.events")
 *   public void onEvent(InternalMessage message) { ... }
 * </pre>
 * Bind the queue to {@code abacox.events} with a routing key pattern like {@code #.#.USER_CREATED}
 * or {@code schema_abc.#} to filter by tenant.
 */
@Service
@Log4j2
public class MessagingService {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;
    private final String moduleName;

    public MessagingService(RabbitTemplate rabbitTemplate,
                            ObjectMapper objectMapper,
                            @Value("${spring.application.name}") String moduleName) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
        this.moduleName = moduleName;
    }

    /**
     * Publish a fire-and-forget event to the events topic exchange.
     *
     * @param tenant schema of the tenant this event concerns, or null for platform-wide
     * @param type         event type identifier (e.g. "USER_CREATED")
     * @param payload      arbitrary payload object
     */
    public void publishEvent(String tenant, String type, Object payload) {
        String schema = tenant != null ? tenant : "all";
        String routingKey = schema + "." + moduleName + "." + type;

        InternalMessage message = InternalMessage.builder()
                .tenant(tenant)
                .sourceModule(moduleName)
                .type(type)
                .payload(payload)
                .build();

        log.debug("Publishing event [{}] with routing key [{}]", type, routingKey);
        rabbitTemplate.convertAndSend(RabbitMQConfig.EVENTS_EXCHANGE, routingKey, message);
    }

    /**
     * Send a synchronous query to a target module and wait for a response (RPC pattern).
     *
     * @param targetModule name of the module to query (e.g. "orchestrator", "users")
     * @param type         query type identifier (e.g. "TENANT_MODULES_QUERY")
     * @param payload      query payload
     * @param timeoutMs    max milliseconds to wait for a response
     * @return the response message, or null if timed out
     */
    public InternalMessage sendQuery(String targetModule, String type, Object payload, long timeoutMs) {
        InternalMessage request = InternalMessage.builder()
                .sourceModule(moduleName)
                .type(type)
                .payload(payload)
                .build();

        log.debug("Sending query [{}] to [{}]", type, targetModule);

        rabbitTemplate.setReplyTimeout(timeoutMs);
        Object response = rabbitTemplate.convertSendAndReceive(
                RabbitMQConfig.QUERIES_EXCHANGE,
                targetModule,
                request
        );

        if (response == null) {
            log.warn("Query [{}] to [{}] timed out after {}ms", type, targetModule, timeoutMs);
            return null;
        }

        if (response instanceof InternalMessage msg) {
            return msg;
        }

        // Fallback: re-map via ObjectMapper in case of type erasure
        try {
            return objectMapper.convertValue(response, InternalMessage.class);
        } catch (Exception e) {
            log.error("Failed to deserialize query response for [{}]: {}", type, e.getMessage());
            return null;
        }
    }

    /**
     * Reply to an RPC query. Use this inside a {@code @RabbitListener} that handles queries.
     *
     * @param request         the original request message
     * @param responsePayload the response payload
     */
    public void reply(InternalMessage request, Object responsePayload) {
        if (request.getReplyTo() == null) {
            log.warn("Cannot reply to message [{}] — no replyTo queue set", request.getType());
            return;
        }

        InternalMessage response = InternalMessage.builder()
                .sourceModule(moduleName)
                .type(request.getType() + "_RESPONSE")
                .correlationId(request.getCorrelationId())
                .payload(responsePayload)
                .build();

        log.debug("Replying to query [{}] on queue [{}]", request.getType(), request.getReplyTo());
        rabbitTemplate.convertAndSend(request.getReplyTo(), response, msg -> {
            msg.getMessageProperties().setCorrelationId(request.getCorrelationId().toString());
            return msg;
        });
    }

    /**
     * Convenience: publish a platform-wide (not tenant-scoped) event.
     */
    public void publishPlatformEvent(String type, Object payload) {
        publishEvent(null, type, payload);
    }
}
