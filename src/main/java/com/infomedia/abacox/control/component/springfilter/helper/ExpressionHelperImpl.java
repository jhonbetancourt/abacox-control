package com.infomedia.abacox.control.component.springfilter.helper;

import com.infomedia.abacox.control.component.springfilter.definition.FilterDefinition;
import com.infomedia.abacox.control.component.springfilter.parser.node.CollectionNode;
import com.infomedia.abacox.control.component.springfilter.parser.node.FieldNode;
import com.infomedia.abacox.control.component.springfilter.parser.node.FilterNode;
import com.infomedia.abacox.control.component.springfilter.parser.node.FunctionNode;
import com.infomedia.abacox.control.component.springfilter.parser.node.InfixOperationNode;
import com.infomedia.abacox.control.component.springfilter.parser.node.InputNode;
import com.infomedia.abacox.control.component.springfilter.parser.node.OperationNode;
import com.infomedia.abacox.control.component.springfilter.parser.node.PlaceholderNode;
import com.infomedia.abacox.control.component.springfilter.parser.node.PostfixOperationNode;
import com.infomedia.abacox.control.component.springfilter.parser.node.PrefixOperationNode;
import com.infomedia.abacox.control.component.springfilter.parser.node.PriorityNode;
import com.infomedia.abacox.control.component.springfilter.transformer.FilterExpressionTransformer;
import com.infomedia.abacox.control.component.springfilter.transformer.processor.FilterNodeProcessor;
import com.infomedia.abacox.control.component.springfilter.transformer.processor.factory.FilterFunctionProcessorFactory;
import com.infomedia.abacox.control.component.springfilter.transformer.processor.factory.FilterOperationProcessorFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.MapJoin;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import jakarta.persistence.metamodel.Bindable.BindableType;
import jakarta.persistence.metamodel.EntityType;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class ExpressionHelperImpl implements PathExpressionHelper, ExistsExpressionHelper {

  protected final EntityManager entityManager;

  protected final Set<Class<? extends FilterDefinition>> ignoreExistsForDefinitions;

  protected final FilterFunctionProcessorFactory filterFunctionProcessorFactory;

  protected final FilterOperationProcessorFactory operationProcessorFactory;

  public ExpressionHelperImpl(EntityManager entityManager,
      @IgnoreExists Set<FilterNodeProcessor<?, ?, ?, ?>> ignoreExistsForProcessors,
      FilterFunctionProcessorFactory filterFunctionProcessorFactory,
      FilterOperationProcessorFactory operationProcessorFactory) {
    this.entityManager = entityManager;
    ignoreExistsForDefinitions = ignoreExistsForProcessors.stream().map(
            FilterNodeProcessor::getDefinitionType)
        .collect(
            Collectors.toSet());
    this.filterFunctionProcessorFactory = filterFunctionProcessorFactory;
    this.operationProcessorFactory = operationProcessorFactory;
  }

  @Override
  public Path<?> getPath(RootContext rootContext, String fieldPath) {

    if (rootContext.getPaths().containsKey(fieldPath)) {
      return rootContext.getPaths().get(fieldPath);
    }

    Path<?> path = rootContext.getRoot();

    String[] fields = fieldPath.split("\\.");

    String chain = null;

    for (String field : fields) {

      if (chain == null) {
        chain = field;
      } else {
        chain += "." + field;
      }

      Path<?> nextPath = null;

      if (path instanceof MapJoin) {
        if (field.equals("key") || field.equals("keys")) {
          nextPath = ((MapJoin<?, ?, ?>) path).key();
        } else if (field.equals("value") || field.equals("values")) {
          nextPath = ((MapJoin<?, ?, ?>) path).value();
        }
      }

      if (nextPath == null) {
        nextPath = path.get(field);
      }

      boolean shouldJoin =
          !(path instanceof MapJoin) && path instanceof From && shouldJoin(nextPath);

      if (shouldJoin) {
        if (!rootContext.getPaths().containsKey(chain)) {
          rootContext.getPaths().put(chain, ((From<?, ?>) path).join(field,
              nextPath.getModel().getBindableType() == BindableType.SINGULAR_ATTRIBUTE
                  ? JoinType.LEFT
                  : JoinType.INNER));
        }
        nextPath = rootContext.getPaths().get(chain);
      }

      path = nextPath;

      if (!rootContext.getPaths().containsKey(chain)) {
        rootContext.getPaths().put(chain, path);
      }

    }

    return path;

  }

  @Override
  public boolean requiresExists(FilterExpressionTransformer transformer, FilterNode node) {

    if (node instanceof FieldNode fieldNode) {

      Path<?> from = transformer.getRoot();
      Path<?> path;

      String[] fields = fieldNode.getName().split("\\.");

      StringBuilder chain = null;

      for (String field : fields) {

        path = from.get(field);

        if (chain == null) {
          chain = new StringBuilder(field);
        } else {
          chain.append(".").append(field);
        }

        boolean requireExists = path.getModel().getBindableType() == BindableType.PLURAL_ATTRIBUTE;

        if (requireExists) {
          return true;
        }

        from = path;

      }

      return false;

    }

    if (node instanceof InputNode) {
      return false;
    }

    if (node instanceof PriorityNode) {
      return requiresExists(transformer, ((PriorityNode) node).getNode());
    }

    if (node instanceof PlaceholderNode) {
      return false;
    }

    if (node instanceof FunctionNode functionNode) {

      if (ignoreExistsForDefinitions.contains(functionNode.getFunction().getClass())) {
        transformer.registerIgnoreExists(functionNode);
        return false;
      }

      if (filterFunctionProcessorFactory.getProcessor(transformer.getClass(),
          functionNode.getFunction()
              .getClass()) instanceof PossibleAggregatedExpression processor) {
        if (processor.isAggregated(functionNode)) {
          transformer.registerIgnoreExists(functionNode);
          return false;
        }
      }

      for (FilterNode argument : functionNode.getArguments()) {
        if (requiresExists(transformer, argument)) {
          return true;
        }
      }

      return false;

    }

    if (node instanceof CollectionNode collectionNode) {
      for (FilterNode items : collectionNode.getItems()) {
        if (requiresExists(transformer, items)) {
          return true;
        }
      }
      return false;
    }

    if (node instanceof OperationNode operationNode) {

      if (ignoreExistsForDefinitions.contains(operationNode.getOperator().getClass())) {
        transformer.registerIgnoreExists(operationNode);
        return false;
      }

      if (operationProcessorFactory.getProcessor(transformer.getClass(),
          operationNode.getOperator()
              .getClass()) instanceof PossibleAggregatedExpression processor) {
        if (processor.isAggregated(operationNode)) {
          transformer.registerIgnoreExists(operationNode);
          return false;
        }
      }

      if (node instanceof PrefixOperationNode) {
        return requiresExists(transformer, ((PrefixOperationNode) node).getRight());
      }

      if (node instanceof InfixOperationNode) {
        return requiresExists(transformer, ((InfixOperationNode) node).getLeft()) || requiresExists(
            transformer,
            ((InfixOperationNode) node).getRight());
      }

      if (node instanceof PostfixOperationNode) {
        return requiresExists(transformer, ((PostfixOperationNode) node).getLeft());
      }

    }

    throw new UnsupportedOperationException("Unsupported node " + node);

  }

  private boolean isEntityType(Class<?> klass) {
    for (EntityType<?> entityType : entityManager.getMetamodel().getEntities()) {
      Class<?> entityClass = entityType.getJavaType();
      if (entityClass.equals(klass)) {
        return true;
      }
    }
    return false;
  }

  private boolean shouldJoin(Path<?> path) {
    return path.getModel().getBindableType() == BindableType.PLURAL_ATTRIBUTE
        || (path.getModel().getBindableType() == BindableType.SINGULAR_ATTRIBUTE
        && isEntityType(path.getModel().getBindableJavaType()));
  }

  @Override
  @SuppressWarnings("unchecked")
  public Expression<?> wrapWithExists(FilterExpressionTransformer transformer, FilterNode node) {

    Subquery<Integer> subquery = transformer.getCriteriaQuery().subquery(Integer.class);

    Root<?> subroot = subquery.correlate(transformer.getRoot());

    transformer.registerRootContext(node, new RootContext(subroot));

    Expression<?> expression = transformer.transform(node);

    subquery.select(transformer.getCriteriaBuilder().literal(1));

    if (Boolean.class.equals(expression.getJavaType())) {
      subquery.where((Expression<Boolean>) expression);
    }

    return transformer.getCriteriaBuilder().exists(subquery);

  }

}
