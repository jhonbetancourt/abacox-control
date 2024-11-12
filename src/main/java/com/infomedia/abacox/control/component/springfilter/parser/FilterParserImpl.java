package com.infomedia.abacox.control.component.springfilter.parser;

import com.infomedia.abacox.control.component.springfilter.definition.FilterOperators;
import com.infomedia.abacox.control.component.springfilter.parser.node.FilterNode;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

@Service
class FilterParserImpl implements FilterParser {

  @Autowired
  private AntlrParser antlrParser;

  private final FilterOperators operators;

  public FilterParserImpl(FilterOperators operators) {
    this.operators = operators;
  }

  @Override
  public FilterNode parse(String input, @Nullable ParseContext ctx) {

    AntlrFilterLexer lexer = new AntlrFilterLexer(
        CharStreams.fromString(input),
        operators);
    lexer.removeErrorListeners();
    lexer.addErrorListener(ThrowingErrorListener.INSTANCE);

    AntlrFilterParser parser = new AntlrFilterParser(
        new CommonTokenStream(lexer),
        operators);
    parser.removeErrorListeners();
    parser.addErrorListener(ThrowingErrorListener.INSTANCE);

    return antlrParser.parse(parser.filter(), ctx);

  }

}
