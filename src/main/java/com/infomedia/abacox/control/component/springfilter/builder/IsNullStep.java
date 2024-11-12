package com.infomedia.abacox.control.component.springfilter.builder;

import com.infomedia.abacox.control.component.springfilter.builder.AbstractStep.StepWithResultImpl;
import com.infomedia.abacox.control.component.springfilter.definition.FilterOperators;
import com.infomedia.abacox.control.component.springfilter.language.IsNullOperator;
import com.infomedia.abacox.control.component.springfilter.parser.node.FilterNode;

public interface IsNullStep extends StepWithResult {

  default IsNullStepImpl isNull() {
    return new IsNullStepImpl(getOperators(),
        get().postfix(getOperators().getPostfixOperator(IsNullOperator.class)));
  }

  class IsNullStepImpl extends StepWithResultImpl implements LogicStep {

    IsNullStepImpl(FilterOperators operators,
        FilterNode result) {
      super(operators, result);
    }

  }

}
