package com.infomedia.abacox.control.component.springfilter.parser.node;

import com.infomedia.abacox.control.component.springfilter.definition.FilterPrefixOperator;
import java.util.List;

public class PrefixOperationNode extends OperationNode {

  private final FilterNode right;

  public PrefixOperationNode(FilterPrefixOperator operator, FilterNode right) {
    super(operator);
    this.right = right;
  }

  public FilterNode getRight() {
    return right;
  }

  @Override
  public FilterPrefixOperator getOperator() {
    return (FilterPrefixOperator) super.getOperator();
  }

  @Override
  public List<FilterNode> getChildren() {
    return List.of(right);
  }

}
