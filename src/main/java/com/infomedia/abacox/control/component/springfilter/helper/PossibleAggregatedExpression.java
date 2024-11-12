package com.infomedia.abacox.control.component.springfilter.helper;

import com.infomedia.abacox.control.component.springfilter.parser.node.FunctionNode;
import com.infomedia.abacox.control.component.springfilter.parser.node.OperationNode;

public interface PossibleAggregatedExpression {

  default boolean isAggregated(FunctionNode functionNode) {
    return false;
  }

  default boolean isAggregated(OperationNode operationNode) {
    return false;
  }

}
