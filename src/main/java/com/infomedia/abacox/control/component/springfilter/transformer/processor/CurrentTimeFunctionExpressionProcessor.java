package com.infomedia.abacox.control.component.springfilter.transformer.processor;

import com.infomedia.abacox.control.component.springfilter.language.CurrentTimeFunction;
import com.infomedia.abacox.control.component.springfilter.parser.node.FunctionNode;
import com.infomedia.abacox.control.component.springfilter.transformer.FilterExpressionTransformer;
import jakarta.persistence.criteria.Expression;
import org.springframework.stereotype.Component;

@Component
public class CurrentTimeFunctionExpressionProcessor implements
    FilterFunctionProcessor<FilterExpressionTransformer, Expression<?>> {

  @Override
  public Class<FilterExpressionTransformer> getTransformerType() {
    return FilterExpressionTransformer.class;
  }

  @Override
  public Class<CurrentTimeFunction> getDefinitionType() {
    return CurrentTimeFunction.class;
  }

  @Override
  public Expression<?> process(FilterExpressionTransformer transformer,
      FunctionNode source) {
    return transformer.getCriteriaBuilder().currentTime();
  }

}
