package com.infomedia.abacox.control.component.springfilter.transformer.processor;

import com.infomedia.abacox.control.component.springfilter.language.ModFunction;
import com.infomedia.abacox.control.component.springfilter.parser.node.FunctionNode;
import com.infomedia.abacox.control.component.springfilter.transformer.FilterExpressionTransformer;
import jakarta.persistence.criteria.Expression;
import org.springframework.stereotype.Component;

@Component
public class ModFunctionExpressionProcessor implements
    FilterFunctionProcessor<FilterExpressionTransformer, Expression<?>> {

  @Override
  public Class<FilterExpressionTransformer> getTransformerType() {
    return FilterExpressionTransformer.class;
  }

  @Override
  public Class<ModFunction> getDefinitionType() {
    return ModFunction.class;
  }

  @SuppressWarnings("unchecked")
  @Override
  public Expression<?> process(FilterExpressionTransformer transformer,
      FunctionNode source) {
    transformer.registerTargetType(source.getArgument(0), Integer.class);
    transformer.registerTargetType(source.getArgument(1), Integer.class);
    return transformer.getCriteriaBuilder()
        .mod((Expression<Integer>) transformer.transform(source.getArgument(0)),
            (Expression<Integer>) transformer.transform(source.getArgument(1)));
  }

}
