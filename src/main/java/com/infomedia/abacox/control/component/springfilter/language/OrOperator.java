package com.infomedia.abacox.control.component.springfilter.language;

import com.infomedia.abacox.control.component.springfilter.definition.FilterInfixOperator;
import org.springframework.stereotype.Component;

@Component
public class OrOperator extends FilterInfixOperator {

  public OrOperator() {
    super("or", 25);
  }

}
