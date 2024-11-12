package com.infomedia.abacox.control.component.springfilter.transformer.processor;

import com.infomedia.abacox.control.component.springfilter.language.SubstringFunction;
import com.infomedia.abacox.control.component.springfilter.parser.node.FunctionNode;
import com.infomedia.abacox.control.component.springfilter.transformer.FilterExpressionTransformer;
import jakarta.persistence.criteria.Expression;
import org.springframework.stereotype.Component;

@Component
public class SubstringFunctionExpressionProcessor implements
    FilterFunctionProcessor<FilterExpressionTransformer, Expression<?>> {

  @Override
  public Class<FilterExpressionTransformer> getTransformerType() {
    return FilterExpressionTransformer.class;
  }

  @Override
  public Class<SubstringFunction> getDefinitionType() {
    return SubstringFunction.class;
  }

  @SuppressWarnings("unchecked")
  @Override
  public Expression<?> process(FilterExpressionTransformer transformer,
      FunctionNode source) {

    transformer.registerTargetType(source.getArgument(0), String.class);
    transformer.registerTargetType(source.getArgument(1), Integer.class);

    if (source.getArguments().size() >= 2) {
      transformer.registerTargetType(source.getArgument(2), Integer.class);
      return transformer.getCriteriaBuilder()
          .substring((Expression<String>) transformer.transform(source.getArgument(0)),
              (Expression<Integer>) transformer.transform(source.getArgument(1)),
              (Expression<Integer>) transformer.transform(source.getArgument(2)));
    }

    return transformer.getCriteriaBuilder()
        .substring((Expression<String>) transformer.transform(source.getArgument(0)),
            (Expression<Integer>) transformer.transform(source.getArgument(1)));

  }

}
