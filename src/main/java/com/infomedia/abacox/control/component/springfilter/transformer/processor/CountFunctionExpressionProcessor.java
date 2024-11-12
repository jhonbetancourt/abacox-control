package com.infomedia.abacox.control.component.springfilter.transformer.processor;

import com.infomedia.abacox.control.component.springfilter.helper.IgnoreExists;
import com.infomedia.abacox.control.component.springfilter.language.CountFunction;
import com.infomedia.abacox.control.component.springfilter.parser.node.FunctionNode;
import com.infomedia.abacox.control.component.springfilter.transformer.FilterExpressionTransformer;
import jakarta.persistence.criteria.Expression;
import org.springframework.stereotype.Component;

@IgnoreExists
@Component
public class CountFunctionExpressionProcessor implements
    FilterFunctionProcessor<FilterExpressionTransformer, Expression<?>> {

  protected final SizeFunctionExpressionProcessor sizeFunctionExpressionProcessor;

  public CountFunctionExpressionProcessor(
      SizeFunctionExpressionProcessor sizeFunctionExpressionProcessor) {
    this.sizeFunctionExpressionProcessor = sizeFunctionExpressionProcessor;
  }

  @Override
  public Class<FilterExpressionTransformer> getTransformerType() {
    return FilterExpressionTransformer.class;
  }

  @Override
  public Class<CountFunction> getDefinitionType() {
    return CountFunction.class;
  }

  @Override
  public Expression<?> process(FilterExpressionTransformer transformer,
      FunctionNode source) {
    return sizeFunctionExpressionProcessor.process(transformer, source);
  }

}
