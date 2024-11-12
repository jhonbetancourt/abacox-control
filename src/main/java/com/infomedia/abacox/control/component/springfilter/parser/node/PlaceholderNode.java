package com.infomedia.abacox.control.component.springfilter.parser.node;

import com.infomedia.abacox.control.component.springfilter.definition.FilterPlaceholder;
import java.util.List;

public class PlaceholderNode extends FilterNode {

  private final FilterPlaceholder placeholder;

  public PlaceholderNode(FilterPlaceholder placeholder) {
    this.placeholder = placeholder;
  }

  public FilterPlaceholder getPlaceholder() {
    return placeholder;
  }

  @Override
  public List<FilterNode> getChildren() {
    return List.of();
  }
  
}
