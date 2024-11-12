package com.infomedia.abacox.control.component.springfilter.language;

import com.infomedia.abacox.control.component.springfilter.definition.FilterPostfixOperator;
import org.springframework.stereotype.Component;

@Component
public class IsNotNullOperator extends FilterPostfixOperator {

  public IsNotNullOperator() {
    super("is not null", 100);
  }

}
