package com.infomedia.abacox.control.component.springfilter.builder;

import com.infomedia.abacox.control.component.springfilter.builder.AbstractStep.StepWithResultImpl;
import com.infomedia.abacox.control.component.springfilter.definition.FilterOperators;
import com.infomedia.abacox.control.component.springfilter.language.NotOperator;
import com.infomedia.abacox.control.component.springfilter.parser.node.FilterNode;

public interface NotStep extends StepWithResult {

  default NotStepImpl not() {
    return new NotStepImpl(getOperators(),
        get().prefix(
            getOperators().getPrefixOperator(NotOperator.class)));
  }

  class NotStepImpl extends StepWithResultImpl implements LogicStep {

    NotStepImpl(FilterOperators operators,
        FilterNode result) {
      super(operators, result);
    }

  }

}
