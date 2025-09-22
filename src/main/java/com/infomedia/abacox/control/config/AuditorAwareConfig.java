package com.infomedia.abacox.control.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Configuration
@EnableJpaAuditing
public class AuditorAwareConfig {

    @Bean
    public AuditorAware<String> auditorProvider() {
        // The lambda implementation for the synchronous AuditorAware interface
        return () -> {
            // Use ReactiveSecurityContextHolder to get the context
            try {
                return Optional.ofNullable(
                        ReactiveSecurityContextHolder.getContext()
                                .map(SecurityContext::getAuthentication)
                                .filter(Authentication::isAuthenticated)
                                .map(Authentication::getName)
                                .filter(name -> !name.equalsIgnoreCase("anonymousUser"))
                        // The block() call is necessary here to bridge the reactive Mono
                        // to the synchronous Optional required by AuditorAware.
                                .toFuture().get()
                );
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        };
    }
}