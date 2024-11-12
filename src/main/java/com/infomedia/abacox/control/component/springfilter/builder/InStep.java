package com.infomedia.abacox.control.component.springfilter.builder;

import com.infomedia.abacox.control.component.springfilter.builder.AbstractStep.StepWithResultImpl;
import com.infomedia.abacox.control.component.springfilter.definition.FilterOperators;
import com.infomedia.abacox.control.component.springfilter.language.InOperator;
import com.infomedia.abacox.control.component.springfilter.parser.node.FilterNode;


public interface InStep extends StepWithResult {

  default InStepImpl in(StepWithResult to) {
    return new InStepImpl(getOperators(),
        get().infix(getOperators().getInfixOperator(InOperator.class),
            to.get()));
  }

  class InStepImpl extends StepWithResultImpl implements LogicStep {

    InStepImpl(FilterOperators operators,
        FilterNode result) {
      super(operators, result);
    }

  }

}
