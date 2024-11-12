package com.infomedia.abacox.control.component.springfilter.boot;

import com.infomedia.abacox.control.component.springfilter.parser.node.FilterNode;
import org.springframework.core.MethodParameter;
import org.springframework.web.reactive.BindingContext;
import org.springframework.web.reactive.result.method.HandlerMethodArgumentResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Optional;

public class FilterNodeArgumentResolver implements HandlerMethodArgumentResolver {

  protected final FilterNodeArgumentResolverHelper filterNodeArgumentResolverHelper;

  public FilterNodeArgumentResolver(
          FilterNodeArgumentResolverHelper filterNodeArgumentResolverHelper) {
    this.filterNodeArgumentResolverHelper = filterNodeArgumentResolverHelper;
  }

  @Override
  public boolean supportsParameter(MethodParameter methodParameter) {
    return methodParameter.getParameterType().equals(FilterNode.class)
            || isOptionalParameter(methodParameter);
  }

  @Override
  public Mono<Object> resolveArgument(MethodParameter methodParameter,
                                      BindingContext bindingContext,
                                      ServerWebExchange exchange) {
    return filterNodeArgumentResolverHelper.resolve(methodParameter, exchange)
            .map(result -> {
              if (isOptionalParameter(methodParameter)) {
                return Optional.ofNullable(result);
              } else {
                return result;
              }
            });
  }

  private boolean isOptionalParameter(MethodParameter methodParameter) {
    return methodParameter.getParameterType().equals(Optional.class)
            && methodParameter.getGenericParameterType().getTypeName()
            .equals(Optional.class.getName() + "<" + FilterNode.class.getName() + ">");
  }
}