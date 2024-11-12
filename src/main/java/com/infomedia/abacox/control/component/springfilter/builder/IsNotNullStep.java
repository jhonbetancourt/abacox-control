package com.infomedia.abacox.control.component.springfilter.builder;

import com.infomedia.abacox.control.component.springfilter.builder.AbstractStep.StepWithResultImpl;
import com.infomedia.abacox.control.component.springfilter.definition.FilterOperators;
import com.infomedia.abacox.control.component.springfilter.language.IsNotNullOperator;
import com.infomedia.abacox.control.component.springfilter.parser.node.FilterNode;

public interface IsNotNullStep extends StepWithResult {

  default IsNotNullStepImpl isNotNull() {
    return new IsNotNullStepImpl(getOperators(),
        get().postfix(getOperators().getPostfixOperator(IsNotNullOperator.class)));
  }

  class IsNotNullStepImpl extends StepWithResultImpl implements LogicStep {

    IsNotNullStepImpl(FilterOperators operators,
        FilterNode result) {
      super(operators, result);
    }

  }

}
