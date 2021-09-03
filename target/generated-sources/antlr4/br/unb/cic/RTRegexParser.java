// Generated from br/unb/cic/RTRegex.g4 by ANTLR 4.3
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
public class RTRegexParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.3", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__6=1, T__5=2, T__4=3, T__3=4, T__2=5, T__1=6, T__0=7, FLOAT=8, SEQ=9, 
		INT=10, TASK=11, GOAL=12, SKIPP=13, X=14, NEWLINE=15, WS=16;
	public static final String[] tokenNames = {
		"<INVALID>", "'?'", "'@'", "'DM('", "'try('", "')'", "':'", "','", "FLOAT", 
		"';'", "'#'", "'T'", "'G'", "'skip'", "'X'", "NEWLINE", "WS"
	};
	public static final int
		RULE_rt = 0, RULE_expr = 1, RULE_id = 2;
	public static final String[] ruleNames = {
		"rt", "expr", "id"
	};

	@Override
	public String getGrammarFileName() { return "RTRegex.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public RTRegexParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class RtContext extends ParserRuleContext {
		public RtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_rt; }
	 
		public RtContext() { }
		public void copyFrom(RtContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class BlankContext extends RtContext {
		public TerminalNode NEWLINE() { return getToken(RTRegexParser.NEWLINE, 0); }
		public BlankContext(RtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RTRegexListener ) ((RTRegexListener)listener).enterBlank(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RTRegexListener ) ((RTRegexListener)listener).exitBlank(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RTRegexVisitor ) return ((RTRegexVisitor<? extends T>)visitor).visitBlank(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class PrintExprContext extends RtContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode NEWLINE() { return getToken(RTRegexParser.NEWLINE, 0); }
		public PrintExprContext(RtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RTRegexListener ) ((RTRegexListener)listener).enterPrintExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RTRegexListener ) ((RTRegexListener)listener).exitPrintExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RTRegexVisitor ) return ((RTRegexVisitor<? extends T>)visitor).visitPrintExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RtContext rt() throws RecognitionException {
		RtContext _localctx = new RtContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_rt);
		try {
			setState(10);
			switch (_input.LA(1)) {
			case T__4:
			case T__3:
			case TASK:
			case GOAL:
			case SKIPP:
				_localctx = new PrintExprContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(6); expr(0);
				setState(7); match(NEWLINE);
				}
				break;
			case NEWLINE:
				_localctx = new BlankContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(9); match(NEWLINE);
				}
				break;
			default:
				throw new NoViableAltException(this);
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

	public static class ExprContext extends ParserRuleContext {
		public ExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expr; }
	 
		public ExprContext() { }
		public void copyFrom(ExprContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class GDMContext extends ExprContext {
		public Token op;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public GDMContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RTRegexListener ) ((RTRegexListener)listener).enterGDM(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RTRegexListener ) ((RTRegexListener)listener).exitGDM(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RTRegexVisitor ) return ((RTRegexVisitor<? extends T>)visitor).visitGDM(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class GIdContext extends ExprContext {
		public Token t;
		public IdContext id() {
			return getRuleContext(IdContext.class,0);
		}
		public GIdContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RTRegexListener ) ((RTRegexListener)listener).enterGId(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RTRegexListener ) ((RTRegexListener)listener).exitGId(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RTRegexVisitor ) return ((RTRegexVisitor<? extends T>)visitor).visitGId(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class GTryContext extends ExprContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public GTryContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RTRegexListener ) ((RTRegexListener)listener).enterGTry(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RTRegexListener ) ((RTRegexListener)listener).exitGTry(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RTRegexVisitor ) return ((RTRegexVisitor<? extends T>)visitor).visitGTry(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class GSkipContext extends ExprContext {
		public TerminalNode SKIPP() { return getToken(RTRegexParser.SKIPP, 0); }
		public GSkipContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RTRegexListener ) ((RTRegexListener)listener).enterGSkip(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RTRegexListener ) ((RTRegexListener)listener).exitGSkip(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RTRegexVisitor ) return ((RTRegexVisitor<? extends T>)visitor).visitGSkip(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class GTimeContext extends ExprContext {
		public Token op;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public GTimeContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RTRegexListener ) ((RTRegexListener)listener).enterGTime(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RTRegexListener ) ((RTRegexListener)listener).exitGTime(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RTRegexVisitor ) return ((RTRegexVisitor<? extends T>)visitor).visitGTime(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class GRetryContext extends ExprContext {
		public Token op;
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode FLOAT() { return getToken(RTRegexParser.FLOAT, 0); }
		public GRetryContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RTRegexListener ) ((RTRegexListener)listener).enterGRetry(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RTRegexListener ) ((RTRegexListener)listener).exitGRetry(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RTRegexVisitor ) return ((RTRegexVisitor<? extends T>)visitor).visitGRetry(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class GDecisionMakingContext extends ExprContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public GDecisionMakingContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RTRegexListener ) ((RTRegexListener)listener).enterGDecisionMaking(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RTRegexListener ) ((RTRegexListener)listener).exitGDecisionMaking(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RTRegexVisitor ) return ((RTRegexVisitor<? extends T>)visitor).visitGDecisionMaking(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExprContext expr() throws RecognitionException {
		return expr(0);
	}

	private ExprContext expr(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExprContext _localctx = new ExprContext(_ctx, _parentState);
		ExprContext _prevctx = _localctx;
		int _startState = 2;
		enterRecursionRule(_localctx, 2, RULE_expr, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(28);
			switch (_input.LA(1)) {
			case TASK:
			case GOAL:
				{
				_localctx = new GIdContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(13);
				((GIdContext)_localctx).t = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==TASK || _la==GOAL) ) {
					((GIdContext)_localctx).t = (Token)_errHandler.recoverInline(this);
				}
				consume();
				setState(14); id();
				}
				break;
			case T__4:
				{
				_localctx = new GDecisionMakingContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(15); match(T__4);
				setState(16); expr(0);
				setState(17); match(T__2);
				}
				break;
			case T__3:
				{
				_localctx = new GTryContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(19); match(T__3);
				setState(20); expr(0);
				setState(21); match(T__2);
				setState(22); match(T__6);
				setState(23); expr(0);
				setState(24); match(T__1);
				setState(25); expr(0);
				}
				break;
			case SKIPP:
				{
				_localctx = new GSkipContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(27); match(SKIPP);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(41);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(39);
					switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
					case 1:
						{
						_localctx = new GDMContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(30);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(31); ((GDMContext)_localctx).op = match(T__0);
						setState(32); expr(6);
						}
						break;

					case 2:
						{
						_localctx = new GTimeContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(33);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(34);
						((GTimeContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==SEQ || _la==INT) ) {
							((GTimeContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						consume();
						setState(35); expr(3);
						}
						break;

					case 3:
						{
						_localctx = new GRetryContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(36);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(37); ((GRetryContext)_localctx).op = match(T__5);
						setState(38); match(FLOAT);
						}
						break;
					}
					} 
				}
				setState(43);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class IdContext extends ParserRuleContext {
		public TerminalNode X() { return getToken(RTRegexParser.X, 0); }
		public TerminalNode FLOAT() { return getToken(RTRegexParser.FLOAT, 0); }
		public IdContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_id; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RTRegexListener ) ((RTRegexListener)listener).enterId(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RTRegexListener ) ((RTRegexListener)listener).exitId(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RTRegexVisitor ) return ((RTRegexVisitor<? extends T>)visitor).visitId(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IdContext id() throws RecognitionException {
		IdContext _localctx = new IdContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_id);
		try {
			setState(48);
			switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(44); match(FLOAT);
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(45); match(FLOAT);
				setState(46); match(X);
				}
				break;

			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(47); match(X);
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

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 1: return expr_sempred((ExprContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expr_sempred(ExprContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0: return precpred(_ctx, 5);

		case 1: return precpred(_ctx, 2);

		case 2: return precpred(_ctx, 4);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\22\65\4\2\t\2\4\3"+
		"\t\3\4\4\t\4\3\2\3\2\3\2\3\2\5\2\r\n\2\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\37\n\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\7\3*\n\3\f\3\16\3-\13\3\3\4\3\4\3\4\3\4\5\4\63\n\4\3\4\2\3"+
		"\4\5\2\4\6\2\4\3\2\r\16\3\2\13\f:\2\f\3\2\2\2\4\36\3\2\2\2\6\62\3\2\2"+
		"\2\b\t\5\4\3\2\t\n\7\21\2\2\n\r\3\2\2\2\13\r\7\21\2\2\f\b\3\2\2\2\f\13"+
		"\3\2\2\2\r\3\3\2\2\2\16\17\b\3\1\2\17\20\t\2\2\2\20\37\5\6\4\2\21\22\7"+
		"\5\2\2\22\23\5\4\3\2\23\24\7\7\2\2\24\37\3\2\2\2\25\26\7\6\2\2\26\27\5"+
		"\4\3\2\27\30\7\7\2\2\30\31\7\3\2\2\31\32\5\4\3\2\32\33\7\b\2\2\33\34\5"+
		"\4\3\2\34\37\3\2\2\2\35\37\7\17\2\2\36\16\3\2\2\2\36\21\3\2\2\2\36\25"+
		"\3\2\2\2\36\35\3\2\2\2\37+\3\2\2\2 !\f\7\2\2!\"\7\t\2\2\"*\5\4\3\b#$\f"+
		"\4\2\2$%\t\3\2\2%*\5\4\3\5&\'\f\6\2\2\'(\7\4\2\2(*\7\n\2\2) \3\2\2\2)"+
		"#\3\2\2\2)&\3\2\2\2*-\3\2\2\2+)\3\2\2\2+,\3\2\2\2,\5\3\2\2\2-+\3\2\2\2"+
		".\63\7\n\2\2/\60\7\n\2\2\60\63\7\20\2\2\61\63\7\20\2\2\62.\3\2\2\2\62"+
		"/\3\2\2\2\62\61\3\2\2\2\63\7\3\2\2\2\7\f\36)+\62";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}