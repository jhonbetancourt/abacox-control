package com.infomedia.abacox.control.component.springfilter.transformer.processor;

import com.infomedia.abacox.control.component.springfilter.helper.IgnoreExists;
import com.infomedia.abacox.control.component.springfilter.helper.RootContext;
import com.infomedia.abacox.control.component.springfilter.language.SizeFunction;
import com.infomedia.abacox.control.component.springfilter.parser.node.FunctionNode;
import com.infomedia.abacox.control.component.springfilter.transformer.FilterExpressionTransformer;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.stereotype.Component;

@IgnoreExists
@Component
public class SizeFunctionExpressionProcessor implements
    FilterFunctionProcessor<FilterExpressionTransformer, Expression<?>> {

  @Override
  public Class<FilterExpressionTransformer> getTransformerType() {
    return FilterExpressionTransformer.class;
  }

  @Override
  public Class<SizeFunction> getDefinitionType() {
    return SizeFunction.class;
  }

  @SuppressWarnings("unchecked")
  @Override
  public Expression<?> process(FilterExpressionTransformer transformer,
      FunctionNode source) {

    Subquery<Long> subquery = transformer.getCriteriaQuery().subquery(Long.class);

    Root<?> root = subquery.correlate(transformer.getRoot());

    transformer.registerRootContext(source, new RootContext(root));

    subquery.select(
        transformer.getCriteriaBuilder().count(transformer.getCriteriaBuilder().literal(1)));

    Expression<?> expression = transformer.transform(
        source.getArgument(0));

    if (expression.getJavaType().equals(Boolean.class)) {
      subquery.where((Expression<Boolean>) expression);
    }

    return subquery;

  }

}
