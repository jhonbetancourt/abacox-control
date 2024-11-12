package com.infomedia.abacox.control.component.springfilter.parser;

import com.infomedia.abacox.control.component.springfilter.definition.FilterOperators;
import org.antlr.v4.runtime.FailedPredicateException;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.RuntimeMetaData;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.Vocabulary;
import org.antlr.v4.runtime.VocabularyImpl;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.ParserATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.tree.TerminalNode;
import java.util.ArrayList;
import java.util.List;

class AntlrFilterParser extends Parser {
   protected static final DFA[] _decisionToDFA;
   protected static final PredictionContextCache _sharedContextCache;
   public static final int PREFIX_OPERATOR = 1;
   public static final int INFIX_OPERATOR = 2;
   public static final int POSTFIX_OPERATOR = 3;
   public static final int TRUE = 4;
   public static final int FALSE = 5;
   public static final int DOT = 6;
   public static final int COMMA = 7;
   public static final int LPAREN = 8;
   public static final int RPAREN = 9;
   public static final int LBRACK = 10;
   public static final int RBRACK = 11;
   public static final int BTICK = 12;
   public static final int ID = 13;
   public static final int NUMBER = 14;
   public static final int STRING = 15;
   public static final int WS = 16;
   public static final int SYMBOL = 17;
   public static final int RULE_filter = 0;
   public static final int RULE_expression = 1;
   public static final int RULE_atom = 2;
   public static final String[] ruleNames;
   private static final String[] _LITERAL_NAMES;
   private static final String[] _SYMBOLIC_NAMES;
   public static final Vocabulary VOCABULARY;
   /** @deprecated */
   @Deprecated
   public static final String[] tokenNames;
   private FilterOperators operators;
   public static final String _serializedATN = "\u0004\u0001\u0011B\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002\u0002\u0007\u0002\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0005\u0001\u0010\b\u0001\n\u0001\f\u0001\u0013\t\u0001\u0001\u0002\u0001\u0002\u0001\u0002\u0005\u0002\u0018\b\u0002\n\u0002\f\u0002\u001b\t\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0005\u0002+\b\u0002\n\u0002\f\u0002.\t\u0002\u0003\u00020\b\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0005\u00028\b\u0002\n\u0002\f\u0002;\t\u0002\u0003\u0002=\b\u0002\u0001\u0002\u0003\u0002@\b\u0002\u0001\u0002\u0000\u0000\u0003\u0000\u0002\u0004\u0000\u0001\u0002\u0000\u0004\u0005\u000e\u000fK\u0000\u0006\u0001\u0000\u0000\u0000\u0002\t\u0001\u0000\u0000\u0000\u0004?\u0001\u0000\u0000\u0000\u0006\u0007\u0003\u0002\u0001\u0000\u0007\b\u0005\u0000\u0000\u0001\b\u0001\u0001\u0000\u0000\u0000\t\u0011\u0003\u0004\u0002\u0000\n\u000b\u0004\u0001\u0000\u0001\u000b\f\u0005\u0002\u0000\u0000\f\u0010\u0003\u0002\u0001\u0000\r\u000e\u0004\u0001\u0001\u0001\u000e\u0010\u0005\u0003\u0000\u0000\u000f\n\u0001\u0000\u0000\u0000\u000f\r\u0001\u0000\u0000\u0000\u0010\u0013\u0001\u0000\u0000\u0000\u0011\u000f\u0001\u0000\u0000\u0000\u0011\u0012\u0001\u0000\u0000\u0000\u0012\u0003\u0001\u0000\u0000\u0000\u0013\u0011\u0001\u0000\u0000\u0000\u0014\u0019\u0005\r\u0000\u0000\u0015\u0016\u0005\u0006\u0000\u0000\u0016\u0018\u0005\r\u0000\u0000\u0017\u0015\u0001\u0000\u0000\u0000\u0018\u001b\u0001\u0000\u0000\u0000\u0019\u0017\u0001\u0000\u0000\u0000\u0019\u001a\u0001\u0000\u0000\u0000\u001a@\u0001\u0000\u0000\u0000\u001b\u0019\u0001\u0000\u0000\u0000\u001c@\u0007\u0000\u0000\u0000\u001d\u001e\u0005\f\u0000\u0000\u001e\u001f\u0005\r\u0000\u0000\u001f@\u0005\f\u0000\u0000 !\u0005\b\u0000\u0000!\"\u0003\u0002\u0001\u0000\"#\u0005\t\u0000\u0000#@\u0001\u0000\u0000\u0000$%\u0005\u0001\u0000\u0000%@\u0003\u0002\u0001\u0000&/\u0005\n\u0000\u0000',\u0003\u0002\u0001\u0000()\u0005\u0007\u0000\u0000)+\u0003\u0002\u0001\u0000*(\u0001\u0000\u0000\u0000+.\u0001\u0000\u0000\u0000,*\u0001\u0000\u0000\u0000,-\u0001\u0000\u0000\u0000-0\u0001\u0000\u0000\u0000.,\u0001\u0000\u0000\u0000/'\u0001\u0000\u0000\u0000/0\u0001\u0000\u0000\u000001\u0001\u0000\u0000\u00001@\u0005\u000b\u0000\u000023\u0005\r\u0000\u00003<\u0005\b\u0000\u000049\u0003\u0002\u0001\u000056\u0005\u0007\u0000\u000068\u0003\u0002\u0001\u000075\u0001\u0000\u0000\u00008;\u0001\u0000\u0000\u000097\u0001\u0000\u0000\u00009:\u0001\u0000\u0000\u0000:=\u0001\u0000\u0000\u0000;9\u0001\u0000\u0000\u0000<4\u0001\u0000\u0000\u0000<=\u0001\u0000\u0000\u0000=>\u0001\u0000\u0000\u0000>@\u0005\t\u0000\u0000?\u0014\u0001\u0000\u0000\u0000?\u001c\u0001\u0000\u0000\u0000?\u001d\u0001\u0000\u0000\u0000? \u0001\u0000\u0000\u0000?$\u0001\u0000\u0000\u0000?&\u0001\u0000\u0000\u0000?2\u0001\u0000\u0000\u0000@\u0005\u0001\u0000\u0000\u0000\b\u000f\u0011\u0019,/9<?";
   public static final ATN _ATN;

