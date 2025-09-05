package com.infomedia.abacox.control.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.adapter.ForwardedHeaderTransformer;

/**
 * Configuration class to provide a custom ForwardedHeaderTransformer.
 * This bean replaces the default one provided by Spring Boot.
 */
@Configuration
public class CustomForwardedHeaderConfig {

    @Bean
    public ForwardedHeaderTransformer forwardedHeaderTransformer() {
        return new PrefixRemovingForwardedHeaderTransformer();
    }

    /**
     * A custom ForwardedHeaderTransformer that specifically removes the 'X-Forwarded-Prefix'
     * header before delegating to the default transformation logic. This prevents the
     * default handler from altering the request's path, while still allowing it to
     * process other headers like X-Forwarded-Host and X-Forwarded-Proto.
     */
    private static class PrefixRemovingForwardedHeaderTransformer extends ForwardedHeaderTransformer {

        private static final Logger log = LoggerFactory.getLogger(PrefixRemovingForwardedHeaderTransformer.class);
        private static final String FORWARDED_PREFIX_HEADER = "X-Forwarded-Prefix";

        @Override
        public ServerHttpRequest apply(ServerHttpRequest request) {
            log.info("--- Intercepted Request in Custom ForwardedHeaderTransformer ---");
            log.info("Original Request URI: {}", request.getURI());
            log.info("Incoming Headers:");
            request.getHeaders().forEach((name, values) -> {
                log.info("  {}: {}", name, String.join(", ", values));
            });

            ServerHttpRequest requestToProcess = request;

            // Check if the X-Forwarded-Prefix header exists.
            if (request.getHeaders().containsKey(FORWARDED_PREFIX_HEADER)) {
                log.warn("Found '{}' header with value: '{}'. Removing it before default processing.",
                        FORWARDED_PREFIX_HEADER, request.getHeaders().getFirst(FORWARDED_PREFIX_HEADER));

                // Create a new request object that is a copy of the original,
                // but without the X-Forwarded-Prefix header.
                requestToProcess = request.mutate()
                        .headers(httpHeaders -> httpHeaders.remove(FORWARDED_PREFIX_HEADER))
                        .build();
            }

            log.info("Passing request to default handler for host, proto, etc. processing.");
            log.info("-----------------------------------------------------------------");

            // Call the parent's apply method with the (potentially) modified request.
            // The default handler will now not see the prefix header and won't modify the path.
            ServerHttpRequest m= super.apply(requestToProcess);

            m.getHeaders().forEach((name, values) -> {
                log.info("  {}: {}", name, String.join(", ", values));
            });
            return m;
        }
    }
}