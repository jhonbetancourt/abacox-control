package com.infomedia.abacox.control.component.springfilter.parser;

import com.infomedia.abacox.control.component.springfilter.definition.*;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonToken;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.RuntimeMetaData;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.Vocabulary;
import org.antlr.v4.runtime.VocabularyImpl;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.LexerATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.Pair;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

class AntlrFilterLexer extends Lexer {
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
   public static String[] channelNames;
   public static String[] modeNames;
   public static final String[] ruleNames;
   private static final String[] _LITERAL_NAMES;
   private static final String[] _SYMBOLIC_NAMES;
   public static final Vocabulary VOCABULARY;
   /** @deprecated */
   @Deprecated
   public static final String[] tokenNames;
   private FilterOperators operators;
   private Deque<Token> deque;
   private Token previousToken;
   private Token nextToken;
   public static final String _serializedATN = "\u0004\u0000\u0011{\u0006\uffff\uffff\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b\u0002\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002\u000f\u0007\u000f\u0002\u0010\u0007\u0010\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0003\u00035\b\u0003\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0003\u0004A\b\u0004\u0001\u0005\u0001\u0005\u0001\u0006\u0001\u0006\u0001\u0007\u0001\u0007\u0001\b\u0001\b\u0001\t\u0001\t\u0001\n\u0001\n\u0001\u000b\u0001\u000b\u0001\f\u0001\f\u0005\fS\b\f\n\f\f\fV\t\f\u0001\r\u0003\rY\b\r\u0001\r\u0004\r\\\b\r\u000b\r\f\r]\u0001\r\u0001\r\u0004\rb\b\r\u000b\r\f\rc\u0003\rf\b\r\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0005\u000el\b\u000e\n\u000e\f\u000eo\t\u000e\u0001\u000e\u0001\u000e\u0001\u000f\u0004\u000ft\b\u000f\u000b\u000f\f\u000fu\u0001\u000f\u0001\u000f\u0001\u0010\u0001\u0010\u0000\u0000\u0011\u0001\u0001\u0003\u0002\u0005\u0003\u0007\u0004\t\u0005\u000b\u0006\r\u0007\u000f\b\u0011\t\u0013\n\u0015\u000b\u0017\f\u0019\r\u001b\u000e\u001d\u000f\u001f\u0010!\u0011\u0001\u0000\u0005\u0004\u0000$$AZ__az\u0005\u0000$$09AZ__az\u0001\u000009\u0002\u0000''\\\\\u0002\u0000\t\t  \u0084\u0000\u0001\u0001\u0000\u0000\u0000\u0000\u0003\u0001\u0000\u0000\u0000\u0000\u0005\u0001\u0000\u0000\u0000\u0000\u0007\u0001\u0000\u0000\u0000\u0000\t\u0001\u0000\u0000\u0000\u0000\u000b\u0001\u0000\u0000\u0000\u0000\r\u0001\u0000\u0000\u0000\u0000\u000f\u0001\u0000\u0000\u0000\u0000\u0011\u0001\u0000\u0000\u0000\u0000\u0013\u0001\u0000\u0000\u0000\u0000\u0015\u0001\u0000\u0000\u0000\u0000\u0017\u0001\u0000\u0000\u0000\u0000\u0019\u0001\u0000\u0000\u0000\u0000\u001b\u0001\u0000\u0000\u0000\u0000\u001d\u0001\u0000\u0000\u0000\u0000\u001f\u0001\u0000\u0000\u0000\u0000!\u0001\u0000\u0000\u0000\u0001#\u0001\u0000\u0000\u0000\u0003&\u0001\u0000\u0000\u0000\u0005)\u0001\u0000\u0000\u0000\u00074\u0001\u0000\u0000\u0000\t@\u0001\u0000\u0000\u0000\u000bB\u0001\u0000\u0000\u0000\rD\u0001\u0000\u0000\u0000\u000fF\u0001\u0000\u0000\u0000\u0011H\u0001\u0000\u0000\u0000\u0013J\u0001\u0000\u0000\u0000\u0015L\u0001\u0000\u0000\u0000\u0017N\u0001\u0000\u0000\u0000\u0019P\u0001\u0000\u0000\u0000\u001bX\u0001\u0000\u0000\u0000\u001dg\u0001\u0000\u0000\u0000\u001fs\u0001\u0000\u0000\u0000!y\u0001\u0000\u0000\u0000#$\u0004\u0000\u0000\u0000$%\t\u0000\u0000\u0000%\u0002\u0001\u0000\u0000\u0000&'\u0004\u0001\u0001\u0000'(\t\u0000\u0000\u0000(\u0004\u0001\u0000\u0000\u0000)*\u0004\u0002\u0002\u0000*+\t\u0000\u0000\u0000+\u0006\u0001\u0000\u0000\u0000,-\u0005T\u0000\u0000-.\u0005R\u0000\u0000./\u0005U\u0000\u0000/5\u0005E\u0000\u000001\u0005t\u0000\u000012\u0005r\u0000\u000023\u0005u\u0000\u000035\u0005e\u0000\u00004,\u0001\u0000\u0000\u000040\u0001\u0000\u0000\u00005\b\u0001\u0000\u0000\u000067\u0005F\u0000\u000078\u0005A\u0000\u000089\u0005L\u0000\u00009:\u0005S\u0000\u0000:A\u0005E\u0000\u0000;<\u0005f\u0000\u0000<=\u0005a\u0000\u0000=>\u0005l\u0000\u0000>?\u0005s\u0000\u0000?A\u0005e\u0000\u0000@6\u0001\u0000\u0000\u0000@;\u0001\u0000\u0000\u0000A\n\u0001\u0000\u0000\u0000BC\u0005.\u0000\u0000C\f\u0001\u0000\u0000\u0000DE\u0005,\u0000\u0000E\u000e\u0001\u0000\u0000\u0000FG\u0005(\u0000\u0000G\u0010\u0001\u0000\u0000\u0000HI\u0005)\u0000\u0000I\u0012\u0001\u0000\u0000\u0000JK\u0005[\u0000\u0000K\u0014\u0001\u0000\u0000\u0000LM\u0005]\u0000\u0000M\u0016\u0001\u0000\u0000\u0000NO\u0005`\u0000\u0000O\u0018\u0001\u0000\u0000\u0000PT\u0007\u0000\u0000\u0000QS\u0007\u0001\u0000\u0000RQ\u0001\u0000\u0000\u0000SV\u0001\u0000\u0000\u0000TR\u0001\u0000\u0000\u0000TU\u0001\u0000\u0000\u0000U\u001a\u0001\u0000\u0000\u0000VT\u0001\u0000\u0000\u0000WY\u0005-\u0000\u0000XW\u0001\u0000\u0000\u0000XY\u0001\u0000\u0000\u0000Y[\u0001\u0000\u0000\u0000Z\\\u0007\u0002\u0000\u0000[Z\u0001\u0000\u0000\u0000\\]\u0001\u0000\u0000\u0000][\u0001\u0000\u0000\u0000]^\u0001\u0000\u0000\u0000^e\u0001\u0000\u0000\u0000_a\u0005.\u0000\u0000`b\u0007\u0002\u0000\u0000a`\u0001\u0000\u0000\u0000bc\u0001\u0000\u0000\u0000ca\u0001\u0000\u0000\u0000cd\u0001\u0000\u0000\u0000df\u0001\u0000\u0000\u0000e_\u0001\u0000\u0000\u0000ef\u0001\u0000\u0000\u0000f\u001c\u0001\u0000\u0000\u0000gm\u0005'\u0000\u0000hl\b\u0003\u0000\u0000ij\u0005\\\u0000\u0000jl\u0007\u0003\u0000\u0000kh\u0001\u0000\u0000\u0000ki\u0001\u0000\u0000\u0000lo\u0001\u0000\u0000\u0000mk\u0001\u0000\u0000\u0000mn\u0001\u0000\u0000\u0000np\u0001\u0000\u0000\u0000om\u0001\u0000\u0000\u0000pq\u0005'\u0000\u0000q\u001e\u0001\u0000\u0000\u0000rt\u0007\u0004\u0000\u0000sr\u0001\u0000\u0000\u0000tu\u0001\u0000\u0000\u0000us\u0001\u0000\u0000\u0000uv\u0001\u0000\u0000\u0000vw\u0001\u0000\u0000\u0000wx\u0006\u000f\u0000\u0000x \u0001\u0000\u0000\u0000yz\t\u0000\u0000\u0000z\"\u0001\u0000\u0000\u0000\u000b\u00004@TX]cekmu\u0001\u0006\u0000\u0000";
   public static final ATN _ATN;

