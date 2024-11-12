package com.infomedia.abacox.control.component.springfilter.transformer.processor.factory;

public interface FilterNodeProcessorFactories {

  FilterFunctionProcessorFactory getFunctionProcessorFactory();

  FilterPlaceholderProcessorFactory getPlaceholderProcessorFactory();

  FilterOperationProcessorFactory getOperationProcessorFactory();

}
