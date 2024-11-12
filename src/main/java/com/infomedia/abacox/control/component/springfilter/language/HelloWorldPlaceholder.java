package com.infomedia.abacox.control.component.springfilter.language;

import com.infomedia.abacox.control.component.springfilter.definition.FilterPlaceholder;
import org.springframework.stereotype.Component;

@Component
public class HelloWorldPlaceholder extends FilterPlaceholder {

  protected HelloWorldPlaceholder() {
    super("hello");
  }

}
