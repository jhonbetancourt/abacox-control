package com.infomedia.abacox.control.component.springfilter.transformer.processor.factory;

import com.infomedia.abacox.control.component.springfilter.parser.node.FunctionNode;
import com.infomedia.abacox.control.component.springfilter.transformer.FilterNodeTransformer;
import com.infomedia.abacox.control.component.springfilter.transformer.processor.FilterFunctionProcessor;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class FilterFunctionProcessorFactoryImpl extends
    AbstractFilterNodeProcessorFactory<FunctionNode, FilterFunctionProcessor<?, ?>> implements
    FilterFunctionProcessorFactory {

  public FilterFunctionProcessorFactoryImpl(List<FilterFunctionProcessor<?, ?>> processors) {
    super(processors);
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> T process(FilterNodeTransformer<T> transformer, FunctionNode source) {

    if (!getProcessorMap().containsKey(transformer.getClass()) || !getProcessorMap().get(
            transformer.getClass())
        .containsKey(source.getFunction().getClass())) {
      throw new UnsupportedOperationException(
          "No transformer from function " + source.getFunction().getClass()
              + " to " + transformer.getTargetType()
              + " found");
    }

    return ((FilterFunctionProcessor<FilterNodeTransformer<T>, T>) getProcessorMap().get(
            transformer.getClass())
        .get(source.getFunction().getClass())).process(transformer,
        source);

  }

}
