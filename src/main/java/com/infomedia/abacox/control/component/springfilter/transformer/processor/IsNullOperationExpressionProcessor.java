package com.infomedia.abacox.control.component.springfilter.transformer.processor;

import com.infomedia.abacox.control.component.springfilter.language.IsNullOperator;
import com.infomedia.abacox.control.component.springfilter.parser.node.PostfixOperationNode;
import com.infomedia.abacox.control.component.springfilter.transformer.FilterExpressionTransformer;
import jakarta.persistence.criteria.Expression;
import org.springframework.stereotype.Component;

@Component
public class IsNullOperationExpressionProcessor implements
    FilterPostfixOperationProcessor<FilterExpressionTransformer, Expression<?>> {

  @Override
  public Class<FilterExpressionTransformer> getTransformerType() {
    return FilterExpressionTransformer.class;
  }

  @Override
  public Class<IsNullOperator> getDefinitionType() {
    return IsNullOperator.class;
  }

  @Override
  public Expression<?> process(FilterExpressionTransformer transformer,
      PostfixOperationNode source) {
    transformer.registerTargetType(source, Boolean.class);
    return transformer.getCriteriaBuilder().isNull(transformer.transform(source.getLeft()));
  }

}
