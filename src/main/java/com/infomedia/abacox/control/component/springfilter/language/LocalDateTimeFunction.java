package com.infomedia.abacox.control.component.springfilter.language;

import com.infomedia.abacox.control.component.springfilter.definition.FilterFunction;
import org.springframework.stereotype.Component;

@Component
public class LocalDateTimeFunction extends FilterFunction {

  public LocalDateTimeFunction() {
    super("localDateTime");
  }

}
