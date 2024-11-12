package com.infomedia.abacox.control.component.springfilter.helper;

import jakarta.persistence.criteria.Path;

public interface PathExpressionHelper {

  Path<?> getPath(RootContext rootContext, String fieldPath);

}
