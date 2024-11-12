package com.infomedia.abacox.control.component.springfilter.transformer.processor;

import com.infomedia.abacox.control.component.springfilter.transformer.FilterNodeTransformer;
import com.infomedia.abacox.control.component.springfilter.definition.FilterFunction;
import com.infomedia.abacox.control.component.springfilter.parser.node.FunctionNode;

public interface FilterFunctionProcessor<Transformer extends FilterNodeTransformer<Target>, Target> extends
    FilterNodeProcessor<Transformer, FilterFunction, FunctionNode, Target> {

}
