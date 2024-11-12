package com.infomedia.abacox.control.component.springfilter.transformer.processor;

import com.infomedia.abacox.control.component.springfilter.transformer.FilterNodeTransformer;
import com.infomedia.abacox.control.component.springfilter.definition.FilterPostfixOperator;
import com.infomedia.abacox.control.component.springfilter.parser.node.PostfixOperationNode;

public interface FilterPostfixOperationProcessor<Transformer extends FilterNodeTransformer<Target>, Target> extends
    FilterOperationProcessor<Transformer, FilterPostfixOperator, PostfixOperationNode, Target> {

}
