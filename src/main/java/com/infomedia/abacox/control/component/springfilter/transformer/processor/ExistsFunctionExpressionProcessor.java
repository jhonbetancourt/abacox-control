package com.infomedia.abacox.control.component.springfilter.transformer.processor;

import com.infomedia.abacox.control.component.springfilter.definition.FilterFunction;
import com.infomedia.abacox.control.component.springfilter.helper.ExistsExpressionHelper;
import com.infomedia.abacox.control.component.springfilter.helper.IgnoreExists;
import com.infomedia.abacox.control.component.springfilter.language.ExistsFunction;
import com.infomedia.abacox.control.component.springfilter.parser.node.FunctionNode;
import com.infomedia.abacox.control.component.springfilter.transformer.FilterExpressionTransformer;
import jakarta.persistence.criteria.Expression;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@IgnoreExists
@Component
public class ExistsFunctionExpressionProcessor implements
    FilterFunctionProcessor<FilterExpressionTransformer, Expression<?>> {

  protected final ExistsExpressionHelper existsExpressionHelper;

  ExistsFunctionExpressionProcessor(
      @Lazy ExistsExpressionHelper existsExpressionHelper) {
    this.existsExpressionHelper = existsExpressionHelper;
  }

  @Override
  public Class<FilterExpressionTransformer> getTransformerType() {
    return FilterExpressionTransformer.class;
  }

  @Override
  public Class<? extends FilterFunction> getDefinitionType() {
    return ExistsFunction.class;
  }

  @Override
  public Expression<?> process(FilterExpressionTransformer transformer, FunctionNode source) {
    transformer.registerTargetType(source, Boolean.class);
    return existsExpressionHelper.wrapWithExists(transformer,
        source.getArgument(0));
  }

}
