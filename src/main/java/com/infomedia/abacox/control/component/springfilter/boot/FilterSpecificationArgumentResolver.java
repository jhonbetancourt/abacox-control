package com.infomedia.abacox.control.component.springfilter.boot;

import com.infomedia.abacox.control.component.springfilter.parser.node.FilterNode;
import com.infomedia.abacox.control.component.springfilter.converter.FilterSpecification;
import com.infomedia.abacox.control.component.springfilter.converter.FilterSpecificationConverter;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.core.MethodParameter;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.reactive.BindingContext;
import org.springframework.web.reactive.result.method.HandlerMethodArgumentResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Optional;

public class FilterSpecificationArgumentResolver implements HandlerMethodArgumentResolver {

  protected final FilterNodeArgumentResolverHelper filterNodeArgumentResolverHelper;
  protected final FilterSpecificationConverter filterSpecificationConverter;

  public FilterSpecificationArgumentResolver(
          FilterNodeArgumentResolverHelper filterNodeArgumentResolverHelper,
          FilterSpecificationConverter filterSpecificationConverter) {
    this.filterNodeArgumentResolverHelper = filterNodeArgumentResolverHelper;
    this.filterSpecificationConverter = filterSpecificationConverter;
  }

  @Override
  public boolean supportsParameter(MethodParameter methodParameter) {
    return methodParameter.getParameterType().equals(FilterSpecification.class)
            || (methodParameter.hasParameterAnnotation(Filter.class)
            && methodParameter.getParameterType().equals(Specification.class))
            || isOptionalParameter(methodParameter);
  }

  @Override
  public Mono<Object> resolveArgument(MethodParameter methodParameter,
                                      BindingContext bindingContext,
                                      ServerWebExchange exchange) {
    return filterNodeArgumentResolverHelper.resolve(methodParameter, exchange)
            .map(filterNode -> {
              if (isOptionalParameter(methodParameter)) {
                return Optional.of(filterSpecificationConverter.convert(filterNode));
              } else {
                return filterSpecificationConverter.convert(filterNode);
              }
            })
            .defaultIfEmpty(createEmptyFilterSpecification(methodParameter));
  }

  private Object createEmptyFilterSpecification(MethodParameter methodParameter) {
    if (isOptionalParameter(methodParameter)) {
      return Optional.empty();
    } else {
      return new FilterSpecification<>() {
        @Override
        public Specification<Object> and(Specification<Object> other) {
          return FilterSpecification.super.and(other);
        }

        @Override
        public Specification<Object> or(Specification<Object> other) {
          return FilterSpecification.super.or(other);
        }

        @Override
        public Predicate toPredicate(Root<Object> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
          return null;
        }

        @Override
        public FilterNode getFilter() {
          return null;
        }
      };
    }
  }

  private boolean isOptionalParameter(MethodParameter methodParameter) {
    return methodParameter.getParameterType().equals(Optional.class)
            && (methodParameter.getGenericParameterType().getTypeName()
            .equals(Optional.class.getName() + "<" + FilterSpecification.class.getName() + ">")
            || (methodParameter.hasParameterAnnotation(Filter.class)
            && methodParameter.getGenericParameterType().getTypeName()
            .equals(Optional.class.getName() + "<" + Specification.class.getName())));
  }
}