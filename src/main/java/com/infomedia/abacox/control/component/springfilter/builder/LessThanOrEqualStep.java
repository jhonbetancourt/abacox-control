package com.infomedia.abacox.control.component.springfilter.builder;

import com.infomedia.abacox.control.component.springfilter.builder.AbstractStep.StepWithResultImpl;
import com.infomedia.abacox.control.component.springfilter.definition.FilterOperators;
import com.infomedia.abacox.control.component.springfilter.language.LessThanOrEqualOperator;
import com.infomedia.abacox.control.component.springfilter.parser.node.FilterNode;

public interface LessThanOrEqualStep extends StepWithResult {

  default LessThanOrEqualStepImpl lessThanOrEqual(StepWithResult to) {
    return new LessThanOrEqualStepImpl(getOperators(), get().infix(
        getOperators().getInfixOperator(LessThanOrEqualOperator.class), to.get()));
  }

  class LessThanOrEqualStepImpl extends StepWithResultImpl implements LogicStep {

    LessThanOrEqualStepImpl(FilterOperators operators,
        FilterNode result) {
      super(operators, result);
    }

  }

}