   private static String[] makeRuleNames() {
      return new String[]{"filter", "expression", "atom"};
   }

   private static String[] makeLiteralNames() {
      return new String[]{null, null, null, null, null, null, "'.'", "','", "'('", "')'", "'['", "']'", "'`'"};
   }

   private static String[] makeSymbolicNames() {
      return new String[]{null, "PREFIX_OPERATOR", "INFIX_OPERATOR", "POSTFIX_OPERATOR", "TRUE", "FALSE", "DOT", "COMMA", "LPAREN", "RPAREN", "LBRACK", "RBRACK", "BTICK", "ID", "NUMBER", "STRING", "WS", "SYMBOL"};
   }

   /** @deprecated */
   @Deprecated
   public String[] getTokenNames() {
      return tokenNames;
   }

   public Vocabulary getVocabulary() {
      return VOCABULARY;
   }

   public String getGrammarFileName() {
      return "AntlrFilter.g4";
   }

   public String[] getRuleNames() {
      return ruleNames;
   }

   public String getSerializedATN() {
      return "\u0004\u0001\u0011B\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002\u0002\u0007\u0002\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0005\u0001\u0010\b\u0001\n\u0001\f\u0001\u0013\t\u0001\u0001\u0002\u0001\u0002\u0001\u0002\u0005\u0002\u0018\b\u0002\n\u0002\f\u0002\u001b\t\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0005\u0002+\b\u0002\n\u0002\f\u0002.\t\u0002\u0003\u00020\b\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0005\u00028\b\u0002\n\u0002\f\u0002;\t\u0002\u0003\u0002=\b\u0002\u0001\u0002\u0003\u0002@\b\u0002\u0001\u0002\u0000\u0000\u0003\u0000\u0002\u0004\u0000\u0001\u0002\u0000\u0004\u0005\u000e\u000fK\u0000\u0006\u0001\u0000\u0000\u0000\u0002\t\u0001\u0000\u0000\u0000\u0004?\u0001\u0000\u0000\u0000\u0006\u0007\u0003\u0002\u0001\u0000\u0007\b\u0005\u0000\u0000\u0001\b\u0001\u0001\u0000\u0000\u0000\t\u0011\u0003\u0004\u0002\u0000\n\u000b\u0004\u0001\u0000\u0001\u000b\f\u0005\u0002\u0000\u0000\f\u0010\u0003\u0002\u0001\u0000\r\u000e\u0004\u0001\u0001\u0001\u000e\u0010\u0005\u0003\u0000\u0000\u000f\n\u0001\u0000\u0000\u0000\u000f\r\u0001\u0000\u0000\u0000\u0010\u0013\u0001\u0000\u0000\u0000\u0011\u000f\u0001\u0000\u0000\u0000\u0011\u0012\u0001\u0000\u0000\u0000\u0012\u0003\u0001\u0000\u0000\u0000\u0013\u0011\u0001\u0000\u0000\u0000\u0014\u0019\u0005\r\u0000\u0000\u0015\u0016\u0005\u0006\u0000\u0000\u0016\u0018\u0005\r\u0000\u0000\u0017\u0015\u0001\u0000\u0000\u0000\u0018\u001b\u0001\u0000\u0000\u0000\u0019\u0017\u0001\u0000\u0000\u0000\u0019\u001a\u0001\u0000\u0000\u0000\u001a@\u0001\u0000\u0000\u0000\u001b\u0019\u0001\u0000\u0000\u0000\u001c@\u0007\u0000\u0000\u0000\u001d\u001e\u0005\f\u0000\u0000\u001e\u001f\u0005\r\u0000\u0000\u001f@\u0005\f\u0000\u0000 !\u0005\b\u0000\u0000!\"\u0003\u0002\u0001\u0000\"#\u0005\t\u0000\u0000#@\u0001\u0000\u0000\u0000$%\u0005\u0001\u0000\u0000%@\u0003\u0002\u0001\u0000&/\u0005\n\u0000\u0000',\u0003\u0002\u0001\u0000()\u0005\u0007\u0000\u0000)+\u0003\u0002\u0001\u0000*(\u0001\u0000\u0000\u0000+.\u0001\u0000\u0000\u0000,*\u0001\u0000\u0000\u0000,-\u0001\u0000\u0000\u0000-0\u0001\u0000\u0000\u0000.,\u0001\u0000\u0000\u0000/'\u0001\u0000\u0000\u0000/0\u0001\u0000\u0000\u000001\u0001\u0000\u0000\u00001@\u0005\u000b\u0000\u000023\u0005\r\u0000\u00003<\u0005\b\u0000\u000049\u0003\u0002\u0001\u000056\u0005\u0007\u0000\u000068\u0003\u0002\u0001\u000075\u0001\u0000\u0000\u00008;\u0001\u0000\u0000\u000097\u0001\u0000\u0000\u00009:\u0001\u0000\u0000\u0000:=\u0001\u0000\u0000\u0000;9\u0001\u0000\u0000\u0000<4\u0001\u0000\u0000\u0000<=\u0001\u0000\u0000\u0000=>\u0001\u0000\u0000\u0000>@\u0005\t\u0000\u0000?\u0014\u0001\u0000\u0000\u0000?\u001c\u0001\u0000\u0000\u0000?\u001d\u0001\u0000\u0000\u0000? \u0001\u0000\u0000\u0000?$\u0001\u0000\u0000\u0000?&\u0001\u0000\u0000\u0000?2\u0001\u0000\u0000\u0000@\u0005\u0001\u0000\u0000\u0000\b\u000f\u0011\u0019,/9<?";
   }

