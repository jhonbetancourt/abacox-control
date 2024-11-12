package com.infomedia.abacox.control.component.springfilter.transformer.processor;

import com.infomedia.abacox.control.component.springfilter.transformer.FilterNodeTransformer;
import com.infomedia.abacox.control.component.springfilter.definition.FilterPlaceholder;
import com.infomedia.abacox.control.component.springfilter.parser.node.PlaceholderNode;

public interface FilterPlaceholderProcessor<Transformer extends FilterNodeTransformer<Target>, Target> extends
    FilterNodeProcessor<Transformer, FilterPlaceholder, PlaceholderNode, Target> {

}
