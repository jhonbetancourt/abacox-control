package com.infomedia.abacox.control.component.springfilter.language;

import org.springframework.stereotype.Component;

import com.infomedia.abacox.control.component.springfilter.definition.FilterInfixOperator;

@Component
public class LessThanOperator extends FilterInfixOperator {

  public LessThanOperator() {
    super("<", 100);
  }

}