   private static String[] makeRuleNames() {
      return new String[]{"PREFIX_OPERATOR", "INFIX_OPERATOR", "POSTFIX_OPERATOR", "TRUE", "FALSE", "DOT", "COMMA", "LPAREN", "RPAREN", "LBRACK", "RBRACK", "BTICK", "ID", "NUMBER", "STRING", "WS", "SYMBOL"};
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

   public AntlrFilterLexer(CharStream input, FilterOperators operators) {
      this(input);
      this.operators = operators;
   }

   boolean tryOperator(Class<? extends FilterOperator> targetClass) {
      Iterator var2 = this.operators.getSortedOperators().iterator();

      Pair operatorWithToken;
      boolean matched;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         operatorWithToken = (Pair)var2.next();
         matched = true;

         for(int i = 0; i < ((String)operatorWithToken.b).length(); ++i) {
            if (Character.toLowerCase((char)this._input.LA(i + 1)) != ((String)operatorWithToken.b).charAt(i)) {
               matched = false;
               break;
            }
         }
      } while(!matched);

      if (!targetClass.isAssignableFrom(((FilterOperator)operatorWithToken.a).getClass())) {
         return false;
      } else {
         this._input.seek(this._input.index() + ((String)operatorWithToken.b).length() - 1);
         return true;
      }
   }

