package com.infomedia.abacox.control.component.springfilter.transformer.processor;

import com.infomedia.abacox.control.component.springfilter.language.ConcatFunction;
import com.infomedia.abacox.control.component.springfilter.parser.node.FilterNode;
import com.infomedia.abacox.control.component.springfilter.parser.node.FunctionNode;
import com.infomedia.abacox.control.component.springfilter.transformer.FilterExpressionTransformer;
import jakarta.persistence.criteria.Expression;
import org.springframework.stereotype.Component;

@Component
public class ConcatFunctionExpressionProcessor implements
    FilterFunctionProcessor<FilterExpressionTransformer, Expression<?>> {

  @Override
  public Class<FilterExpressionTransformer> getTransformerType() {
    return FilterExpressionTransformer.class;
  }

  @Override
  public Class<ConcatFunction> getDefinitionType() {
    return ConcatFunction.class;
  }

  @SuppressWarnings("unchecked")
  @Override
  public Expression<?> process(FilterExpressionTransformer transformer,
      FunctionNode source) {

    if (source.getArguments().isEmpty()) {
      return transformer.getCriteriaBuilder().literal("");
    }

    for (FilterNode argument : source.getArguments()) {
      transformer.registerTargetType(argument, String.class);
    }

    Expression<String> result = (Expression<String>) transformer.transform(source.getArgument(0));

    for (int i = 1; i < source.getArguments().size(); i++) {
      result = transformer.getCriteriaBuilder()
          .concat(result, (Expression<String>) transformer.transform(source.getArgument(i)));
    }

    return result;

  }

}
