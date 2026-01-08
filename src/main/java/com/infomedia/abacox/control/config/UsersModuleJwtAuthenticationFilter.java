package com.infomedia.abacox.control.config;

import com.infomedia.abacox.control.service.ModuleService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import com.infomedia.abacox.control.entity.Module;

import java.util.Map;
import java.util.concurrent.ExecutionException;

@Log4j2
@RequiredArgsConstructor
public class UsersModuleJwtAuthenticationFilter implements WebFilter {

    private final ModuleService moduleService;
    private final String internalApiKey;
    private final ObjectMapper objectMapper;
    private final String suVerificationUrl;

    private static final String VALIDATE_ACCESS_TOKEN_PATH = "/api/auth/validateAccessToken";
    private static final String VALIDATE_DOWNLOAD_TOKEN_PATH = "/api/auth/validateDownloadToken";
    private static final String SU_USERNAME_PREFIX = "abacox-su@";

    public JsonNode validateAccessToken(String token, boolean isSu, String tenantId) {
        String verificationUrl;
        if (isSu) {
            verificationUrl = suVerificationUrl;
        } else {
            Module usersModule = moduleService.getUsersModule();
            verificationUrl = usersModule.getUrl();
        }

        RestClient.Builder restClientBuilder = RestClient.builder()
                .baseUrl(verificationUrl);

        // Add X-Tenant-Id header if present
        if (tenantId != null && !tenantId.isEmpty()) {
            restClientBuilder.defaultHeader("X-Tenant-Id", tenantId);
        }

        RestClient restClient = restClientBuilder.build();

        ObjectNode requestBody = objectMapper.createObjectNode();
        requestBody.put("token", token);
        return restClient
                .post().uri(VALIDATE_ACCESS_TOKEN_PATH)
                .body(requestBody)
                .retrieve()
                .onStatus(s -> !s.is2xxSuccessful(), (req, res) -> {
                    String detail = null;
                    try {
                        JsonNode errorNode = new ObjectMapper().readTree(res.getBody());
                        detail = errorNode.get("detail").asText();
                    } catch (Exception ignored) {
                    }

                    if (detail != null) {
                        throw new RestClientException("Error while validating access token: " + detail);
                    }
                    throw new RestClientException("Error while validating access token");
                })
                .body(JsonNode.class);
    }

    public JsonNode validateDownloadToken(String token, boolean isSu, String tenantId) {
        String verificationUrl;
        if (isSu) {
            verificationUrl = suVerificationUrl;
        } else {
            Module usersModule = moduleService.getUsersModule();
            verificationUrl = usersModule.getUrl();
        }

        RestClient.Builder restClientBuilder = RestClient.builder()
                .baseUrl(verificationUrl);

        // Add X-Tenant-Id header if present
        if (tenantId != null && !tenantId.isEmpty()) {
            restClientBuilder.defaultHeader("X-Tenant-Id", tenantId);
        }

        RestClient restClient = restClientBuilder.build();

        ObjectNode requestBody = objectMapper.createObjectNode();
        requestBody.put("token", token);
        return restClient
                .post().uri(VALIDATE_DOWNLOAD_TOKEN_PATH)
                .body(requestBody)
                .retrieve()
                .onStatus(s -> !s.is2xxSuccessful(), (req, res) -> {
                    String detail = null;
                    try {
                        JsonNode errorNode = new ObjectMapper().readTree(res.getBody());
                        detail = errorNode.get("detail").asText();
                    } catch (Exception ignored) {
                    }

                    if (detail != null) {
                        throw new RestClientException("Error while validating download token: " + detail);
                    }
                    throw new RestClientException("Error while validating download token");
                })
                .body(JsonNode.class);
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        boolean secured;
        ServerWebExchangeMatcher securedPathsMatcher = moduleService.getSecuredPathsMatcher();
        try {
            secured = securedPathsMatcher.matches(exchange).toFuture().get().isMatch();
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }

        log.info("Method: {} Path: {} Secured: {}",
            exchange.getRequest().getMethod().name(), exchange.getRequest().getPath().value(), secured);

        String token = extractAccessToken(exchange);
        String queryToken = extractDownloadToken(exchange);
        String internalKey = extractInternalApiKey(exchange);
        String username = "anonymousUser";
        JsonNode userJson = null;

        // Extract tenantId from path
        String tenantId = null;
        String path = exchange.getRequest().getPath().value();
        String[] pathParts = path.split("/");

        // New path structure for proxied services is /{tenantId}/{prefix}/...
        // e.g., /my-tenant/users/api/me
        // pathParts would be: ["", "my-tenant", "users", ...]
        // We need at least 3 parts for a valid proxied path (empty string, tenant, prefix).
        // We also exclude paths for the control module itself, which start with /control.
        if (pathParts.length > 2 && !"control".equals(pathParts[1])) {
            tenantId = pathParts[1];
            log.info("Tenant context identified: {}", tenantId);
        }

        if (secured && token != null) {
            log.info("Access token: " + token.substring(0, Math.min(token.length(), 15)) + "...");
            boolean isSu = isSuAccessToken(token);
            // Pass tenantId to validation method
            userJson = validateAccessToken(token, isSu, tenantId);
            if(userJson.has("username")){
                username = userJson.get("username").asText();
                if(isSu) username = SU_USERNAME_PREFIX + username;
            }
        } else if (secured && queryToken != null) {
            log.info("Download token: " + queryToken);
            boolean isSu = isSuDownloadToken(queryToken);
            // Pass tenantId to validation method
            userJson = validateDownloadToken(queryToken, isSu, tenantId);
            if(userJson.has("username")){
                username = userJson.get("username").asText();
                if(isSu) username = SU_USERNAME_PREFIX + username;
            }
        } else if (internalApiKey != null && internalApiKey.equals(internalKey)) {
            log.info("Internal API key matched");
            username = "system";
        }

        if (username.equals("anonymousUser")&&!secured || !username.equals("anonymousUser")&&secured) {
            log.info("Authenticated user: " + username);
            if(userJson != null){
                exchange.getRequest().mutate()
                        .header("X-Username", username)
                        .build();
            }

            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username, null, null);
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(auth);
            SecurityContextHolder.setContext(context);
            return chain.filter(exchange)
                    .contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth));
        }

        return chain.filter(exchange);
    }

    private String extractAccessToken(ServerWebExchange exchange) {
        String bearerToken = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private String extractInternalApiKey(ServerWebExchange exchange) {
        return exchange.getRequest().getHeaders().getFirst("X-Internal-Api-Key");
    }

    private String extractDownloadToken(ServerWebExchange exchange) {
        if (exchange.getRequest().getMethod().name().equals("GET")) {
            return exchange.getRequest().getQueryParams().getFirst("t");
        }
        return null;
    }

    private boolean isSuDownloadToken(String token) {
        return token != null && token.startsWith("SUDL_");
    }

    private boolean isSuAccessToken(String token) {
        Map<String, Object> headers = JwtUnverifiedParser.getHeaders(token);
        return headers != null && "true".equals(String.valueOf(headers.get("su")));
    }
}