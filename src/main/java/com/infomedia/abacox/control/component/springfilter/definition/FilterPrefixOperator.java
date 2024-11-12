package com.infomedia.abacox.control.component.springfilter.definition;

import com.infomedia.abacox.control.component.springfilter.parser.node.FilterNode;
import com.infomedia.abacox.control.component.springfilter.parser.node.PrefixOperationNode;

public abstract class FilterPrefixOperator extends FilterOperator {

  public FilterPrefixOperator(String[] tokens, int priority) {
    super(tokens, priority);
  }

  public FilterPrefixOperator(String token, int priority) {
    super(token, priority);
  }

  public PrefixOperationNode toNode(FilterNode right) {
    return new PrefixOperationNode(this, right);
  }

}
