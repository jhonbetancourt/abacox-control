package com.infomedia.abacox.control.component.springfilter.language;

import com.infomedia.abacox.control.component.springfilter.definition.FilterInfixOperator;
import org.springframework.stereotype.Component;

@Component
public class NotInOperator extends FilterInfixOperator {

  public NotInOperator() {
    super("not in", 100);
  }

}
