package com.infomedia.abacox.control.component.springfilter.definition;

public abstract class FilterPlaceholder implements FilterDefinition {

  private final String name;

  protected FilterPlaceholder(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

}
