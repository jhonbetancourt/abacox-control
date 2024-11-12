package com.infomedia.abacox.control.component.springfilter.transformer.processor;

import com.infomedia.abacox.control.component.springfilter.language.EqualOperator;
import com.infomedia.abacox.control.component.springfilter.parser.node.InfixOperationNode;
import com.infomedia.abacox.control.component.springfilter.transformer.FilterExpressionTransformer;
import jakarta.persistence.criteria.Expression;
import org.springframework.stereotype.Component;

@Component
public class EqualOperationExpressionProcessor implements
    FilterInfixOperationProcessor<FilterExpressionTransformer, Expression<?>> {

  @Override
  public Class<FilterExpressionTransformer> getTransformerType() {
    return FilterExpressionTransformer.class;
  }

  @Override
  public Class<EqualOperator> getDefinitionType() {
    return EqualOperator.class;
  }

  @Override
  public Expression<?> process(FilterExpressionTransformer transformer, InfixOperationNode source) {
    transformer.registerTargetType(source, Boolean.class);
    Expression<?> left = transformer.transform(source.getLeft());
    transformer.registerTargetType(source.getRight(), left.getJavaType());
    Expression<?> right = transformer.transform(source.getRight());
    return transformer.getCriteriaBuilder().equal(left, right);
  }

}
