package com.infomedia.abacox.control.component.springfilter.transformer.processor;

import com.infomedia.abacox.control.component.springfilter.language.HelloWorldPlaceholder;
import com.infomedia.abacox.control.component.springfilter.parser.node.PlaceholderNode;
import com.infomedia.abacox.control.component.springfilter.transformer.FilterExpressionTransformer;
import jakarta.persistence.criteria.Expression;
import org.springframework.stereotype.Component;

@Component
public class HelloWorldPlaceholderExpressionProcessor implements
    FilterPlaceholderProcessor<FilterExpressionTransformer, Expression<?>> {

  @Override
  public Class<FilterExpressionTransformer> getTransformerType() {
    return FilterExpressionTransformer.class;
  }

  @Override
  public Class<HelloWorldPlaceholder> getDefinitionType() {
    return HelloWorldPlaceholder.class;
  }

  @Override
  public Expression<?> process(FilterExpressionTransformer transformer, PlaceholderNode source) {
    transformer.registerTargetType(source, String.class);
    return transformer.getCriteriaBuilder().literal("Hello world!");
  }

}
