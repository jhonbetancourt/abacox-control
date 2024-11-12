package com.infomedia.abacox.control.component.springfilter.language;

import org.springframework.stereotype.Component;

import com.infomedia.abacox.control.component.springfilter.definition.FilterInfixOperator;

@Component
public class GreaterThanOperator extends FilterInfixOperator {

  public GreaterThanOperator() {
    super(">", 100);
  }

}
