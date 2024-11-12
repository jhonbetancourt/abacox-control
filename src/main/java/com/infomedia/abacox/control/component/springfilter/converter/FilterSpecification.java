package com.infomedia.abacox.control.component.springfilter.converter;

import com.infomedia.abacox.control.component.springfilter.parser.node.FilterNode;
import org.springframework.data.jpa.domain.Specification;

public interface FilterSpecification<T> extends Specification<T> {

  FilterNode getFilter();

}
