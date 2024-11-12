package com.infomedia.abacox.control.component.springfilter.transformer.processor;

import com.infomedia.abacox.control.component.springfilter.language.LengthFunction;
import com.infomedia.abacox.control.component.springfilter.parser.node.FunctionNode;
import com.infomedia.abacox.control.component.springfilter.transformer.FilterExpressionTransformer;
import jakarta.persistence.criteria.Expression;
import org.springframework.stereotype.Component;

@Component
public class LengthFunctionExpressionProcessor implements
    FilterFunctionProcessor<FilterExpressionTransformer, Expression<?>> {

  @Override
  public Class<FilterExpressionTransformer> getTransformerType() {
    return FilterExpressionTransformer.class;
  }

  @Override
  public Class<LengthFunction> getDefinitionType() {
    return LengthFunction.class;
  }

  @SuppressWarnings("unchecked")
  @Override
  public Expression<?> process(FilterExpressionTransformer transformer,
      FunctionNode source) {
    transformer.registerTargetType(source.getArgument(0), String.class);
    return transformer.getCriteriaBuilder()
        .length((Expression<String>) transformer.transform(source.getArgument(0)));
  }

}