   public Token nextToken() {
      if (!this.deque.isEmpty()) {
         return this.previousToken = (Token)this.deque.pollFirst();
      } else {
         Token next = super.nextToken();
         if (next.getType() != 17) {
            return this.previousToken = next;
         } else {
            StringBuilder builder = new StringBuilder();
            if (next.getType() == 17) {
               builder.append(next.getText());
               next = super.nextToken();
            }

            this.deque.addLast(this.nextToken = next);
            List<Token> tokens = this.findOperatorCombination(builder.toString(), this.getOperatorType());

            for(int i = tokens.size() - 1; i >= 0; --i) {
               this.deque.addFirst((Token)tokens.get(i));
            }

            return (Token)this.deque.pollFirst();
         }
      }
   }

   private List<Token> findOperatorCombination(String sequence, OperatorType type) {
      switch(type) {
      case POSTFIX_OPERATOR:
         return this.getPostfixCombination(sequence);
      case PREFIX_OPERATOR:
         return this.getPrefixCombination(sequence);
      case INFIX_OPERATOR:
         return this.getInfixCombination(sequence);
      default:
         return null;
      }
   }

   private List<Token> getPrefixCombination(String sequence) {
      if (this.isPrefixOperator(sequence)) {
         List<Token> seq = new ArrayList(1);
         seq.add(0, new CommonToken(1, sequence));
         return seq;
      } else if (sequence.length() <= 1) {
         return null;
      } else {
         for(int i = 1; i < sequence.length(); ++i) {
            List<Token> seq1 = this.getPrefixCombination(sequence.substring(0, i));
            List<Token> seq2 = this.getPrefixCombination(sequence.substring(i, sequence.length()));
            if (seq1 != null & seq2 != null) {
               seq1.addAll(seq2);
               return seq1;
            }
         }

         return null;
      }
   }

   private List<Token> getPostfixCombination(String sequence) {
      if (this.isPostfixOperator(sequence)) {
         List<Token> seq = new ArrayList(1);
         seq.add(0, new CommonToken(3, sequence));
         return seq;
      } else if (sequence.length() <= 1) {
         return null;
      } else {
         for(int i = 1; i < sequence.length(); ++i) {
            List<Token> seq1 = this.getPostfixCombination(sequence.substring(0, i));
            List<Token> seq2 = this.getPostfixCombination(sequence.substring(i, sequence.length()));
            if (seq1 != null && seq2 != null) {
               seq1.addAll(seq2);
               return seq1;
            }
         }

         return null;
      }
   }

