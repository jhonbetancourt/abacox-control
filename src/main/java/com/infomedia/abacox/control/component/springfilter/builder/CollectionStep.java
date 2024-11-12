package com.infomedia.abacox.control.component.springfilter.builder;


import com.infomedia.abacox.control.component.springfilter.builder.AbstractStep.StepWithResultImpl;
import com.infomedia.abacox.control.component.springfilter.definition.FilterOperators;
import com.infomedia.abacox.control.component.springfilter.parser.node.CollectionNode;
import com.infomedia.abacox.control.component.springfilter.parser.node.FilterNode;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public interface CollectionStep extends Step {

  default CollectionStepImpl collection(List<StepWithResult> items) {
    return new CollectionStepImpl(getOperators(),
        new CollectionNode(items.stream().map(StepWithResult::get).collect(Collectors.toList())));
  }

  default CollectionStepImpl collection(StepWithResult... arguments) {
    return collection(Arrays.asList(arguments));
  }

  class CollectionStepImpl extends StepWithResultImpl implements ComparisonStep {

    CollectionStepImpl(FilterOperators operators,
        FilterNode result) {
      super(operators, result);
    }

  }

}
