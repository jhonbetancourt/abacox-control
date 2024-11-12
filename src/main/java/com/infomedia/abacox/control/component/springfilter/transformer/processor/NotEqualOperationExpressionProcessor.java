package com.infomedia.abacox.control.component.springfilter.transformer.processor;

import com.infomedia.abacox.control.component.springfilter.language.NotEqualOperator;
import com.infomedia.abacox.control.component.springfilter.parser.node.InfixOperationNode;
import com.infomedia.abacox.control.component.springfilter.transformer.FilterExpressionTransformer;
import jakarta.persistence.criteria.Expression;
import org.springframework.stereotype.Component;

@Component
public class NotEqualOperationExpressionProcessor implements
    FilterInfixOperationProcessor<FilterExpressionTransformer, Expression<?>> {

  @Override
  public Class<FilterExpressionTransformer> getTransformerType() {
    return FilterExpressionTransformer.class;
  }

  @Override
  public Class<NotEqualOperator> getDefinitionType() {
    return NotEqualOperator.class;
  }

  @Override
  public Expression<?> process(FilterExpressionTransformer transformer, InfixOperationNode source) {
    transformer.registerTargetType(source, Boolean.class);
    Expression<?> left = transformer.transform(source.getLeft());
    transformer.registerTargetType(source.getRight(), left.getJavaType());
    Expression<?> right = transformer.transform(source.getRight());
    return transformer.getCriteriaBuilder().notEqual(left, right);
  }

}
