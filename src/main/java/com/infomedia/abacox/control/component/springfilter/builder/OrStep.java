package com.infomedia.abacox.control.component.springfilter.builder;

import com.infomedia.abacox.control.component.springfilter.builder.AbstractStep.StepWithResultImpl;
import com.infomedia.abacox.control.component.springfilter.definition.FilterOperators;
import com.infomedia.abacox.control.component.springfilter.language.OrOperator;
import com.infomedia.abacox.control.component.springfilter.parser.node.FilterNode;

public interface OrStep extends StepWithResult {

  default OrStepImpl or(StepWithResult with) {
    return new OrStepImpl(getOperators(),
        get().infix(getOperators().getInfixOperator(OrOperator.class),
            with.get()));
  }

  class OrStepImpl extends StepWithResultImpl implements LogicStep {

    OrStepImpl(FilterOperators operators,
        FilterNode result) {
      super(operators, result);
    }

  }

}
