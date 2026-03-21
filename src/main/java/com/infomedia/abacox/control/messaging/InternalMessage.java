package com.infomedia.abacox.control.messaging;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Envelope for all internal inter-module and module-to-orchestrator messages.
 * <p>
 * Routing key convention:
 * - Events:  {tenant}.{sourceModule}.{type}   (topic exchange)
 * - Queries: {targetModule}                          (direct exchange, replyTo set)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InternalMessage {

    /** Schema of the tenant this message concerns. Null means platform-wide. */
    private String tenant;

    /** Name of the module that sent this message (e.g. "users", "core", "orchestrator"). */
    private String sourceModule;

    /** Event or query type identifier (e.g. "USER_CREATED", "TENANT_MODULES_QUERY"). */
    private String type;

    /** Arbitrary payload — serialized as JSON. */
    private Object payload;

    /** True if this is a successful response; false if it represents an error. */
    @Builder.Default
    private boolean success = true;

    /** Username of the user that triggered this message (from SecurityContext). */
    private String actor;

    /** Correlation ID for RPC-style request/response matching. */
    @Builder.Default
    private UUID correlationId = UUID.randomUUID();

    /** Reply-to queue name. Set automatically by MessagingService when sending queries. */
    private String replyTo;

    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();
}
