package com.infomedia.abacox.control.component.springfilter.builder;

import com.infomedia.abacox.control.component.springfilter.builder.AbstractStep.StepWithResultImpl;
import com.infomedia.abacox.control.component.springfilter.definition.FilterOperators;
import com.infomedia.abacox.control.component.springfilter.language.LessThanOperator;
import com.infomedia.abacox.control.component.springfilter.parser.node.FilterNode;

public interface LessThanStep extends StepWithResult {

  default LessThanStepImpl lessThan(StepWithResult to) {
    return new LessThanStepImpl(getOperators(), get()
        .infix(getOperators().getInfixOperator(LessThanOperator.class), to.get()));
  }

  class LessThanStepImpl extends StepWithResultImpl implements LogicStep {

    LessThanStepImpl(FilterOperators operators,
        FilterNode result) {
      super(operators, result);
    }

  }

}
