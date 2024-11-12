package com.infomedia.abacox.control.component.springfilter.helper;

import com.infomedia.abacox.control.component.springfilter.parser.node.FilterNode;
import com.infomedia.abacox.control.component.springfilter.transformer.FilterExpressionTransformer;
import jakarta.persistence.criteria.Expression;

public interface ExistsExpressionHelper {

  boolean requiresExists(FilterExpressionTransformer transformer, FilterNode node);

  Expression<?> wrapWithExists(FilterExpressionTransformer transformer, FilterNode node);

}
