package com.infomedia.abacox.control.component.springfilter.builder;


import com.infomedia.abacox.control.component.springfilter.builder.AbstractStep.StepWithResultImpl;
import com.infomedia.abacox.control.component.springfilter.definition.FilterOperators;
import com.infomedia.abacox.control.component.springfilter.parser.node.FilterNode;
import com.infomedia.abacox.control.component.springfilter.parser.node.InputNode;

public interface InputStep extends Step {

  default InputStepImpl input(Object value) {
    return new InputStepImpl(getOperators(), new InputNode(value));
  }

  class InputStepImpl extends StepWithResultImpl implements ComparisonStep {

    InputStepImpl(FilterOperators operators,
        FilterNode result) {
      super(operators, result);
    }

  }

}