   public ATN getATN() {
      return _ATN;
   }

   public AntlrFilterParser(TokenStream input, FilterOperators operators) {
      this(input);
      this.operators = operators;
   }

   public Integer getPrecedence(Token op) {
      if (op.getType() == 1) {
         return this.operators.getPrefixOperator(op.getText()).getPriority();
      } else if (op.getType() == 2) {
         return this.operators.getInfixOperator(op.getText()).getPriority();
      } else if (op.getType() == 3) {
         return this.operators.getPostfixOperator(op.getText()).getPriority();
      } else {
         throw new IllegalStateException("Unexpected token `" + op.getText() + "`");
      }
   }

   public Integer getNextPrecedence(Token op) {
      Integer p = this.getPrecedence(op);
      if (op.getType() == 1) {
         return p;
      } else if (op.getType() == 3) {
         return p;
      } else if (op.getType() == 2) {
         return p + 1;
      } else {
         throw new IllegalStateException("Unexpected token `" + op.getText() + "`");
      }
   }

   public AntlrFilterParser(TokenStream input) {
      super(input);
      this._interp = new ParserATNSimulator(this, _ATN, _decisionToDFA, _sharedContextCache);
   }

   public final FilterContext filter() throws RecognitionException {
      FilterContext _localctx = new FilterContext(this._ctx, this.getState());
      this.enterRule(_localctx, 0, 0);

      try {
         this.enterOuterAlt(_localctx, 1);
         this.setState(6);
         this.expression(0);
         this.setState(7);
         this.match(-1);
      } catch (RecognitionException var6) {
         _localctx.exception = var6;
         this._errHandler.reportError(this, var6);
         this._errHandler.recover(this, var6);
      } finally {
         this.exitRule();
      }

      return _localctx;
   }

