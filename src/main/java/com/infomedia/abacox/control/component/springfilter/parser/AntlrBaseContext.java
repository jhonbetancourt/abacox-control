package com.infomedia.abacox.control.component.springfilter.parser;

import org.antlr.v4.runtime.ParserRuleContext;

abstract class AntlrBaseContext extends ParserRuleContext {

  public AntlrBaseContext() {
  }

  public AntlrBaseContext(ParserRuleContext parent, int invokingStateNumber) {
    super(parent, invokingStateNumber);
  }

}
