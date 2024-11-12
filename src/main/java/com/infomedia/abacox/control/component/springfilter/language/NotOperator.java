package com.infomedia.abacox.control.component.springfilter.language;

import org.springframework.stereotype.Component;

import com.infomedia.abacox.control.component.springfilter.definition.FilterPrefixOperator;

@Component
public class NotOperator extends FilterPrefixOperator {

  public NotOperator() {
    super("not", 150);
  }

}