   private List<Token> getInfixCombination(String sequence) {
      for(int i = 0; i < sequence.length(); ++i) {
         for(int j = 0; j < sequence.length() - i; ++j) {
            String seqPost = sequence.substring(0, i);
            List<Token> post = this.getPostfixCombination(seqPost);
            String seqPre = sequence.substring(sequence.length() - j, sequence.length());
            List<Token> pre = this.getPrefixCombination(seqPre);
            String seqIn = sequence.substring(i, sequence.length() - j);
            if ((post != null || seqPost.isEmpty()) && (pre != null || seqPre.isEmpty()) && this.isInfixOperator(seqIn)) {
               List<Token> res = new ArrayList();
               if (post != null) {
                  res.addAll(post);
               }

               res.add(new CommonToken(2, seqIn));
               if (pre != null) {
                  res.addAll(pre);
               }

               return res;
            }
         }
      }

      return null;
   }

   private OperatorType getOperatorType() {
      if (this.isAfterAtom()) {
         return this.isBeforeAtom() ? OperatorType.INFIX_OPERATOR : OperatorType.POSTFIX_OPERATOR;
      } else {
         return OperatorType.PREFIX_OPERATOR;
      }
   }

   private boolean isBeforeAtom() {
      int type = this.nextToken.getType();
      return type == 14 || type == 13 || type == 15 || type == 4 || type == 5 || type == 8;
   }

   private boolean isAfterAtom() {
      int type = this.previousToken.getType();
      return type == 14 || type == 13 || type == 15 || type == 4 || type == 5 || type == 9;
   }

   private boolean isPrefixOperator(String operator) {
      try {
         this.operators.getPrefixOperator(operator);
         return true;
      } catch (Exception var3) {
         return false;
      }
   }

   private boolean isInfixOperator(String operator) {
      try {
         this.operators.getInfixOperator(operator);
         return true;
      } catch (Exception var3) {
         return false;
      }
   }

   private boolean isPostfixOperator(String operator) {
      try {
         this.operators.getPostfixOperator(operator);
         return true;
      } catch (Exception var3) {
         return false;
      }
   }

   public AntlrFilterLexer(CharStream input) {
      super(input);
      this.deque = new LinkedList();
      this._interp = new LexerATNSimulator(this, _ATN, _decisionToDFA, _sharedContextCache);
   }

   public String getGrammarFileName() {
      return "AntlrFilter.g4";
   }

   public String[] getRuleNames() {
      return ruleNames;
   }

