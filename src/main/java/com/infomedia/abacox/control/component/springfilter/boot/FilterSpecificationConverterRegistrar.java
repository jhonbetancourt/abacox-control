package com.infomedia.abacox.control.component.springfilter.boot;

import com.infomedia.abacox.control.component.springfilter.converter.FilterSpecificationConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.ConverterRegistry;

@Configuration
public class FilterSpecificationConverterRegistrar {

  public FilterSpecificationConverterRegistrar(
      @Qualifier("sfConverterRegistry") ConverterRegistry converterRegistry,
      FilterSpecificationConverter filterSpecificationConverter) {
    converterRegistry.addConverter(filterSpecificationConverter);
  }

}
