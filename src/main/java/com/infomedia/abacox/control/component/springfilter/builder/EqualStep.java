package com.infomedia.abacox.control.component.springfilter.builder;

import com.infomedia.abacox.control.component.springfilter.builder.AbstractStep.StepWithResultImpl;
import com.infomedia.abacox.control.component.springfilter.definition.FilterOperators;
import com.infomedia.abacox.control.component.springfilter.language.EqualOperator;
import com.infomedia.abacox.control.component.springfilter.parser.node.FilterNode;

public interface EqualStep extends StepWithResult {

  default EqualStepImpl equal(StepWithResult to) {
    return new EqualStepImpl(getOperators(),
        get().infix(
            getOperators().getInfixOperator(EqualOperator.class),
            to.get()));
  }

  class EqualStepImpl extends StepWithResultImpl implements LogicStep {

    EqualStepImpl(FilterOperators operators,
        FilterNode result) {
      super(operators, result);
    }

  }

}