   public String getSerializedATN() {
      return "\u0004\u0000\u0011{\u0006\uffff\uffff\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b\u0002\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002\u000f\u0007\u000f\u0002\u0010\u0007\u0010\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0003\u00035\b\u0003\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0003\u0004A\b\u0004\u0001\u0005\u0001\u0005\u0001\u0006\u0001\u0006\u0001\u0007\u0001\u0007\u0001\b\u0001\b\u0001\t\u0001\t\u0001\n\u0001\n\u0001\u000b\u0001\u000b\u0001\f\u0001\f\u0005\fS\b\f\n\f\f\fV\t\f\u0001\r\u0003\rY\b\r\u0001\r\u0004\r\\\b\r\u000b\r\f\r]\u0001\r\u0001\r\u0004\rb\b\r\u000b\r\f\rc\u0003\rf\b\r\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0005\u000el\b\u000e\n\u000e\f\u000eo\t\u000e\u0001\u000e\u0001\u000e\u0001\u000f\u0004\u000ft\b\u000f\u000b\u000f\f\u000fu\u0001\u000f\u0001\u000f\u0001\u0010\u0001\u0010\u0000\u0000\u0011\u0001\u0001\u0003\u0002\u0005\u0003\u0007\u0004\t\u0005\u000b\u0006\r\u0007\u000f\b\u0011\t\u0013\n\u0015\u000b\u0017\f\u0019\r\u001b\u000e\u001d\u000f\u001f\u0010!\u0011\u0001\u0000\u0005\u0004\u0000$$AZ__az\u0005\u0000$$09AZ__az\u0001\u000009\u0002\u0000''\\\\\u0002\u0000\t\t  \u0084\u0000\u0001\u0001\u0000\u0000\u0000\u0000\u0003\u0001\u0000\u0000\u0000\u0000\u0005\u0001\u0000\u0000\u0000\u0000\u0007\u0001\u0000\u0000\u0000\u0000\t\u0001\u0000\u0000\u0000\u0000\u000b\u0001\u0000\u0000\u0000\u0000\r\u0001\u0000\u0000\u0000\u0000\u000f\u0001\u0000\u0000\u0000\u0000\u0011\u0001\u0000\u0000\u0000\u0000\u0013\u0001\u0000\u0000\u0000\u0000\u0015\u0001\u0000\u0000\u0000\u0000\u0017\u0001\u0000\u0000\u0000\u0000\u0019\u0001\u0000\u0000\u0000\u0000\u001b\u0001\u0000\u0000\u0000\u0000\u001d\u0001\u0000\u0000\u0000\u0000\u001f\u0001\u0000\u0000\u0000\u0000!\u0001\u0000\u0000\u0000\u0001#\u0001\u0000\u0000\u0000\u0003&\u0001\u0000\u0000\u0000\u0005)\u0001\u0000\u0000\u0000\u00074\u0001\u0000\u0000\u0000\t@\u0001\u0000\u0000\u0000\u000bB\u0001\u0000\u0000\u0000\rD\u0001\u0000\u0000\u0000\u000fF\u0001\u0000\u0000\u0000\u0011H\u0001\u0000\u0000\u0000\u0013J\u0001\u0000\u0000\u0000\u0015L\u0001\u0000\u0000\u0000\u0017N\u0001\u0000\u0000\u0000\u0019P\u0001\u0000\u0000\u0000\u001bX\u0001\u0000\u0000\u0000\u001dg\u0001\u0000\u0000\u0000\u001fs\u0001\u0000\u0000\u0000!y\u0001\u0000\u0000\u0000#$\u0004\u0000\u0000\u0000$%\t\u0000\u0000\u0000%\u0002\u0001\u0000\u0000\u0000&'\u0004\u0001\u0001\u0000'(\t\u0000\u0000\u0000(\u0004\u0001\u0000\u0000\u0000)*\u0004\u0002\u0002\u0000*+\t\u0000\u0000\u0000+\u0006\u0001\u0000\u0000\u0000,-\u0005T\u0000\u0000-.\u0005R\u0000\u0000./\u0005U\u0000\u0000/5\u0005E\u0000\u000001\u0005t\u0000\u000012\u0005r\u0000\u000023\u0005u\u0000\u000035\u0005e\u0000\u00004,\u0001\u0000\u0000\u000040\u0001\u0000\u0000\u00005\b\u0001\u0000\u0000\u000067\u0005F\u0000\u000078\u0005A\u0000\u000089\u0005L\u0000\u00009:\u0005S\u0000\u0000:A\u0005E\u0000\u0000;<\u0005f\u0000\u0000<=\u0005a\u0000\u0000=>\u0005l\u0000\u0000>?\u0005s\u0000\u0000?A\u0005e\u0000\u0000@6\u0001\u0000\u0000\u0000@;\u0001\u0000\u0000\u0000A\n\u0001\u0000\u0000\u0000BC\u0005.\u0000\u0000C\f\u0001\u0000\u0000\u0000DE\u0005,\u0000\u0000E\u000e\u0001\u0000\u0000\u0000FG\u0005(\u0000\u0000G\u0010\u0001\u0000\u0000\u0000HI\u0005)\u0000\u0000I\u0012\u0001\u0000\u0000\u0000JK\u0005[\u0000\u0000K\u0014\u0001\u0000\u0000\u0000LM\u0005]\u0000\u0000M\u0016\u0001\u0000\u0000\u0000NO\u0005`\u0000\u0000O\u0018\u0001\u0000\u0000\u0000PT\u0007\u0000\u0000\u0000QS\u0007\u0001\u0000\u0000RQ\u0001\u0000\u0000\u0000SV\u0001\u0000\u0000\u0000TR\u0001\u0000\u0000\u0000TU\u0001\u0000\u0000\u0000U\u001a\u0001\u0000\u0000\u0000VT\u0001\u0000\u0000\u0000WY\u0005-\u0000\u0000XW\u0001\u0000\u0000\u0000XY\u0001\u0000\u0000\u0000Y[\u0001\u0000\u0000\u0000Z\\\u0007\u0002\u0000\u0000[Z\u0001\u0000\u0000\u0000\\]\u0001\u0000\u0000\u0000][\u0001\u0000\u0000\u0000]^\u0001\u0000\u0000\u0000^e\u0001\u0000\u0000\u0000_a\u0005.\u0000\u0000`b\u0007\u0002\u0000\u0000a`\u0001\u0000\u0000\u0000bc\u0001\u0000\u0000\u0000ca\u0001\u0000\u0000\u0000cd\u0001\u0000\u0000\u0000df\u0001\u0000\u0000\u0000e_\u0001\u0000\u0000\u0000ef\u0001\u0000\u0000\u0000f\u001c\u0001\u0000\u0000\u0000gm\u0005'\u0000\u0000hl\b\u0003\u0000\u0000ij\u0005\\\u0000\u0000jl\u0007\u0003\u0000\u0000kh\u0001\u0000\u0000\u0000ki\u0001\u0000\u0000\u0000lo\u0001\u0000\u0000\u0000mk\u0001\u0000\u0000\u0000mn\u0001\u0000\u0000\u0000np\u0001\u0000\u0000\u0000om\u0001\u0000\u0000\u0000pq\u0005'\u0000\u0000q\u001e\u0001\u0000\u0000\u0000rt\u0007\u0004\u0000\u0000sr\u0001\u0000\u0000\u0000tu\u0001\u0000\u0000\u0000us\u0001\u0000\u0000\u0000uv\u0001\u0000\u0000\u0000vw\u0001\u0000\u0000\u0000wx\u0006\u000f\u0000\u0000x \u0001\u0000\u0000\u0000yz\t\u0000\u0000\u0000z\"\u0001\u0000\u0000\u0000\u000b\u00004@TX]cekmu\u0001\u0006\u0000\u0000";
   }

