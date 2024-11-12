package com.infomedia.abacox.control.component.springfilter.transformer.processor;

import com.infomedia.abacox.control.component.springfilter.helper.ExistsExpressionHelper;
import com.infomedia.abacox.control.component.springfilter.helper.IgnoreExists;
import com.infomedia.abacox.control.component.springfilter.language.IsNotEmptyOperator;
import com.infomedia.abacox.control.component.springfilter.parser.node.PostfixOperationNode;
import com.infomedia.abacox.control.component.springfilter.transformer.FilterExpressionTransformer;
import jakarta.persistence.criteria.Expression;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@IgnoreExists
@Component
public class IsNotEmptyOperationExpressionProcessor implements
    FilterPostfixOperationProcessor<FilterExpressionTransformer, Expression<?>> {

  protected final ExistsExpressionHelper existsExpressionHelper;

  IsNotEmptyOperationExpressionProcessor(@Lazy ExistsExpressionHelper existsExpressionHelper) {
    this.existsExpressionHelper = existsExpressionHelper;
  }

  @Override

  public Class<FilterExpressionTransformer> getTransformerType() {
    return FilterExpressionTransformer.class;
  }

  @Override
  public Class<IsNotEmptyOperator> getDefinitionType() {
    return IsNotEmptyOperator.class;
  }

  @Override
  public Expression<?> process(FilterExpressionTransformer transformer,
      PostfixOperationNode source) {
    transformer.registerTargetType(source, Boolean.class);
    return existsExpressionHelper.wrapWithExists(transformer, source.getLeft()
    );
  }

}
