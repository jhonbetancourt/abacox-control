package com.infomedia.abacox.control.component.springfilter.transformer.processor;

import com.infomedia.abacox.control.component.springfilter.helper.PossibleAggregatedExpression;
import com.infomedia.abacox.control.component.springfilter.helper.RootContext;
import com.infomedia.abacox.control.component.springfilter.language.SumFunction;
import com.infomedia.abacox.control.component.springfilter.parser.node.FunctionNode;
import com.infomedia.abacox.control.component.springfilter.transformer.FilterExpressionTransformer;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.stereotype.Component;

@Component
public class SumFunctionExpressionProcessor implements
    FilterFunctionProcessor<FilterExpressionTransformer, Expression<?>>,
    PossibleAggregatedExpression {

  @Override
  public Class<FilterExpressionTransformer> getTransformerType() {
    return FilterExpressionTransformer.class;
  }

  @Override
  public Class<SumFunction> getDefinitionType() {
    return SumFunction.class;
  }

  @SuppressWarnings("unchecked")
  @Override
  public Expression<?> process(FilterExpressionTransformer transformer,
      FunctionNode source) {

    transformer.registerTargetType(source.getArgument(0), Number.class);

    if (source.getArguments().size() > 1) {
      transformer.registerTargetType(source.getArgument(1), Number.class);
      return transformer.getCriteriaBuilder()
          .sum((Expression<Number>) transformer.transform(source.getArgument(0)),
              (Expression<Number>) transformer.transform(source.getArgument(1)));
    }

    Subquery<Number> subquery = transformer.getCriteriaQuery().subquery(Number.class);

    Root<?> root = subquery.correlate(transformer.getRoot());

    transformer.registerRootContext(source, new RootContext(root));

    subquery.select(
        transformer.getCriteriaBuilder().sum((Expression<Number>) transformer.transform(
            source.getArgument(0))));

    return subquery;

  }

  @Override
  public boolean isAggregated(FunctionNode functionNode) {
    return functionNode.getArguments().size() == 1;
  }

}
