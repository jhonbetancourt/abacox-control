package com.infomedia.abacox.control.component.springfilter.transformer.processor.factory;

import com.infomedia.abacox.control.component.springfilter.parser.node.PlaceholderNode;
import com.infomedia.abacox.control.component.springfilter.transformer.FilterNodeTransformer;
import com.infomedia.abacox.control.component.springfilter.transformer.processor.FilterPlaceholderProcessor;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class FilterPlaceholderProcessorFactoryImpl extends
    AbstractFilterNodeProcessorFactory<PlaceholderNode, FilterPlaceholderProcessor<?, PlaceholderNode>> implements
    FilterPlaceholderProcessorFactory {

  public FilterPlaceholderProcessorFactoryImpl(
      List<FilterPlaceholderProcessor<?, PlaceholderNode>> processors) {
    super(processors);
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> T process(FilterNodeTransformer<T> transformer, PlaceholderNode source) {

    if (!getProcessorMap().containsKey(transformer.getClass()) || !getProcessorMap().get(
            transformer.getClass())
        .containsKey(source.getPlaceholder().getClass())) {
      throw new UnsupportedOperationException(
          "No transformer from placeholder " + source.getPlaceholder().getClass()
              + " to " + transformer.getTargetType()
              + " found");
    }

    return ((FilterPlaceholderProcessor<FilterNodeTransformer<T>, T>) getProcessorMap().get(
            transformer.getClass())
        .get(source.getPlaceholder().getClass())).process(transformer,
        source);

  }

}
