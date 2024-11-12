package com.infomedia.abacox.control.component.springfilter.converter;

import com.infomedia.abacox.control.component.springfilter.parser.node.FilterNode;
import org.springframework.core.convert.converter.GenericConverter;

public interface FilterSpecificationConverter extends GenericConverter {

  <T> FilterSpecification<T> convert(String query);

  <T> FilterSpecification<T> convert(FilterNode node);

  String convert(FilterSpecification<?> specification);

}