   public final ExpressionContext expression(int _p) throws RecognitionException {
      ExpressionContext _localctx = new ExpressionContext(this._ctx, this.getState(), _p);
      this.enterRule(_localctx, 2, 1);

      try {
         this.enterOuterAlt(_localctx, 1);
         this.setState(9);
         this.atom();
         this.setState(17);
         this._errHandler.sync(this);

         for(int _alt = ((ParserATNSimulator)this.getInterpreter()).adaptivePredict(this._input, 1, this._ctx); _alt != 2 && _alt != 0; _alt = ((ParserATNSimulator)this.getInterpreter()).adaptivePredict(this._input, 1, this._ctx)) {
            if (_alt == 1) {
               this.setState(15);
               this._errHandler.sync(this);
               switch(((ParserATNSimulator)this.getInterpreter()).adaptivePredict(this._input, 0, this._ctx)) {
               case 1:
                  this.setState(10);
                  if (this.getPrecedence(this._input.LT(1)) < _localctx._p) {
                     throw new FailedPredicateException(this, "getPrecedence(_input.LT(1)) >= $_p");
                  }

                  this.setState(11);
                  _localctx.op = this.match(2);
                  this.setState(12);
                  this.expression(this.getNextPrecedence(_localctx.op));
                  break;
               case 2:
                  this.setState(13);
                  if (this.getPrecedence(this._input.LT(1)) < _localctx._p) {
                     throw new FailedPredicateException(this, "getPrecedence(_input.LT(1)) >= $_p");
                  }

                  this.setState(14);
                  this.match(3);
               }
            }

            this.setState(19);
            this._errHandler.sync(this);
         }
      } catch (RecognitionException var7) {
         _localctx.exception = var7;
         this._errHandler.reportError(this, var7);
         this._errHandler.recover(this, var7);
      } finally {
         this.exitRule();
      }

      return _localctx;
   }

