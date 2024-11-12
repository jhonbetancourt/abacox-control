package com.infomedia.abacox.control.component.springfilter.builder;


import com.infomedia.abacox.control.component.springfilter.builder.AbstractStep.StepWithResultImpl;
import com.infomedia.abacox.control.component.springfilter.definition.FilterOperators;
import com.infomedia.abacox.control.component.springfilter.definition.FilterPlaceholder;
import com.infomedia.abacox.control.component.springfilter.parser.node.FilterNode;
import com.infomedia.abacox.control.component.springfilter.parser.node.PlaceholderNode;

public interface PlaceholderStep extends Step {

  default PlaceholderStepImpl placeholder(FilterPlaceholder placeholder) {
    return new PlaceholderStepImpl(getOperators(),
        new PlaceholderNode(placeholder));
  }

  class PlaceholderStepImpl extends StepWithResultImpl implements ComparisonStep, LogicStep {

    PlaceholderStepImpl(FilterOperators operators,
        FilterNode result) {
      super(operators, result);
    }

  }

}
