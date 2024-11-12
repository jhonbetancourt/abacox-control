package com.infomedia.abacox.control.component.springfilter.language;

import com.infomedia.abacox.control.component.springfilter.definition.FilterFunction;
import org.springframework.stereotype.Component;

@Component
public class SomeFunction extends FilterFunction {

  public SomeFunction() {
    super("some");
  }

}
