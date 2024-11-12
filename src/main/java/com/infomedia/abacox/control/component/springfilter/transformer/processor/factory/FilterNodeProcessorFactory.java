package com.infomedia.abacox.control.component.springfilter.transformer.processor.factory;

import com.infomedia.abacox.control.component.springfilter.definition.FilterDefinition;
import com.infomedia.abacox.control.component.springfilter.parser.node.FilterNode;
import com.infomedia.abacox.control.component.springfilter.transformer.FilterNodeTransformer;
import com.infomedia.abacox.control.component.springfilter.transformer.processor.FilterNodeProcessor;

public interface FilterNodeProcessorFactory<Source extends FilterNode> {

  <Target> Target process(FilterNodeTransformer<Target> transformer, Source source);

  <Transformer extends FilterNodeTransformer<Target>, Definition extends FilterDefinition, Target> FilterNodeProcessor<Transformer, Definition, Source, Target> getProcessor(
      Class<Transformer> transformerType, Class<Definition> definitionType);

}
