package com.infomedia.abacox.control.component.springfilter.language;

import com.infomedia.abacox.control.component.springfilter.definition.FilterInfixOperator;
import org.springframework.stereotype.Component;

@Component
public class LikeOperator extends FilterInfixOperator {

  public LikeOperator() {
    super(new String[]{"~", "like"}, 100);
  }

}
