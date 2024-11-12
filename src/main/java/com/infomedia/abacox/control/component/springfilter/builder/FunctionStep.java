package com.infomedia.abacox.control.component.springfilter.builder;


import com.infomedia.abacox.control.component.springfilter.builder.AbstractStep.StepWithResultImpl;
import com.infomedia.abacox.control.component.springfilter.definition.FilterFunction;
import com.infomedia.abacox.control.component.springfilter.definition.FilterOperators;
import com.infomedia.abacox.control.component.springfilter.parser.node.FilterNode;
import com.infomedia.abacox.control.component.springfilter.parser.node.FunctionNode;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public interface FunctionStep extends Step {

  default FunctionStepImpl function(FilterFunction function, List<StepWithResult> arguments) {
    return new FunctionStepImpl(getOperators(),
        new FunctionNode(function,
            arguments.stream().map(StepWithResult::get).collect(Collectors.toList())));
  }

  default FunctionStepImpl function(FilterFunction function, StepWithResult... arguments) {
    return function(function, Arrays.asList(arguments));
  }

  class FunctionStepImpl extends StepWithResultImpl implements ComparisonStep, LogicStep {

    FunctionStepImpl(FilterOperators operators,
        FilterNode result) {
      super(operators, result);
    }

  }

}
