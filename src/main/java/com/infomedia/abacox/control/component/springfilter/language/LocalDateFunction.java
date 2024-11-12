package com.infomedia.abacox.control.component.springfilter.language;

import com.infomedia.abacox.control.component.springfilter.definition.FilterFunction;
import org.springframework.stereotype.Component;

@Component
public class LocalDateFunction extends FilterFunction {

  public LocalDateFunction() {
    super("localDate");
  }

}
