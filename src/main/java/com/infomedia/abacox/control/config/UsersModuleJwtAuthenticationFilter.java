package com.infomedia.abacox.control.config;

import com.infomedia.abacox.control.constants.ModuleType;
import com.infomedia.abacox.control.service.ModuleService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import com.infomedia.abacox.control.entity.Module;

import java.util.Base64;
import java.util.concurrent.ExecutionException;

@Log4j2
@RequiredArgsConstructor
public class UsersModuleJwtAuthenticationFilter implements WebFilter {

    private final ModuleService moduleService;
    private final ObjectMapper objectMapper;

    private static final String VALIDATE_ACCESS_TOKEN_PATH = "/api/auth/validateAccessToken";
    private static final String VALIDATE_DOWNLOAD_TOKEN_PATH = "/api/auth/validateDownloadToken";

    public JsonNode validateAccessToken(String token) {
        Module usersModule = moduleService.getUsersModule();
        RestClient restClient = RestClient.builder()
                .baseUrl(usersModule.getUrl())
                .build();
        ObjectNode requestBody = objectMapper.createObjectNode();
        requestBody.put("token", token);
        //try parse detail from response
        return restClient
                .post().uri(VALIDATE_ACCESS_TOKEN_PATH)
                .body(requestBody)
                .retrieve()
                .onStatus(s -> !s.is2xxSuccessful(), (req, res) -> {
                    //try parse detail from response
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

    public JsonNode validateDownloadToken(String token) {
        Module usersModule = moduleService.getUsersModule();
        RestClient restClient = RestClient.builder()
                .baseUrl(usersModule.getUrl())
                .build();
        ObjectNode requestBody = objectMapper.createObjectNode();
        requestBody.put("token", token);
        return restClient
                .post().uri(VALIDATE_DOWNLOAD_TOKEN_PATH)
                .body(requestBody)
                .retrieve()
                .onStatus(s -> !s.is2xxSuccessful(), (req, res) -> {
                    //try parse details from response
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
        if(!moduleService.moduleExistsByTypeAndActive(ModuleType.USERS)) {
            secured = false;
        }else{
            ServerWebExchangeMatcher securedPathsMatcher = moduleService.getSecuredPathsMatcher();
            try {
                secured = securedPathsMatcher.matches(exchange).toFuture().get().isMatch();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
        log.info("Path: " + exchange.getRequest().getPath().value() + " Secured: " + secured);

        String token = extractAccessToken(exchange);
        String queryToken = extractDownloadToken(exchange);
        String username = "anonymousUser";
        JsonNode userJson = null;

        if (secured && token != null) {
            log.info("Access token: " + token);
            userJson = validateAccessToken(token);
            if(userJson.has("username")){
                username = userJson.get("username").asText();
            }
        } else if (secured && queryToken != null) {
            log.info("Download token: " + queryToken);
            userJson = validateDownloadToken(queryToken);
            if(userJson.has("username")){
                username = userJson.get("username").asText();
            }
        }

        if (username.equals("anonymousUser")&&!secured || !username.equals("anonymousUser")&&secured) {
            log.info("Authenticated user: " + username);
            //add to headers
            if(userJson != null){
                exchange.getRequest().mutate()
                        .header("X-User", Base64.getEncoder().encodeToString(userJson.toString().getBytes()))
                        .build();
            }

            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username, null, null);
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

    private String extractDownloadToken(ServerWebExchange exchange) {
        if (exchange.getRequest().getMethod().name().equals("GET")) {
            return exchange.getRequest().getQueryParams().getFirst("t");
        }
        return null;
    }
}