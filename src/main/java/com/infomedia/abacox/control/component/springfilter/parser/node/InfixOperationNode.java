package com.infomedia.abacox.control.component.springfilter.parser.node;

import com.infomedia.abacox.control.component.springfilter.definition.FilterInfixOperator;
import java.util.List;

public class InfixOperationNode extends OperationNode {

  private final FilterNode left;

  private final FilterNode right;

  public InfixOperationNode(FilterNode left, FilterInfixOperator operator, FilterNode right) {
    super(operator);
    this.left = left;
    this.right = right;
  }

  public FilterNode getLeft() {
    return left;
  }

  public FilterNode getRight() {
    return right;
  }

  @Override
  public FilterInfixOperator getOperator() {
    return (FilterInfixOperator) super.getOperator();
  }

  @Override
  public List<FilterNode> getChildren() {
    return List.of(left, right);
  }
  
}
