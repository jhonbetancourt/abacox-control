package com.infomedia.abacox.control.component.springfilter.transformer.processor;

import com.infomedia.abacox.control.component.springfilter.language.CurrentDateFunction;
import com.infomedia.abacox.control.component.springfilter.parser.node.FunctionNode;
import com.infomedia.abacox.control.component.springfilter.transformer.FilterExpressionTransformer;
import jakarta.persistence.criteria.Expression;
import org.springframework.stereotype.Component;

@Component
public class CurrentDateFunctionExpressionProcessor implements
    FilterFunctionProcessor<FilterExpressionTransformer, Expression<?>> {

  @Override
  public Class<FilterExpressionTransformer> getTransformerType() {
    return FilterExpressionTransformer.class;
  }

  @Override
  public Class<CurrentDateFunction> getDefinitionType() {
    return CurrentDateFunction.class;
  }

  @Override
  public Expression<?> process(FilterExpressionTransformer transformer,
      FunctionNode source) {
    return transformer.getCriteriaBuilder().currentDate();
  }

}
