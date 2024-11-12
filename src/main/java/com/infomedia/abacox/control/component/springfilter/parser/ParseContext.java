package com.infomedia.abacox.control.component.springfilter.parser;

import com.infomedia.abacox.control.component.springfilter.parser.node.FilterNode;
import java.util.function.UnaryOperator;

public interface ParseContext {

  default UnaryOperator<String> getFieldMapper() {
    return UnaryOperator.identity();
  }

  default UnaryOperator<FilterNode> getNodeMapper() {
    return UnaryOperator.identity();
  }

}