   public final AtomContext atom() throws RecognitionException {
      AtomContext _localctx = new AtomContext(this._ctx, this.getState());
      this.enterRule((ParserRuleContext)_localctx, 4, 2);

      try {
         this.setState(63);
         this._errHandler.sync(this);
         int _la;
         switch(((ParserATNSimulator)this.getInterpreter()).adaptivePredict(this._input, 7, this._ctx)) {
         case 1:
            _localctx = new FieldContext((AtomContext)_localctx);
            this.enterOuterAlt((ParserRuleContext)_localctx, 1);
            this.setState(20);
            this.match(13);
            this.setState(25);
            this._errHandler.sync(this);

            for(int _alt = ((ParserATNSimulator)this.getInterpreter()).adaptivePredict(this._input, 2, this._ctx); _alt != 2 && _alt != 0; _alt = ((ParserATNSimulator)this.getInterpreter()).adaptivePredict(this._input, 2, this._ctx)) {
               if (_alt == 1) {
                  this.setState(21);
                  this.match(6);
                  this.setState(22);
                  this.match(13);
               }

               this.setState(27);
               this._errHandler.sync(this);
            }

            return (AtomContext)_localctx;
         case 2:
            _localctx = new InputContext((AtomContext)_localctx);
            this.enterOuterAlt((ParserRuleContext)_localctx, 2);
            this.setState(28);
            _la = this._input.LA(1);
            if ((_la & -64) == 0 && (1L << _la & 49200L) != 0L) {
               if (this._input.LA(1) == -1) {
                  this.matchedEOF = true;
               }

               this._errHandler.reportMatch(this);
               this.consume();
            } else {
               this._errHandler.recoverInline(this);
            }
            break;
         case 3:
            _localctx = new PlaceholderContext((AtomContext)_localctx);
            this.enterOuterAlt((ParserRuleContext)_localctx, 3);
            this.setState(29);
            this.match(12);
            this.setState(30);
            this.match(13);
            this.setState(31);
            this.match(12);
            break;
         case 4:
            _localctx = new PriorityContext((AtomContext)_localctx);
            this.enterOuterAlt((ParserRuleContext)_localctx, 4);
            this.setState(32);
            this.match(8);
            this.setState(33);
            this.expression(0);
            this.setState(34);
            this.match(9);
            break;
         case 5:
            _localctx = new PrefixExpressionContext((AtomContext)_localctx);
            this.enterOuterAlt((ParserRuleContext)_localctx, 5);
            this.setState(36);
            ((PrefixExpressionContext)_localctx).op = this.match(1);
            this.setState(37);
            this.expression(this.getNextPrecedence(((PrefixExpressionContext)_localctx).op));
            break;
         case 6:
            _localctx = new CollectionContext((AtomContext)_localctx);
            this.enterOuterAlt((ParserRuleContext)_localctx, 6);
            this.setState(38);
            this.match(10);
            this.setState(47);
            this._errHandler.sync(this);
            _la = this._input.LA(1);
            if ((_la & -64) == 0 && (1L << _la & 62770L) != 0L) {
               this.setState(39);
               ((CollectionContext)_localctx).expression = this.expression(0);
               ((CollectionContext)_localctx).items.add(((CollectionContext)_localctx).expression);
               this.setState(44);
               this._errHandler.sync(this);

               for(_la = this._input.LA(1); _la == 7; _la = this._input.LA(1)) {
                  this.setState(40);
                  this.match(7);
                  this.setState(41);
                  ((CollectionContext)_localctx).expression = this.expression(0);
                  ((CollectionContext)_localctx).items.add(((CollectionContext)_localctx).expression);
                  this.setState(46);
                  this._errHandler.sync(this);
               }
            }

            this.setState(49);
            this.match(11);
            break;
         case 7:
            _localctx = new FunctionContext((AtomContext)_localctx);
            this.enterOuterAlt((ParserRuleContext)_localctx, 7);
            this.setState(50);
            this.match(13);
            this.setState(51);
            this.match(8);
            this.setState(60);
            this._errHandler.sync(this);
            _la = this._input.LA(1);
            if ((_la & -64) == 0 && (1L << _la & 62770L) != 0L) {
               this.setState(52);
               ((FunctionContext)_localctx).expression = this.expression(0);
               ((FunctionContext)_localctx).arguments.add(((FunctionContext)_localctx).expression);
               this.setState(57);
               this._errHandler.sync(this);

               for(_la = this._input.LA(1); _la == 7; _la = this._input.LA(1)) {
                  this.setState(53);
                  this.match(7);
                  this.setState(54);
                  ((FunctionContext)_localctx).expression = this.expression(0);
                  ((FunctionContext)_localctx).arguments.add(((FunctionContext)_localctx).expression);
                  this.setState(59);
                  this._errHandler.sync(this);
               }
            }

            this.setState(62);
            this.match(9);
         }
      } catch (RecognitionException var7) {
         ((AtomContext)_localctx).exception = var7;
         this._errHandler.reportError(this, var7);
         this._errHandler.recover(this, var7);
      } finally {
         this.exitRule();
      }

      return (AtomContext)_localctx;
   }

