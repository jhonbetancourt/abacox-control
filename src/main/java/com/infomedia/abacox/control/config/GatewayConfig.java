package com.infomedia.abacox.control.config;

import java.time.Duration;
import org.springframework.cloud.gateway.config.HttpClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public HttpClientCustomizer httpClientCustomizer() {
        return httpClient -> httpClient.resolver(spec -> 
            spec.cacheMaxTimeToLive(Duration.ofSeconds(30)) // Max cache time
                .cacheMinTimeToLive(Duration.ofSeconds(15)) // Min cache time
                .queryTimeout(Duration.ofSeconds(5))        // Timeout if DNS is slow
        );
    }
}