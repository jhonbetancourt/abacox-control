package com.infomedia.abacox.control.component.springfilter.builder;

import com.infomedia.abacox.control.component.springfilter.builder.AbstractStep.StepWithResultImpl;
import com.infomedia.abacox.control.component.springfilter.definition.FilterOperators;
import com.infomedia.abacox.control.component.springfilter.language.NotInOperator;
import com.infomedia.abacox.control.component.springfilter.parser.node.FilterNode;


public interface NotInStep extends StepWithResult {

  default NotInStepImpl notIn(StepWithResult to) {
    return new NotInStepImpl(getOperators(),
        get().infix(getOperators().getInfixOperator(NotInOperator.class),
            to.get()));
  }

  class NotInStepImpl extends StepWithResultImpl implements LogicStep {

    NotInStepImpl(FilterOperators operators,
        FilterNode result) {
      super(operators, result);
    }

  }

}
