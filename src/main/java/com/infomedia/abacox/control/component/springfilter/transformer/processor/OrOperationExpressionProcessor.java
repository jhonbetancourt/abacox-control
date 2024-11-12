package com.infomedia.abacox.control.component.springfilter.transformer.processor;

import com.infomedia.abacox.control.component.springfilter.language.OrOperator;
import com.infomedia.abacox.control.component.springfilter.parser.node.InfixOperationNode;
import com.infomedia.abacox.control.component.springfilter.transformer.FilterExpressionTransformer;
import jakarta.persistence.criteria.Expression;
import org.springframework.stereotype.Component;

@Component
public class OrOperationExpressionProcessor implements
    FilterInfixOperationProcessor<FilterExpressionTransformer, Expression<?>> {

  @Override
  public Class<FilterExpressionTransformer> getTransformerType() {
    return FilterExpressionTransformer.class;
  }

  @Override
  public Class<OrOperator> getDefinitionType() {
    return OrOperator.class;
  }

  @SuppressWarnings("unchecked")

  @Override
  public Expression<?> process(FilterExpressionTransformer transformer, InfixOperationNode source) {
    transformer.registerTargetType(source, Boolean.class);
    transformer.registerTargetType(source.getLeft(), Boolean.class);
    transformer.registerTargetType(source.getRight(), Boolean.class);
    return transformer.getCriteriaBuilder()
        .or((Expression<Boolean>) transformer.transform(source.getLeft()),
            (Expression<Boolean>) transformer.transform(source.getRight()));
  }

}
