package com.infomedia.abacox.control.component.springfilter.language;

import com.infomedia.abacox.control.component.springfilter.definition.FilterInfixOperator;
import org.springframework.stereotype.Component;

@Component
public class InsensitiveLikeOperator extends FilterInfixOperator {

  public InsensitiveLikeOperator() {
    super(new String[]{"~~", "ilike"}, 100);
  }

}
