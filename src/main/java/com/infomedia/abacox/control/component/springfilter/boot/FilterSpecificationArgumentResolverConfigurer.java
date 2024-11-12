package com.infomedia.abacox.control.component.springfilter.boot;

import com.infomedia.abacox.control.component.springfilter.converter.FilterSpecificationConverter;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.result.method.annotation.ArgumentResolverConfigurer;

@Component
public class FilterSpecificationArgumentResolverConfigurer implements WebFluxConfigurer {

  protected final FilterNodeArgumentResolverHelper filterNodeArgumentResolverHelper;
  protected final FilterSpecificationConverter filterSpecificationConverter;

  public FilterSpecificationArgumentResolverConfigurer(
          @Lazy FilterNodeArgumentResolverHelper filterNodeArgumentResolverHelper,
          @Lazy FilterSpecificationConverter filterSpecificationConverter) {
    this.filterNodeArgumentResolverHelper = filterNodeArgumentResolverHelper;
    this.filterSpecificationConverter = filterSpecificationConverter;
  }

  @Override
  public void configureArgumentResolvers(ArgumentResolverConfigurer configurer) {
    configurer.addCustomResolver(new FilterSpecificationArgumentResolver(
            filterNodeArgumentResolverHelper,
            filterSpecificationConverter));
  }
}