   public String[] getChannelNames() {
      return channelNames;
   }

   public String[] getModeNames() {
      return modeNames;
   }

   public ATN getATN() {
      return _ATN;
   }

   public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
      switch(ruleIndex) {
      case 0:
         return this.PREFIX_OPERATOR_sempred(_localctx, predIndex);
      case 1:
         return this.INFIX_OPERATOR_sempred(_localctx, predIndex);
      case 2:
         return this.POSTFIX_OPERATOR_sempred(_localctx, predIndex);
      default:
         return true;
      }
   }

   private boolean PREFIX_OPERATOR_sempred(RuleContext _localctx, int predIndex) {
      switch(predIndex) {
      case 0:
         return this.tryOperator(FilterPrefixOperator.class);
      default:
         return true;
      }
   }

   private boolean INFIX_OPERATOR_sempred(RuleContext _localctx, int predIndex) {
      switch(predIndex) {
      case 1:
         return this.tryOperator(FilterInfixOperator.class);
      default:
         return true;
      }
   }

   private boolean POSTFIX_OPERATOR_sempred(RuleContext _localctx, int predIndex) {
      switch(predIndex) {
      case 2:
         return this.tryOperator(FilterPostfixOperator.class);
      default:
         return true;
      }
   }

   static {
      RuntimeMetaData.checkVersion("4.13.1", "4.13.1");
      _sharedContextCache = new PredictionContextCache();
      channelNames = new String[]{"DEFAULT_TOKEN_CHANNEL", "HIDDEN"};
      modeNames = new String[]{"DEFAULT_MODE"};
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

      _ATN = (new ATNDeserializer()).deserialize("\u0004\u0000\u0011{\u0006\uffff\uffff\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b\u0002\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002\u000f\u0007\u000f\u0002\u0010\u0007\u0010\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0003\u00035\b\u0003\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0003\u0004A\b\u0004\u0001\u0005\u0001\u0005\u0001\u0006\u0001\u0006\u0001\u0007\u0001\u0007\u0001\b\u0001\b\u0001\t\u0001\t\u0001\n\u0001\n\u0001\u000b\u0001\u000b\u0001\f\u0001\f\u0005\fS\b\f\n\f\f\fV\t\f\u0001\r\u0003\rY\b\r\u0001\r\u0004\r\\\b\r\u000b\r\f\r]\u0001\r\u0001\r\u0004\rb\b\r\u000b\r\f\rc\u0003\rf\b\r\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0005\u000el\b\u000e\n\u000e\f\u000eo\t\u000e\u0001\u000e\u0001\u000e\u0001\u000f\u0004\u000ft\b\u000f\u000b\u000f\f\u000fu\u0001\u000f\u0001\u000f\u0001\u0010\u0001\u0010\u0000\u0000\u0011\u0001\u0001\u0003\u0002\u0005\u0003\u0007\u0004\t\u0005\u000b\u0006\r\u0007\u000f\b\u0011\t\u0013\n\u0015\u000b\u0017\f\u0019\r\u001b\u000e\u001d\u000f\u001f\u0010!\u0011\u0001\u0000\u0005\u0004\u0000$$AZ__az\u0005\u0000$$09AZ__az\u0001\u000009\u0002\u0000''\\\\\u0002\u0000\t\t  \u0084\u0000\u0001\u0001\u0000\u0000\u0000\u0000\u0003\u0001\u0000\u0000\u0000\u0000\u0005\u0001\u0000\u0000\u0000\u0000\u0007\u0001\u0000\u0000\u0000\u0000\t\u0001\u0000\u0000\u0000\u0000\u000b\u0001\u0000\u0000\u0000\u0000\r\u0001\u0000\u0000\u0000\u0000\u000f\u0001\u0000\u0000\u0000\u0000\u0011\u0001\u0000\u0000\u0000\u0000\u0013\u0001\u0000\u0000\u0000\u0000\u0015\u0001\u0000\u0000\u0000\u0000\u0017\u0001\u0000\u0000\u0000\u0000\u0019\u0001\u0000\u0000\u0000\u0000\u001b\u0001\u0000\u0000\u0000\u0000\u001d\u0001\u0000\u0000\u0000\u0000\u001f\u0001\u0000\u0000\u0000\u0000!\u0001\u0000\u0000\u0000\u0001#\u0001\u0000\u0000\u0000\u0003&\u0001\u0000\u0000\u0000\u0005)\u0001\u0000\u0000\u0000\u00074\u0001\u0000\u0000\u0000\t@\u0001\u0000\u0000\u0000\u000bB\u0001\u0000\u0000\u0000\rD\u0001\u0000\u0000\u0000\u000fF\u0001\u0000\u0000\u0000\u0011H\u0001\u0000\u0000\u0000\u0013J\u0001\u0000\u0000\u0000\u0015L\u0001\u0000\u0000\u0000\u0017N\u0001\u0000\u0000\u0000\u0019P\u0001\u0000\u0000\u0000\u001bX\u0001\u0000\u0000\u0000\u001dg\u0001\u0000\u0000\u0000\u001fs\u0001\u0000\u0000\u0000!y\u0001\u0000\u0000\u0000#$\u0004\u0000\u0000\u0000$%\t\u0000\u0000\u0000%\u0002\u0001\u0000\u0000\u0000&'\u0004\u0001\u0001\u0000'(\t\u0000\u0000\u0000(\u0004\u0001\u0000\u0000\u0000)*\u0004\u0002\u0002\u0000*+\t\u0000\u0000\u0000+\u0006\u0001\u0000\u0000\u0000,-\u0005T\u0000\u0000-.\u0005R\u0000\u0000./\u0005U\u0000\u0000/5\u0005E\u0000\u000001\u0005t\u0000\u000012\u0005r\u0000\u000023\u0005u\u0000\u000035\u0005e\u0000\u00004,\u0001\u0000\u0000\u000040\u0001\u0000\u0000\u00005\b\u0001\u0000\u0000\u000067\u0005F\u0000\u000078\u0005A\u0000\u000089\u0005L\u0000\u00009:\u0005S\u0000\u0000:A\u0005E\u0000\u0000;<\u0005f\u0000\u0000<=\u0005a\u0000\u0000=>\u0005l\u0000\u0000>?\u0005s\u0000\u0000?A\u0005e\u0000\u0000@6\u0001\u0000\u0000\u0000@;\u0001\u0000\u0000\u0000A\n\u0001\u0000\u0000\u0000BC\u0005.\u0000\u0000C\f\u0001\u0000\u0000\u0000DE\u0005,\u0000\u0000E\u000e\u0001\u0000\u0000\u0000FG\u0005(\u0000\u0000G\u0010\u0001\u0000\u0000\u0000HI\u0005)\u0000\u0000I\u0012\u0001\u0000\u0000\u0000JK\u0005[\u0000\u0000K\u0014\u0001\u0000\u0000\u0000LM\u0005]\u0000\u0000M\u0016\u0001\u0000\u0000\u0000NO\u0005`\u0000\u0000O\u0018\u0001\u0000\u0000\u0000PT\u0007\u0000\u0000\u0000QS\u0007\u0001\u0000\u0000RQ\u0001\u0000\u0000\u0000SV\u0001\u0000\u0000\u0000TR\u0001\u0000\u0000\u0000TU\u0001\u0000\u0000\u0000U\u001a\u0001\u0000\u0000\u0000VT\u0001\u0000\u0000\u0000WY\u0005-\u0000\u0000XW\u0001\u0000\u0000\u0000XY\u0001\u0000\u0000\u0000Y[\u0001\u0000\u0000\u0000Z\\\u0007\u0002\u0000\u0000[Z\u0001\u0000\u0000\u0000\\]\u0001\u0000\u0000\u0000][\u0001\u0000\u0000\u0000]^\u0001\u0000\u0000\u0000^e\u0001\u0000\u0000\u0000_a\u0005.\u0000\u0000`b\u0007\u0002\u0000\u0000a`\u0001\u0000\u0000\u0000bc\u0001\u0000\u0000\u0000ca\u0001\u0000\u0000\u0000cd\u0001\u0000\u0000\u0000df\u0001\u0000\u0000\u0000e_\u0001\u0000\u0000\u0000ef\u0001\u0000\u0000\u0000f\u001c\u0001\u0000\u0000\u0000gm\u0005'\u0000\u0000hl\b\u0003\u0000\u0000ij\u0005\\\u0000\u0000jl\u0007\u0003\u0000\u0000kh\u0001\u0000\u0000\u0000ki\u0001\u0000\u0000\u0000lo\u0001\u0000\u0000\u0000mk\u0001\u0000\u0000\u0000mn\u0001\u0000\u0000\u0000np\u0001\u0000\u0000\u0000om\u0001\u0000\u0000\u0000pq\u0005'\u0000\u0000q\u001e\u0001\u0000\u0000\u0000rt\u0007\u0004\u0000\u0000sr\u0001\u0000\u0000\u0000tu\u0001\u0000\u0000\u0000us\u0001\u0000\u0000\u0000uv\u0001\u0000\u0000\u0000vw\u0001\u0000\u0000\u0000wx\u0006\u000f\u0000\u0000x \u0001\u0000\u0000\u0000yz\t\u0000\u0000\u0000z\"\u0001\u0000\u0000\u0000\u000b\u00004@TX]cekmu\u0001\u0006\u0000\u0000".toCharArray());
      _decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];

      for(i = 0; i < _ATN.getNumberOfDecisions(); ++i) {
         _decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
      }

   }

   private static enum OperatorType {
      PREFIX_OPERATOR,
      INFIX_OPERATOR,
      POSTFIX_OPERATOR;

      // $FF: synthetic method
      private static OperatorType[] $values() {
         return new OperatorType[]{PREFIX_OPERATOR, INFIX_OPERATOR, POSTFIX_OPERATOR};
      }
   }
}