   public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
      switch(ruleIndex) {
      case 1:
         return this.expression_sempred((ExpressionContext)_localctx, predIndex);
      default:
         return true;
      }
   }

   private boolean expression_sempred(ExpressionContext _localctx, int predIndex) {
      switch(predIndex) {
      case 0:
         return this.getPrecedence(this._input.LT(1)) >= _localctx._p;
      case 1:
         return this.getPrecedence(this._input.LT(1)) >= _localctx._p;
      default:
         return true;
      }
   }

   static {
      RuntimeMetaData.checkVersion("4.13.1", "4.13.1");
      _sharedContextCache = new PredictionContextCache();
      ruleNames = makeRuleNames();
      _LITERAL_NAMES = makeLiteralNames();
      _SYMBOLIC_NAMES = makeSymbolicNames();
      VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);
      tokenNames = new String[_SYMBOLIC_NAMES.length];

      int i;
      for(i = 0; i < tokenNames.length; ++i) {
         tokenNames[i] = VOCABULARY.getLiteralName(i);
         if (tokenNames[i] == null) {
            tokenNames[i] = VOCABULARY.getSymbolicName(i);
         }

         if (tokenNames[i] == null) {
            tokenNames[i] = "<INVALID>";
         }
      }

      _ATN = (new ATNDeserializer()).deserialize("\u0004\u0001\u0011B\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002\u0002\u0007\u0002\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0005\u0001\u0010\b\u0001\n\u0001\f\u0001\u0013\t\u0001\u0001\u0002\u0001\u0002\u0001\u0002\u0005\u0002\u0018\b\u0002\n\u0002\f\u0002\u001b\t\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0005\u0002+\b\u0002\n\u0002\f\u0002.\t\u0002\u0003\u00020\b\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0005\u00028\b\u0002\n\u0002\f\u0002;\t\u0002\u0003\u0002=\b\u0002\u0001\u0002\u0003\u0002@\b\u0002\u0001\u0002\u0000\u0000\u0003\u0000\u0002\u0004\u0000\u0001\u0002\u0000\u0004\u0005\u000e\u000fK\u0000\u0006\u0001\u0000\u0000\u0000\u0002\t\u0001\u0000\u0000\u0000\u0004?\u0001\u0000\u0000\u0000\u0006\u0007\u0003\u0002\u0001\u0000\u0007\b\u0005\u0000\u0000\u0001\b\u0001\u0001\u0000\u0000\u0000\t\u0011\u0003\u0004\u0002\u0000\n\u000b\u0004\u0001\u0000\u0001\u000b\f\u0005\u0002\u0000\u0000\f\u0010\u0003\u0002\u0001\u0000\r\u000e\u0004\u0001\u0001\u0001\u000e\u0010\u0005\u0003\u0000\u0000\u000f\n\u0001\u0000\u0000\u0000\u000f\r\u0001\u0000\u0000\u0000\u0010\u0013\u0001\u0000\u0000\u0000\u0011\u000f\u0001\u0000\u0000\u0000\u0011\u0012\u0001\u0000\u0000\u0000\u0012\u0003\u0001\u0000\u0000\u0000\u0013\u0011\u0001\u0000\u0000\u0000\u0014\u0019\u0005\r\u0000\u0000\u0015\u0016\u0005\u0006\u0000\u0000\u0016\u0018\u0005\r\u0000\u0000\u0017\u0015\u0001\u0000\u0000\u0000\u0018\u001b\u0001\u0000\u0000\u0000\u0019\u0017\u0001\u0000\u0000\u0000\u0019\u001a\u0001\u0000\u0000\u0000\u001a@\u0001\u0000\u0000\u0000\u001b\u0019\u0001\u0000\u0000\u0000\u001c@\u0007\u0000\u0000\u0000\u001d\u001e\u0005\f\u0000\u0000\u001e\u001f\u0005\r\u0000\u0000\u001f@\u0005\f\u0000\u0000 !\u0005\b\u0000\u0000!\"\u0003\u0002\u0001\u0000\"#\u0005\t\u0000\u0000#@\u0001\u0000\u0000\u0000$%\u0005\u0001\u0000\u0000%@\u0003\u0002\u0001\u0000&/\u0005\n\u0000\u0000',\u0003\u0002\u0001\u0000()\u0005\u0007\u0000\u0000)+\u0003\u0002\u0001\u0000*(\u0001\u0000\u0000\u0000+.\u0001\u0000\u0000\u0000,*\u0001\u0000\u0000\u0000,-\u0001\u0000\u0000\u0000-0\u0001\u0000\u0000\u0000.,\u0001\u0000\u0000\u0000/'\u0001\u0000\u0000\u0000/0\u0001\u0000\u0000\u000001\u0001\u0000\u0000\u00001@\u0005\u000b\u0000\u000023\u0005\r\u0000\u00003<\u0005\b\u0000\u000049\u0003\u0002\u0001\u000056\u0005\u0007\u0000\u000068\u0003\u0002\u0001\u000075\u0001\u0000\u0000\u00008;\u0001\u0000\u0000\u000097\u0001\u0000\u0000\u00009:\u0001\u0000\u0000\u0000:=\u0001\u0000\u0000\u0000;9\u0001\u0000\u0000\u0000<4\u0001\u0000\u0000\u0000<=\u0001\u0000\u0000\u0000=>\u0001\u0000\u0000\u0000>@\u0005\t\u0000\u0000?\u0014\u0001\u0000\u0000\u0000?\u001c\u0001\u0000\u0000\u0000?\u001d\u0001\u0000\u0000\u0000? \u0001\u0000\u0000\u0000?$\u0001\u0000\u0000\u0000?&\u0001\u0000\u0000\u0000?2\u0001\u0000\u0000\u0000@\u0005\u0001\u0000\u0000\u0000\b\u000f\u0011\u0019,/9<?".toCharArray());
      _decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];

      for(i = 0; i < _ATN.getNumberOfDecisions(); ++i) {
         _decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
      }

   }

   public static class FilterContext extends AntlrBaseContext {
      public ExpressionContext expression() {
         return (ExpressionContext)this.getRuleContext(ExpressionContext.class, 0);
      }

      public TerminalNode EOF() {
         return this.getToken(-1, 0);
      }

      public FilterContext(ParserRuleContext parent, int invokingState) {
         super(parent, invokingState);
      }

      public int getRuleIndex() {
         return 0;
      }
   }

   public static class ExpressionContext extends AntlrBaseContext {
      public int _p;
      public Token op;

      public AtomContext atom() {
         return (AtomContext)this.getRuleContext(AtomContext.class, 0);
      }

      public List<ExpressionContext> expression() {
         return this.getRuleContexts(ExpressionContext.class);
      }

      public ExpressionContext expression(int i) {
         return (ExpressionContext)this.getRuleContext(ExpressionContext.class, i);
      }

      public List<TerminalNode> POSTFIX_OPERATOR() {
         return this.getTokens(3);
      }

      public TerminalNode POSTFIX_OPERATOR(int i) {
         return this.getToken(3, i);
      }

      public List<TerminalNode> INFIX_OPERATOR() {
         return this.getTokens(2);
      }

      public TerminalNode INFIX_OPERATOR(int i) {
         return this.getToken(2, i);
      }

      public ExpressionContext(ParserRuleContext parent, int invokingState) {
         super(parent, invokingState);
      }

      public ExpressionContext(ParserRuleContext parent, int invokingState, int _p) {
         super(parent, invokingState);
         this._p = _p;
      }

      public int getRuleIndex() {
         return 1;
      }
   }

   public static class AtomContext extends AntlrBaseContext {
      public AtomContext(ParserRuleContext parent, int invokingState) {
         super(parent, invokingState);
      }

      public int getRuleIndex() {
         return 2;
      }

      public AtomContext() {
      }

      public void copyFrom(AtomContext ctx) {
         super.copyFrom(ctx);
      }
   }

   public static class FieldContext extends AtomContext {
      public List<TerminalNode> ID() {
         return this.getTokens(13);
      }

      public TerminalNode ID(int i) {
         return this.getToken(13, i);
      }

      public List<TerminalNode> DOT() {
         return this.getTokens(6);
      }

      public TerminalNode DOT(int i) {
         return this.getToken(6, i);
      }

      public FieldContext(AtomContext ctx) {
         this.copyFrom(ctx);
      }
   }

   public static class InputContext extends AtomContext {
      public TerminalNode STRING() {
         return this.getToken(15, 0);
      }

      public TerminalNode NUMBER() {
         return this.getToken(14, 0);
      }

      public TerminalNode TRUE() {
         return this.getToken(4, 0);
      }

      public TerminalNode FALSE() {
         return this.getToken(5, 0);
      }

      public InputContext(AtomContext ctx) {
         this.copyFrom(ctx);
      }
   }

   public static class PlaceholderContext extends AtomContext {
      public List<TerminalNode> BTICK() {
         return this.getTokens(12);
      }

      public TerminalNode BTICK(int i) {
         return this.getToken(12, i);
      }

      public TerminalNode ID() {
         return this.getToken(13, 0);
      }

      public PlaceholderContext(AtomContext ctx) {
         this.copyFrom(ctx);
      }
   }

   public static class PriorityContext extends AtomContext {
      public TerminalNode LPAREN() {
         return this.getToken(8, 0);
      }

      public ExpressionContext expression() {
         return (ExpressionContext)this.getRuleContext(ExpressionContext.class, 0);
      }

      public TerminalNode RPAREN() {
         return this.getToken(9, 0);
      }

      public PriorityContext(AtomContext ctx) {
         this.copyFrom(ctx);
      }
   }

   public static class PrefixExpressionContext extends AtomContext {
      public Token op;

      public ExpressionContext expression() {
         return (ExpressionContext)this.getRuleContext(ExpressionContext.class, 0);
      }

      public TerminalNode PREFIX_OPERATOR() {
         return this.getToken(1, 0);
      }

      public PrefixExpressionContext(AtomContext ctx) {
         this.copyFrom(ctx);
      }
   }

   public static class CollectionContext extends AtomContext {
      public ExpressionContext expression;
      public List<ExpressionContext> items = new ArrayList();

      public TerminalNode LBRACK() {
         return this.getToken(10, 0);
      }

      public TerminalNode RBRACK() {
         return this.getToken(11, 0);
      }

      public List<ExpressionContext> expression() {
         return this.getRuleContexts(ExpressionContext.class);
      }

      public ExpressionContext expression(int i) {
         return (ExpressionContext)this.getRuleContext(ExpressionContext.class, i);
      }

      public List<TerminalNode> COMMA() {
         return this.getTokens(7);
      }

      public TerminalNode COMMA(int i) {
         return this.getToken(7, i);
      }

      public CollectionContext(AtomContext ctx) {
         this.copyFrom(ctx);
      }
   }

   public static class FunctionContext extends AtomContext {
      public ExpressionContext expression;
      public List<ExpressionContext> arguments = new ArrayList();

      public TerminalNode ID() {
         return this.getToken(13, 0);
      }

      public TerminalNode LPAREN() {
         return this.getToken(8, 0);
      }

      public TerminalNode RPAREN() {
         return this.getToken(9, 0);
      }

      public List<ExpressionContext> expression() {
         return this.getRuleContexts(ExpressionContext.class);
      }

      public ExpressionContext expression(int i) {
         return (ExpressionContext)this.getRuleContext(ExpressionContext.class, i);
      }

      public List<TerminalNode> COMMA() {
         return this.getTokens(7);
      }

      public TerminalNode COMMA(int i) {
         return this.getToken(7, i);
      }

      public FunctionContext(AtomContext ctx) {
         this.copyFrom(ctx);
      }
   }
}
