package com.infomedia.abacox.control.component.springfilter.builder;

import com.infomedia.abacox.control.component.springfilter.builder.AbstractStep.StepWithResultImpl;
import com.infomedia.abacox.control.component.springfilter.definition.FilterOperators;
import com.infomedia.abacox.control.component.springfilter.language.GreaterThanOrEqualOperator;
import com.infomedia.abacox.control.component.springfilter.parser.node.FilterNode;

public interface GreaterThanOrEqualStep extends StepWithResult {

  default GreaterThanOrEqualStepImpl greaterThanOrEqual(StepWithResult to) {
    return new GreaterThanOrEqualStepImpl(getOperators(), get().infix(
        getOperators().getInfixOperator(GreaterThanOrEqualOperator.class), to.get()));
  }

  class GreaterThanOrEqualStepImpl extends StepWithResultImpl implements LogicStep {

    GreaterThanOrEqualStepImpl(FilterOperators operators,
        FilterNode result) {
      super(operators, result);
    }

  }

}
