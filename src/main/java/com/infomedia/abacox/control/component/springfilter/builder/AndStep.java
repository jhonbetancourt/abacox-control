package com.infomedia.abacox.control.component.springfilter.builder;

import com.infomedia.abacox.control.component.springfilter.builder.AbstractStep.StepWithResultImpl;
import com.infomedia.abacox.control.component.springfilter.definition.FilterOperators;
import com.infomedia.abacox.control.component.springfilter.language.AndOperator;
import com.infomedia.abacox.control.component.springfilter.parser.node.FilterNode;

public interface AndStep extends StepWithResult {

  default AndStepImpl and(StepWithResult with) {
    return new AndStepImpl(getOperators(),
        get().infix(
            getOperators().getInfixOperator(AndOperator.class),
            with.get()));
  }

  class AndStepImpl extends StepWithResultImpl implements LogicStep {

    AndStepImpl(FilterOperators operators,
        FilterNode result) {
      super(operators, result);
    }

  }

}
