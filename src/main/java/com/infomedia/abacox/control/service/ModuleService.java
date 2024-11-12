package com.infomedia.abacox.control.service;

import com.infomedia.abacox.control.component.events.EventsWebSocketClient;
import com.infomedia.abacox.control.constants.ModuleType;
import com.infomedia.abacox.control.dto.gateway.RouteDefinition;
import com.infomedia.abacox.control.dto.module.CreateModuleUrl;
import com.infomedia.abacox.control.dto.moduleext.MEndpointInfo;
import com.infomedia.abacox.control.dto.moduleext.ModuleInfo;
import com.infomedia.abacox.control.dto.superclass.ActivationDto;
import com.infomedia.abacox.control.entity.Module;
import com.infomedia.abacox.control.entity.ModuleEndpoint;
import com.infomedia.abacox.control.exception.ResourceNotFoundException;
import com.infomedia.abacox.control.repository.ModuleRepository;
import com.infomedia.abacox.control.service.common.CrudService;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationContext;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.reactive.result.method.RequestMappingInfo;
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.pattern.PathPattern;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Log4j2
public class ModuleService extends CrudService<Module, UUID, ModuleRepository> {
    private final GatewayService gatewayService;
    private final ApplicationContext applicationContext;
    private RestClient restClient;
    private final EventsWebSocketClient eventsWebSocketClient;
    public ModuleService(ModuleRepository repository, GatewayService gatewayService, ApplicationContext applicationContext
            , EventsWebSocketClient eventsWebSocketClient) {
        super(repository);
        this.gatewayService = gatewayService;
        this.applicationContext = applicationContext;
        this.eventsWebSocketClient = eventsWebSocketClient;
        restClient = RestClient.builder().build();
    }

    public Module buildFromDto(CreateModuleUrl cDto) {

        RestClient restClient = RestClient.builder()
                .baseUrl(cDto.getUrl())
                .build();

        ModuleInfo moduleInfo = getModuleInfo(cDto.getUrl(), restClient);
        List<MEndpointInfo> endpointsInfo = getEndpointsInfo(cDto.getUrl(), restClient);

        Module module = Module.builder()
                .name(moduleInfo.getName())
                .type(ModuleType.valueOf(moduleInfo.getType()))
                .description(moduleInfo.getDescription())
                .version(moduleInfo.getVersion())
                .prefix(moduleInfo.getPrefix())
                .url(cDto.getUrl())
                .build();

        Set<ModuleEndpoint> endpoints = endpointsInfo.stream()
                .map(e -> ModuleEndpoint.builder()
                        .path(e.getPath())
                        .method(e.getMethod())
                        .secured(e.isSecured())
                        .module(module)
                        .build())
                .collect(Collectors.toSet());

        module.setEndpoints(endpoints);
        validate(module);
        return module;
    }

    @Transactional
    public void updateModuleInfo(UUID moduleId){
        Module module = get(moduleId);

        ModuleInfo moduleInfo = getModuleInfo(module.getUrl(), restClient);
        List<MEndpointInfo> endpointsInfo = getEndpointsInfo(module.getUrl(), restClient);

        module = module.toBuilder()
                .name(moduleInfo.getName())
                .description(moduleInfo.getDescription())
                .version(moduleInfo.getVersion())
                .prefix(moduleInfo.getPrefix())
                .build();

        Module finalModule = module;
        Set<ModuleEndpoint> endpoints = endpointsInfo.stream()
                .map(e -> ModuleEndpoint.builder()
                        .path(e.getPath())
                        .method(e.getMethod())
                        .secured(e.isSecured())
                        .module(finalModule)
                        .build())
                .collect(Collectors.toSet());

        module.setEndpoints(endpoints);
        validate(module);
        save(module);
        updateGateway();
    }

    private List<MEndpointInfo> getEndpointsInfo(String url, RestClient restClient) {
        ParameterizedTypeReference<List<MEndpointInfo>> typeRef = new ParameterizedTypeReference<>() {};
        return restClient
        .get().uri(url + "/api/module/endpoints")
        .retrieve()
        .onStatus( s -> !s.is2xxSuccessful(), (req, res) -> {
            throw new RestClientException("Error while fetching module endpoints");
        })
        .body(typeRef);
    }

    private ModuleInfo getModuleInfo(String url, RestClient restClient) {
        return restClient
                .get().uri(url + "/api/module/info")
                .retrieve()
                .onStatus( s -> !s.is2xxSuccessful(), (req, res) -> {
                    throw new RestClientException("Error while fetching module info");
                })
                .body(ModuleInfo.class);
    }

    private void validate(Module module) {
        if (module.getType().equals(ModuleType.USERS) && getRepository().existsByType(ModuleType.USERS)) {
            throw new IllegalArgumentException("Only one module of type USERS is allowed");
        }
        if (module.getType().equals(ModuleType.CONTROL) && getRepository().existsByType(ModuleType.CONTROL)) {
            throw new IllegalArgumentException("Only one module of type CONTROL is allowed");
        }
    }

    private void validateActivation(boolean active, Module module) {
        if(active&&module.getType().equals(ModuleType.CONTROL)){
            throw new IllegalArgumentException("Cannot deactivate control module");
        }
    }

    @Transactional
    public Module create(CreateModuleUrl cDto) {
        Module module = save(buildFromDto(cDto));
        updateGateway();
        eventsWebSocketClient.connect(module);
        return module;
    }

