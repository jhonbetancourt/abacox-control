package com.infomedia.abacox.control.component.springfilter.transformer.processor;

import com.infomedia.abacox.control.component.springfilter.language.LowerFunction;
import com.infomedia.abacox.control.component.springfilter.parser.node.FunctionNode;
import com.infomedia.abacox.control.component.springfilter.transformer.FilterExpressionTransformer;
import jakarta.persistence.criteria.Expression;
import org.springframework.stereotype.Component;

@Component
public class LowerFunctionExpressionProcessor implements
    FilterFunctionProcessor<FilterExpressionTransformer, Expression<?>> {

  @Override
  public Class<FilterExpressionTransformer> getTransformerType() {
    return FilterExpressionTransformer.class;
  }

  @Override
  public Class<LowerFunction> getDefinitionType() {
    return LowerFunction.class;
  }

  @SuppressWarnings("unchecked")
  @Override
  public Expression<?> process(FilterExpressionTransformer transformer,
      FunctionNode source) {
    transformer.registerTargetType(source.getArgument(1), String.class);
    return transformer.getCriteriaBuilder()
        .lower((Expression<String>) transformer.transform(source.getArgument(0)));
  }

}
