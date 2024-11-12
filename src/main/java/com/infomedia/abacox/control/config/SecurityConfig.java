package com.infomedia.abacox.control.config;

import com.infomedia.abacox.control.service.ModuleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.HttpStatusServerEntryPoint;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import java.util.Arrays;

@Configuration
@EnableWebFluxSecurity
@Log4j2
public class SecurityConfig {

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("*"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http, ModuleService moduleService, ObjectMapper objectMapper) {
        http.cors(corsSpec -> corsSpec.configurationSource(corsConfigurationSource()))
            .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
            .httpBasic(httpBasicSpec -> httpBasicSpec.authenticationEntryPoint(new HttpStatusServerEntryPoint(HttpStatus.UNAUTHORIZED)))
            .csrf(ServerHttpSecurity.CsrfSpec::disable)
            .authorizeExchange(exchanges ->
                exchanges
                    .pathMatchers(HttpMethod.OPTIONS).permitAll() // Allow OPTIONS requests
                    .anyExchange().authenticated()
            )
            .addFilterAt(new UsersModuleJwtAuthenticationFilter(moduleService, objectMapper), SecurityWebFiltersOrder.AUTHENTICATION)
            .securityContextRepository(NoOpServerSecurityContextRepository.getInstance());
        
        return http.build();
    }
}