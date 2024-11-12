package com.infomedia.abacox.control.component.springfilter.builder;


import com.infomedia.abacox.control.component.springfilter.definition.FilterOperators;
import com.infomedia.abacox.control.component.springfilter.parser.node.FilterNode;

public abstract class AbstractStep implements Step {

  private final FilterOperators operators;
  
  public AbstractStep(FilterOperators operators) {
    this.operators = operators;
  }

  @Override
  public FilterOperators getOperators() {
    return operators;
  }

  static class StepWithResultImpl extends AbstractStep implements StepWithResult {

    private final FilterNode result;

    public StepWithResultImpl(FilterOperators operators,
        FilterNode result) {
      super(operators);
      this.result = result;
    }

    public FilterNode get() {
      return result;
    }

  }

}
