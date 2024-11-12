package com.infomedia.abacox.control.component.springfilter.transformer.processor;

import com.infomedia.abacox.control.component.springfilter.language.NotOperator;
import com.infomedia.abacox.control.component.springfilter.parser.node.PrefixOperationNode;
import com.infomedia.abacox.control.component.springfilter.transformer.FilterExpressionTransformer;
import jakarta.persistence.criteria.Expression;
import org.springframework.stereotype.Component;

@Component
public class NotOperationExpressionProcessor implements
    FilterPrefixOperationProcessor<FilterExpressionTransformer, Expression<?>> {

  @Override
  public Class<FilterExpressionTransformer> getTransformerType() {
    return FilterExpressionTransformer.class;
  }

  @Override
  public Class<NotOperator> getDefinitionType() {
    return NotOperator.class;
  }

  @SuppressWarnings("unchecked")
  @Override
  public Expression<?> process(FilterExpressionTransformer transformer,
      PrefixOperationNode source) {
    transformer.registerTargetType(source, Boolean.class);
    transformer.registerTargetType(source.getRight(), Boolean.class);
    return transformer.getCriteriaBuilder()
        .not((Expression<Boolean>) transformer.transform(source.getRight()));
  }

}