    @Transactional
    public void delete(UUID id) {
        Module module = get(id);
        deleteById(id);
        updateGateway();
        eventsWebSocketClient.disconnect(module);
    }

    public Module getUsersModule() {
        return getRepository().findByType(ModuleType.USERS)
                .orElseThrow(() -> new ResourceNotFoundException(Module.class, "USERS module"));
    }

    @Transactional
    @Override
    public Module changeActivation(UUID id, boolean active) {
        Module module = getRepository().findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Module.class, id));
        validateActivation(active, module);
        module = super.changeActivation(id, active);
        updateGateway();
        if(module.isActive()) {
            eventsWebSocketClient.connect(module);
        }else{
            eventsWebSocketClient.disconnect(module);
        }
        return module;
    }

    private void initControlModule(){
        Module module = getRepository().findByType(ModuleType.CONTROL)
                .orElse(new Module());
        module = module.toBuilder()
                .name("Control Module")
                .type(ModuleType.CONTROL)
                .description("Control Module")
                .version("5.1")
                .prefix("control")
                .url("N/A")
                .build();

        if (module.getEndpoints() == null) {
            module.setEndpoints(new HashSet<>());
        }

        Module finalModule = module;
        List<MEndpointInfo> endpoints = getEnpoints();

        //remove not present endpoints
        module.getEndpoints().removeIf(e -> endpoints.stream()
                .noneMatch(ei -> ei.getPath().equals(e.getPath())&&ei.getMethod().equals(e.getMethod())));

        //update existing endpoints and add new ones
        endpoints.forEach(ei -> {
            ModuleEndpoint endpoint = finalModule.getEndpoints().stream()
                    .filter(e -> e.getPath().equals(ei.getPath()) && e.getMethod().equals(ei.getMethod()))
                    .findFirst()
                    .orElse(ModuleEndpoint.builder()
                            .module(finalModule)
                            .path(ei.getPath())
                            .method(ei.getMethod())
                            .build());
            endpoint.setSecured(ei.isSecured());
            finalModule.getEndpoints().add(endpoint);
        });
        save(module);
    }

    public ServerWebExchangeMatcher getSecuredPathsMatcher() {
        List<RouteDefinition> routes = new ArrayList<>();
        for (Module module : getAllActive()) {
            for (ModuleEndpoint endpoint : module.getEndpoints()) {
                if (endpoint.isActive() && endpoint.isSecured()) {
                    RouteDefinition route = RouteDefinition.builder()
                            .id(endpoint.getId().toString())
                            .prefix(module.getPrefix())
                            .method(endpoint.getMethod())
                            .baseUrl(module.getUrl())
                            .path(endpoint.getPath())
                            .build();
                    routes.add(route);
                }
            }
        }
        return ServerWebExchangeMatchers.pathMatchers(routes.stream()
                .map(rd -> rd.getPrefix() + rd.getPath())
                .toArray(String[]::new));
    }

    public boolean moduleExistsByType(ModuleType type) {
        return getRepository().existsByType(type);
    }

    public boolean moduleExistsByTypeAndActive(ModuleType type) {
        return getRepository().existsByTypeAndActive(type, true);
    }

    private String[] publicPaths() {
        return new String[] {"/v3/api-docs/**"
                , "/swagger-ui/**"
                , "/swagger-ui.html"
                , "/error"};
    }

    public boolean isPublicPath(String path) {
        return Arrays.asList(publicPaths()).contains(path);
    }

    public List<MEndpointInfo> getEnpoints() {
        List<MEndpointInfo> endpointInfos = new ArrayList<>();
        RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = mapping.getHandlerMethods();
        handlerMethods.forEach((key, value) -> {
            Set<String> patterns = key.getDirectPaths();
            if (patterns.isEmpty()) {
                patterns = key.getPatternsCondition().getPatterns().stream().map(PathPattern::toString).collect(Collectors.toSet());
            }

            Set<RequestMethod> methods = key.getMethodsCondition().getMethods();
            if (methods.isEmpty()) {
                methods = Set.of(RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.PATCH);
            }

            for (RequestMethod method : methods) {
                for (String pattern : patterns) {
                    if(pattern.startsWith("/control")){
                        pattern = pattern.substring(8);
                    }
                    endpointInfos.add(new MEndpointInfo(method.toString(), pattern, !isPublicPath(pattern)));
                }
            }
        });
        endpointInfos.sort(Comparator.comparing(MEndpointInfo::getPath));
        return endpointInfos;
    }

    public void initModules() {
        initControlModule();
        updateGateway();
        for (Module module : getRepository().findByTypeNotAndActive(ModuleType.CONTROL, true)) {
            eventsWebSocketClient.connect(module);
        }
    }

    public boolean isModuleConnected(UUID moduleId) {
        Module module = get(moduleId);
        if(module.getType().equals(ModuleType.CONTROL)){
            return true;
        }
        return eventsWebSocketClient.isConnected(module);
    }

    @Transactional
    public void updateGateway() {
        List<RouteDefinition> routes = new ArrayList<>();
        for (Module module : getRepository().findByTypeNotAndActive(ModuleType.CONTROL, true)) {
            for (ModuleEndpoint endpoint : module.getEndpoints()) {
                RouteDefinition route = RouteDefinition.builder()
                        .id(endpoint.getId().toString())
                        .prefix(module.getPrefix())
                        .method(endpoint.getMethod())
                        .baseUrl(module.getUrl())
                        .path(endpoint.getPath())
                        .build();
                routes.add(route);
            }
        }
        gatewayService.updateRoutes(routes);
    }
}
