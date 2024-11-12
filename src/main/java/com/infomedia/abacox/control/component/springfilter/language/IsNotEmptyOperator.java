package com.infomedia.abacox.control.component.springfilter.language;

import org.springframework.stereotype.Component;

import com.infomedia.abacox.control.component.springfilter.definition.FilterPostfixOperator;

@Component
public class IsNotEmptyOperator extends FilterPostfixOperator {

  public IsNotEmptyOperator() {
    super("is not empty", 100);
  }

}
