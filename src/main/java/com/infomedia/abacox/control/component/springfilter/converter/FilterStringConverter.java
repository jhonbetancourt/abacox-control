package com.infomedia.abacox.control.component.springfilter.converter;

import com.infomedia.abacox.control.component.springfilter.parser.ParseContext;
import com.infomedia.abacox.control.component.springfilter.parser.node.FilterNode;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.lang.Nullable;

public interface FilterStringConverter extends GenericConverter {

  FilterNode convert(String source, @Nullable ParseContext ctx);

  default FilterNode convert(String source) {
    return convert(source, null);
  }

  String convert(FilterNode source);

}
