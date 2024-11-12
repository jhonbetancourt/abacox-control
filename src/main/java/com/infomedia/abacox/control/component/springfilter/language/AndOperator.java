package com.infomedia.abacox.control.component.springfilter.language;

import com.infomedia.abacox.control.component.springfilter.definition.FilterInfixOperator;
import org.springframework.stereotype.Component;

@Component
public class AndOperator extends FilterInfixOperator {

  public AndOperator() {
    super("and", 50);
  }

}
