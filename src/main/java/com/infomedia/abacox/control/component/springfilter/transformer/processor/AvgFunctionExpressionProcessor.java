package com.infomedia.abacox.control.component.springfilter.transformer.processor;

import com.infomedia.abacox.control.component.springfilter.helper.IgnoreExists;
import com.infomedia.abacox.control.component.springfilter.helper.RootContext;
import com.infomedia.abacox.control.component.springfilter.language.AvgFunction;
import com.infomedia.abacox.control.component.springfilter.parser.node.FunctionNode;
import com.infomedia.abacox.control.component.springfilter.transformer.FilterExpressionTransformer;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.stereotype.Component;

@IgnoreExists
@Component
public class AvgFunctionExpressionProcessor implements
    FilterFunctionProcessor<FilterExpressionTransformer, Expression<?>> {

  @Override
  public Class<FilterExpressionTransformer> getTransformerType() {
    return FilterExpressionTransformer.class;
  }

  @Override
  public Class<AvgFunction> getDefinitionType() {
    return AvgFunction.class;
  }

  @SuppressWarnings("unchecked")
  @Override
  public Expression<?> process(FilterExpressionTransformer transformer,
      FunctionNode source) {

    Subquery<Double> subquery = transformer.getCriteriaQuery().subquery(Double.class);

    Root<?> root = subquery.correlate(transformer.getRoot());

    transformer.registerRootContext(source, new RootContext(root));

    subquery.select(
        transformer.getCriteriaBuilder().avg((Expression<Number>) transformer.transform(
            source.getArgument(0))));

    return subquery;

  }

}
