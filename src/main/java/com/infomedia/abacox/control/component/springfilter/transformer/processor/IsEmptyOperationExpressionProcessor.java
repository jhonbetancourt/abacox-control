package com.infomedia.abacox.control.component.springfilter.transformer.processor;

import com.infomedia.abacox.control.component.springfilter.helper.IgnoreExists;
import com.infomedia.abacox.control.component.springfilter.language.IsEmptyOperator;
import com.infomedia.abacox.control.component.springfilter.parser.node.PostfixOperationNode;
import com.infomedia.abacox.control.component.springfilter.transformer.FilterExpressionTransformer;
import jakarta.persistence.criteria.Expression;
import org.springframework.stereotype.Component;

@IgnoreExists
@Component
public class IsEmptyOperationExpressionProcessor implements
    FilterPostfixOperationProcessor<FilterExpressionTransformer, Expression<?>> {

  protected final IsNotEmptyOperationExpressionProcessor isNotEmptyOperationExpressionProcessor;

  IsEmptyOperationExpressionProcessor(
      IsNotEmptyOperationExpressionProcessor isNotEmptyOperationExpressionProcessor) {
    this.isNotEmptyOperationExpressionProcessor = isNotEmptyOperationExpressionProcessor;
  }

  @Override

  public Class<FilterExpressionTransformer> getTransformerType() {
    return FilterExpressionTransformer.class;
  }

  @Override
  public Class<IsEmptyOperator> getDefinitionType() {
    return IsEmptyOperator.class;
  }

  @SuppressWarnings("unchecked")
  @Override
  public Expression<?> process(FilterExpressionTransformer transformer,
      PostfixOperationNode source) {
    transformer.registerTargetType(source, Boolean.class);
    return transformer.getCriteriaBuilder().not(
        (Expression<Boolean>) isNotEmptyOperationExpressionProcessor.process(transformer, source));
  }

}
