package com.infomedia.abacox.control.component.springfilter.definition;

import com.infomedia.abacox.control.component.springfilter.parser.node.FilterNode;
import com.infomedia.abacox.control.component.springfilter.parser.node.PostfixOperationNode;

public abstract class FilterPostfixOperator extends FilterOperator {

  public FilterPostfixOperator(String[] tokens, int priority) {
    super(tokens, priority);
  }

  public FilterPostfixOperator(String token, int priority) {
    super(token, priority);
  }

  public PostfixOperationNode toNode(FilterNode right) {
    return new PostfixOperationNode(right, this);
  }

}
