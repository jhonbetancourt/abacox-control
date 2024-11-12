package com.infomedia.abacox.control.component.springfilter.builder;

import com.infomedia.abacox.control.component.springfilter.builder.AbstractStep.StepWithResultImpl;
import com.infomedia.abacox.control.component.springfilter.definition.FilterOperators;
import com.infomedia.abacox.control.component.springfilter.language.IsNotEmptyOperator;
import com.infomedia.abacox.control.component.springfilter.parser.node.FilterNode;

public interface IsNotEmptyStep extends StepWithResult {

  default IsNotEmptyStepImpl isNotEmpty() {
    return new IsNotEmptyStepImpl(getOperators(),
        get().postfix(getOperators().getPostfixOperator(IsNotEmptyOperator.class)));
  }

  class IsNotEmptyStepImpl extends StepWithResultImpl implements LogicStep {

    IsNotEmptyStepImpl(FilterOperators operators,
        FilterNode result) {
      super(operators, result);
    }

  }

}
