package com.infomedia.abacox.control.component.springfilter.transformer.processor.factory;

import com.infomedia.abacox.control.component.springfilter.definition.FilterDefinition;
import com.infomedia.abacox.control.component.springfilter.parser.node.FilterNode;
import com.infomedia.abacox.control.component.springfilter.transformer.FilterNodeTransformer;
import com.infomedia.abacox.control.component.springfilter.transformer.processor.FilterNodeProcessor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
abstract class AbstractFilterNodeProcessorFactory<Source extends FilterNode, Processor extends FilterNodeProcessor<?, ?, ? extends Source, ?>> implements
    FilterNodeProcessorFactory<Source> {

  private final Map<Class<?>, Map<Class<?>, Processor>> processorMap;

  public AbstractFilterNodeProcessorFactory(
      List<Processor> processors) {
    processorMap = new HashMap<>();
    for (Processor processor : processors) {
      if (!processorMap.containsKey(processor.getTransformerType())) {
        processorMap.put(processor.getTransformerType(), new HashMap<>());
      }
      if (processorMap.get(processor.getTransformerType())
          .containsKey(processor.getDefinitionType())) {
        throw new IllegalStateException(
            "A processor for transformer " + processor.getTransformerType() + " and definition "
                + processor.getDefinitionType() + " is already present");
      }
      processorMap.get(processor.getTransformerType())
          .put(processor.getDefinitionType(), processor);
    }
  }

  public Map<Class<?>, Map<Class<?>, Processor>> getProcessorMap() {
    return processorMap;
  }

  @SuppressWarnings("unchecked")
  @Override
  public <Transformer extends FilterNodeTransformer<Target>, Definition extends FilterDefinition, Target> FilterNodeProcessor<Transformer, Definition, Source, Target> getProcessor(
      Class<Transformer> transformerType, Class<Definition> definitionType) {
    return (FilterNodeProcessor<Transformer, Definition, Source, Target>) getProcessorMap().get(
        transformerType).get(definitionType);
  }
  
}
