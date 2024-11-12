package com.infomedia.abacox.control.component.springfilter.transformer;

import com.infomedia.abacox.control.component.springfilter.parser.node.CollectionNode;
import com.infomedia.abacox.control.component.springfilter.parser.node.FieldNode;
import com.infomedia.abacox.control.component.springfilter.parser.node.FunctionNode;
import com.infomedia.abacox.control.component.springfilter.parser.node.InfixOperationNode;
import com.infomedia.abacox.control.component.springfilter.parser.node.InputNode;
import com.infomedia.abacox.control.component.springfilter.parser.node.PlaceholderNode;
import com.infomedia.abacox.control.component.springfilter.parser.node.PostfixOperationNode;
import com.infomedia.abacox.control.component.springfilter.parser.node.PrefixOperationNode;
import com.infomedia.abacox.control.component.springfilter.parser.node.PriorityNode;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.core.convert.ConversionService;

public class FilterStringTransformer implements FilterNodeTransformer<String> {

  protected final ConversionService conversionService;

  public FilterStringTransformer(ConversionService conversionService) {
    this.conversionService = conversionService;
  }

  @Override
  public Class<String> getTargetType() {
    return String.class;
  }

  @Override
  public String transformField(FieldNode node) {
    return node.getName();
  }

  @Override
  public String transformInput(InputNode node) {
    return "'" + Objects.requireNonNull(
        conversionService.convert(node.getValue(), String.class),
        "Could not convert `" + node.getValue() + "` to string").replace("'", "\\'") + "'";
  }

  @Override
  public String transformPriority(PriorityNode node) {
    return "(" + transform(node.getNode()) + ")";
  }

  @Override
  public String transformPlaceholder(PlaceholderNode node) {
    return "`" + node.getPlaceholder().getName() + "`";
  }

  @Override
  public String transformFunction(FunctionNode node) {
    return node.getFunction().getName() + "("
        + node.getArguments().stream().map(this::transform).collect(Collectors.joining(", ")) + ")";
  }

  @Override
  public String transformCollection(CollectionNode node) {
    return "[" + node.getItems().stream().map(this::transform).collect(Collectors.joining(", "))
        + "]";
  }

  @Override
  public String transformPrefixOperation(PrefixOperationNode node) {
    return node.getOperator().getToken() + " " + transform(node.getRight());
  }

  @Override
  public String transformInfixOperation(InfixOperationNode node) {
    return transform(node.getLeft()) + " " + node.getOperator().getToken() + " " + transform(
        node.getRight());
  }

  @Override
  public String transformPostfixOperation(PostfixOperationNode node) {
    return transform(node.getLeft()) + " " + node.getOperator().getToken();
  }

}
