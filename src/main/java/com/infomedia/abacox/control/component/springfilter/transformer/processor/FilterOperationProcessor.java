package com.infomedia.abacox.control.component.springfilter.transformer.processor;

import com.infomedia.abacox.control.component.springfilter.transformer.FilterNodeTransformer;
import com.infomedia.abacox.control.component.springfilter.definition.FilterOperator;
import com.infomedia.abacox.control.component.springfilter.parser.node.OperationNode;

public interface FilterOperationProcessor<Transformer extends FilterNodeTransformer<Target>, Definition extends FilterOperator, Source extends OperationNode, Target> extends
    FilterNodeProcessor<Transformer, Definition, Source, Target> {

}
