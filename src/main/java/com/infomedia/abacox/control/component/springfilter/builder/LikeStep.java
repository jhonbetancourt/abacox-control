package com.infomedia.abacox.control.component.springfilter.builder;

import com.infomedia.abacox.control.component.springfilter.builder.AbstractStep.StepWithResultImpl;
import com.infomedia.abacox.control.component.springfilter.definition.FilterOperators;
import com.infomedia.abacox.control.component.springfilter.language.LikeOperator;
import com.infomedia.abacox.control.component.springfilter.parser.node.FilterNode;

public interface LikeStep extends StepWithResult {

  default LikeStepImpl like(StepWithResult to) {
    return new LikeStepImpl(getOperators(),
        get().infix(getOperators().getInfixOperator(LikeOperator.class),
            to.get()));
  }

  class LikeStepImpl extends StepWithResultImpl implements LogicStep {

    LikeStepImpl(FilterOperators operators,
        FilterNode result) {
      super(operators, result);
    }

  }

}
