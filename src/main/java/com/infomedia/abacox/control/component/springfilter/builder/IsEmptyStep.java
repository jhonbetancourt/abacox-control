package com.infomedia.abacox.control.component.springfilter.builder;

import com.infomedia.abacox.control.component.springfilter.builder.AbstractStep.StepWithResultImpl;
import com.infomedia.abacox.control.component.springfilter.definition.FilterOperators;
import com.infomedia.abacox.control.component.springfilter.language.IsEmptyOperator;
import com.infomedia.abacox.control.component.springfilter.parser.node.FilterNode;

public interface IsEmptyStep extends StepWithResult {

  default IsEmptyStepImpl isEmpty() {
    return new IsEmptyStepImpl(getOperators(),
        get().postfix(getOperators().getPostfixOperator(IsEmptyOperator.class)));
  }

  class IsEmptyStepImpl extends StepWithResultImpl implements LogicStep {

    IsEmptyStepImpl(FilterOperators operators,
        FilterNode result) {
      super(operators, result);
    }

  }

}
