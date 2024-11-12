package com.infomedia.abacox.control.component.springfilter.builder;

import com.infomedia.abacox.control.component.springfilter.definition.FilterOperators;
import org.springframework.stereotype.Service;

@Service
public class FilterBuilder extends RootStep {

  public FilterBuilder(FilterOperators operators) {
    super(operators);
  }

}
