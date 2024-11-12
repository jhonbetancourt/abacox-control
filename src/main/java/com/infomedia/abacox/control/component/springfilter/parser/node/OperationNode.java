package com.infomedia.abacox.control.component.springfilter.parser.node;

import com.infomedia.abacox.control.component.springfilter.definition.FilterOperator;

public abstract class OperationNode extends FilterNode {

  private final FilterOperator operator;

  public OperationNode(FilterOperator operator) {
    this.operator = operator;
  }

  public FilterOperator getOperator() {
    return operator;
  }

}
