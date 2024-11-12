package com.infomedia.abacox.control.component.springfilter.builder;

import com.infomedia.abacox.control.component.springfilter.builder.AbstractStep.StepWithResultImpl;
import com.infomedia.abacox.control.component.springfilter.definition.FilterOperators;
import com.infomedia.abacox.control.component.springfilter.language.GreaterThanOperator;
import com.infomedia.abacox.control.component.springfilter.parser.node.FilterNode;

public interface GreaterThanStep extends StepWithResult {

  default GreaterThanStepImpl greaterThan(StepWithResult to) {
    return new GreaterThanStepImpl(getOperators(), get()
        .infix(getOperators().getInfixOperator(GreaterThanOperator.class), to.get()));
  }

  class GreaterThanStepImpl extends StepWithResultImpl implements LogicStep {

    GreaterThanStepImpl(FilterOperators operators,
        FilterNode result) {
      super(operators, result);
    }

  }

}
