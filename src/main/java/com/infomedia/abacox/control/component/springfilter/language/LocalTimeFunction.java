package com.infomedia.abacox.control.component.springfilter.language;

import com.infomedia.abacox.control.component.springfilter.definition.FilterFunction;
import org.springframework.stereotype.Component;

@Component
public class LocalTimeFunction extends FilterFunction {

  public LocalTimeFunction() {
    super("localTime");
  }

}
