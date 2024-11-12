package com.infomedia.abacox.control.component.springfilter.transformer.processor;

import com.infomedia.abacox.control.component.springfilter.transformer.FilterNodeTransformer;
import com.infomedia.abacox.control.component.springfilter.definition.FilterPrefixOperator;
import com.infomedia.abacox.control.component.springfilter.parser.node.PrefixOperationNode;

public interface FilterPrefixOperationProcessor<Transformer extends FilterNodeTransformer<Target>, Target> extends
    FilterOperationProcessor<Transformer, FilterPrefixOperator, PrefixOperationNode, Target> {

}
