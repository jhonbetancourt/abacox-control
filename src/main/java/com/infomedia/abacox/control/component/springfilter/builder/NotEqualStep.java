package com.infomedia.abacox.control.component.springfilter.builder;

import com.infomedia.abacox.control.component.springfilter.builder.AbstractStep.StepWithResultImpl;
import com.infomedia.abacox.control.component.springfilter.definition.FilterOperators;
import com.infomedia.abacox.control.component.springfilter.language.NotEqualOperator;
import com.infomedia.abacox.control.component.springfilter.parser.node.FilterNode;

public interface NotEqualStep extends StepWithResult {

  default NotEqualStepImpl notEqual(StepWithResult to) {
    return new NotEqualStepImpl(getOperators(), get()
        .infix(getOperators().getInfixOperator(NotEqualOperator.class), to.get()));
  }

  class NotEqualStepImpl extends StepWithResultImpl implements LogicStep {

    NotEqualStepImpl(FilterOperators operators,
        FilterNode result) {
      super(operators, result);
    }

  }

}
