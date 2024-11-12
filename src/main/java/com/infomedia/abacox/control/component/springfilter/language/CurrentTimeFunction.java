package com.infomedia.abacox.control.component.springfilter.language;

import com.infomedia.abacox.control.component.springfilter.definition.FilterFunction;
import org.springframework.stereotype.Component;

@Component
public class CurrentTimeFunction extends FilterFunction {

  public CurrentTimeFunction() {
    super("currentTime");
  }

}
