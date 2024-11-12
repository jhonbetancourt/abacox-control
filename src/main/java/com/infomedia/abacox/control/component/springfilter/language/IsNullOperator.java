package com.infomedia.abacox.control.component.springfilter.language;

import org.springframework.stereotype.Component;

import com.infomedia.abacox.control.component.springfilter.definition.FilterPostfixOperator;

@Component
public class IsNullOperator extends FilterPostfixOperator {

  public IsNullOperator() {
    super("is null", 100);
  }

}
