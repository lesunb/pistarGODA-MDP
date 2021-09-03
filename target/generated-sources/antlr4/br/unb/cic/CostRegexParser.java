// Generated from br/unb/cic/CostRegex.g4 by ANTLR 4.3
package br.unb.cic;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class CostRegexParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.3", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__1=1, T__0=2, FLOAT=3, VAR=4, NEWLINE=5, WS=6;
	public static final String[] tokenNames = {
		"<INVALID>", "'W'", "'='", "FLOAT", "VAR", "NEWLINE", "WS"
	};
	public static final int
		RULE_cost = 0;
	public static final String[] ruleNames = {
		"cost"
	};

	@Override
	public String getGrammarFileName() { return "CostRegex.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public CostRegexParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class CostContext extends ParserRuleContext {
		public CostContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cost; }
	 
		public CostContext() { }
		public void copyFrom(CostContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class BlankContext extends CostContext {
		public TerminalNode NEWLINE() { return getToken(CostRegexParser.NEWLINE, 0); }
		public BlankContext(CostContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CostRegexListener ) ((CostRegexListener)listener).enterBlank(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CostRegexListener ) ((CostRegexListener)listener).exitBlank(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CostRegexVisitor ) return ((CostRegexVisitor<? extends T>)visitor).visitBlank(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class GVariableContext extends CostContext {
		public Token op;
		public TerminalNode VAR() { return getToken(CostRegexParser.VAR, 0); }
		public GVariableContext(CostContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CostRegexListener ) ((CostRegexListener)listener).enterGVariable(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CostRegexListener ) ((CostRegexListener)listener).exitGVariable(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CostRegexVisitor ) return ((CostRegexVisitor<? extends T>)visitor).visitGVariable(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class GFloatContext extends CostContext {
		public Token op;
		public TerminalNode FLOAT() { return getToken(CostRegexParser.FLOAT, 0); }
		public GFloatContext(CostContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CostRegexListener ) ((CostRegexListener)listener).enterGFloat(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CostRegexListener ) ((CostRegexListener)listener).exitGFloat(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CostRegexVisitor ) return ((CostRegexVisitor<? extends T>)visitor).visitGFloat(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class GExpressionContext extends CostContext {
		public Token op;
		public TerminalNode VAR() { return getToken(CostRegexParser.VAR, 0); }
		public TerminalNode FLOAT() { return getToken(CostRegexParser.FLOAT, 0); }
		public GExpressionContext(CostContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CostRegexListener ) ((CostRegexListener)listener).enterGExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CostRegexListener ) ((CostRegexListener)listener).exitGExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CostRegexVisitor ) return ((CostRegexVisitor<? extends T>)visitor).visitGExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CostContext cost() throws RecognitionException {
		CostContext _localctx = new CostContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_cost);
		try {
			setState(13);
			switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
			case 1:
				_localctx = new GFloatContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(2); match(T__1);
				setState(3); ((GFloatContext)_localctx).op = match(T__0);
				setState(4); match(FLOAT);
				}
				break;

			case 2:
				_localctx = new GExpressionContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(5); match(T__1);
				setState(6); ((GExpressionContext)_localctx).op = match(T__0);
				setState(7); match(FLOAT);
				setState(8); match(VAR);
				}
				break;

			case 3:
				_localctx = new GVariableContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(9); match(T__1);
				setState(10); ((GVariableContext)_localctx).op = match(T__0);
				setState(11); match(VAR);
				}
				break;

			case 4:
				_localctx = new BlankContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(12); match(NEWLINE);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\b\22\4\2\t\2\3\2"+
		"\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\5\2\20\n\2\3\2\2\2\3\2\2\2\23"+
		"\2\17\3\2\2\2\4\5\7\3\2\2\5\6\7\4\2\2\6\20\7\5\2\2\7\b\7\3\2\2\b\t\7\4"+
		"\2\2\t\n\7\5\2\2\n\20\7\6\2\2\13\f\7\3\2\2\f\r\7\4\2\2\r\20\7\6\2\2\16"+
		"\20\7\7\2\2\17\4\3\2\2\2\17\7\3\2\2\2\17\13\3\2\2\2\17\16\3\2\2\2\20\3"+
		"\3\2\2\2\3\17";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}