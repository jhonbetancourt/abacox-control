package com.infomedia.abacox.control.component.springfilter.boot;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.result.method.annotation.ArgumentResolverConfigurer;

@Component
public class FilterNodeArgumentResolverConfigurer implements WebFluxConfigurer {

  protected final FilterNodeArgumentResolverHelper filterNodeArgumentResolverHelper;

  public FilterNodeArgumentResolverConfigurer(
          FilterNodeArgumentResolverHelper filterNodeArgumentResolverHelper) {
    this.filterNodeArgumentResolverHelper = filterNodeArgumentResolverHelper;
  }

  @Override
  public void configureArgumentResolvers(ArgumentResolverConfigurer configurer) {
    configurer.addCustomResolver(new FilterNodeArgumentResolver(filterNodeArgumentResolverHelper));
  }
}