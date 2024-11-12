package com.infomedia.abacox.control.component.springfilter.transformer.processor;

import com.infomedia.abacox.control.component.springfilter.language.TodayFunction;
import com.infomedia.abacox.control.component.springfilter.parser.node.FunctionNode;
import com.infomedia.abacox.control.component.springfilter.transformer.FilterExpressionTransformer;
import jakarta.persistence.criteria.Expression;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.stereotype.Component;

@Component
public class TodayFunctionExpressionProcessor implements
    FilterFunctionProcessor<FilterExpressionTransformer, Expression<?>> {

  @Override
  public Class<FilterExpressionTransformer> getTransformerType() {
    return FilterExpressionTransformer.class;
  }

  @Override
  public Class<TodayFunction> getDefinitionType() {
    return TodayFunction.class;
  }

  @Override
  public Expression<?> process(FilterExpressionTransformer transformer, FunctionNode source) {
    transformer.registerTargetType(source, String.class);
    return transformer.getCriteriaBuilder()
        .literal(new SimpleDateFormat("EEEE").format(new Date()));
  }

}
