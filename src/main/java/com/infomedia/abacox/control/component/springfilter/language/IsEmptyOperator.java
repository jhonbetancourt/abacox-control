package com.infomedia.abacox.control.component.springfilter.language;

import com.infomedia.abacox.control.component.springfilter.definition.FilterPostfixOperator;
import org.springframework.stereotype.Component;

@Component
public class IsEmptyOperator extends FilterPostfixOperator {

  public IsEmptyOperator() {
    super("is empty", 100);
  }

}
