package com.infomedia.abacox.control.component.springfilter.transformer.processor;

import com.infomedia.abacox.control.component.springfilter.transformer.FilterNodeTransformer;
import com.infomedia.abacox.control.component.springfilter.definition.FilterInfixOperator;
import com.infomedia.abacox.control.component.springfilter.parser.node.InfixOperationNode;

public interface FilterInfixOperationProcessor<Transformer extends FilterNodeTransformer<Target>, Target> extends
    FilterOperationProcessor<Transformer, FilterInfixOperator, InfixOperationNode, Target> {

}
