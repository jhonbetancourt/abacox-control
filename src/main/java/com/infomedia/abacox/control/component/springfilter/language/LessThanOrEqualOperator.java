package com.infomedia.abacox.control.component.springfilter.language;

import com.infomedia.abacox.control.component.springfilter.definition.FilterInfixOperator;
import org.springframework.stereotype.Component;

@Component
public class LessThanOrEqualOperator extends FilterInfixOperator {

  public LessThanOrEqualOperator() {
    super(new String[]{"<:", "<="}, 100);
  }

}
