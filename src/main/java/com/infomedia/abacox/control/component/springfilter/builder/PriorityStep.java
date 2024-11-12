package com.infomedia.abacox.control.component.springfilter.builder;


import com.infomedia.abacox.control.component.springfilter.builder.AbstractStep.StepWithResultImpl;
import com.infomedia.abacox.control.component.springfilter.definition.FilterOperators;
import com.infomedia.abacox.control.component.springfilter.parser.node.FilterNode;
import com.infomedia.abacox.control.component.springfilter.parser.node.PriorityNode;

public interface PriorityStep extends Step {

  default PriorityStepImpl priority(StepWithResult value) {
    return new PriorityStepImpl(getOperators(),
        new PriorityNode(value.get()));
  }

  class PriorityStepImpl extends StepWithResultImpl implements LogicStep {

    PriorityStepImpl(FilterOperators operators,
        FilterNode result) {
      super(operators, result);
    }

  }

}
