package com.infomedia.abacox.control.component.springfilter.builder;


import com.infomedia.abacox.control.component.springfilter.builder.AbstractStep.StepWithResultImpl;
import com.infomedia.abacox.control.component.springfilter.definition.FilterOperators;
import com.infomedia.abacox.control.component.springfilter.parser.node.FieldNode;
import com.infomedia.abacox.control.component.springfilter.parser.node.FilterNode;

public interface FieldStep extends Step {

  default FieldStepImpl field(String name) {
    return new FieldStepImpl(getOperators(), new FieldNode(name));
  }

  class FieldStepImpl extends StepWithResultImpl implements ComparisonStep, LogicStep {

    FieldStepImpl(FilterOperators operators,
        FilterNode result) {
      super(operators, result);
    }

  }

}
