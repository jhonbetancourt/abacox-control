package com.infomedia.abacox.control.component.springfilter.language;

import com.infomedia.abacox.control.component.springfilter.definition.FilterInfixOperator;
import org.springframework.stereotype.Component;

@Component
public class NotEqualOperator extends FilterInfixOperator {

  public NotEqualOperator() {
    super(new String[]{"!", "<>"}, 100);
  }

}
