package com.infomedia.abacox.control.component.springfilter.boot;

import com.infomedia.abacox.control.component.springfilter.builder.FilterBuilder;
import com.infomedia.abacox.control.component.springfilter.converter.FilterStringConverter;
import com.infomedia.abacox.control.component.springfilter.parser.node.FilterNode;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilterNodeArgumentResolverHelper {

  protected final FilterStringConverter filterStringConverter;
  protected final FilterBuilder builder;

  public FilterNodeArgumentResolverHelper(@Lazy FilterStringConverter filterStringConverter,
                                          @Lazy FilterBuilder builder) {
    this.filterStringConverter = filterStringConverter;
    this.builder = builder;
  }

  public Mono<FilterNode> resolve(MethodParameter methodParameter,
                                  ServerWebExchange exchange) {
    Filter annotation = methodParameter.getParameterAnnotation(Filter.class);

    String parameterName = annotation != null ? annotation.parameter() : Filter.DEFAULT_PARAMETER_NAME;

    return Mono.justOrEmpty(exchange.getRequest().getQueryParams().get(parameterName))
            .flatMap(paramValues -> {
              List<FilterNode> nodes = paramValues.stream()
                      .filter(p -> p != null && !p.isBlank())
                      .map(p -> filterStringConverter.convert(p.trim()))
                      .collect(Collectors.toList());

              if (nodes.isEmpty()) {
                return Mono.empty();
              }

              FilterNode node = builder.and(
                      nodes.stream().map(builder::from).collect(Collectors.toList())).get();

              return Mono.just(node);
            });
  }
}