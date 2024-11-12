package com.infomedia.abacox.control.component.springfilter.parser.node;

import com.infomedia.abacox.control.component.springfilter.definition.FilterInfixOperator;
import com.infomedia.abacox.control.component.springfilter.definition.FilterPostfixOperator;
import com.infomedia.abacox.control.component.springfilter.definition.FilterPrefixOperator;
import java.util.List;
import org.springframework.lang.Nullable;

public abstract class FilterNode {

  private Object payload;

  public FilterNode prefix(FilterPrefixOperator operator) {
    return new PrefixOperationNode(operator, this);
  }

  public FilterNode infix(FilterInfixOperator operator, FilterNode right) {
    return new InfixOperationNode(this, operator, right);
  }

  public FilterNode postfix(FilterPostfixOperator operator) {
    return new PostfixOperationNode(this, operator);
  }

  @Nullable
  public Object getPayload() {
    return payload;
  }

  public void setPayload(@Nullable Object payload) {
    this.payload = payload;
  }

  public abstract List<FilterNode> getChildren();

}
