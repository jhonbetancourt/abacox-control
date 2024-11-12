package com.infomedia.abacox.control.component.springfilter.transformer.processor;

import com.infomedia.abacox.control.component.springfilter.definition.FilterDefinition;
import com.infomedia.abacox.control.component.springfilter.parser.node.FilterNode;
import com.infomedia.abacox.control.component.springfilter.transformer.FilterNodeTransformer;

public interface FilterNodeProcessor<Transformer extends FilterNodeTransformer<Target>, Definition extends FilterDefinition, Source extends FilterNode, Target> {

  Class<Transformer> getTransformerType();

  Class<? extends Definition> getDefinitionType();

  Target process(Transformer transformer, Source source);

}
