package com.infomedia.abacox.control.component.springfilter.boot;

import com.infomedia.abacox.control.component.springfilter.converter.FilterStringConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.ConverterRegistry;

@Configuration
public class FilterStringConverterRegistrar {

  public FilterStringConverterRegistrar(
      @Qualifier("sfConverterRegistry") ConverterRegistry converterRegistry,
      FilterStringConverter filterStringConverter) {
    converterRegistry.addConverter(filterStringConverter);
  }

}
