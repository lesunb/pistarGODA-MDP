// Generated from br/unb/cic/CostRegex.g4 by ANTLR 4.3
package br.unb.cic;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class CostRegexLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.3", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__1=1, T__0=2, FLOAT=3, VAR=4, NEWLINE=5, WS=6;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] tokenNames = {
		"'\\u0000'", "'\\u0001'", "'\\u0002'", "'\\u0003'", "'\\u0004'", "'\\u0005'", 
		"'\\u0006'"
	};
	public static final String[] ruleNames = {
		"T__1", "T__0", "FLOAT", "VAR", "NEWLINE", "WS", "DIGIT"
	};


	public CostRegexLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "CostRegex.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\b\66\b\1\4\2\t\2"+
		"\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\3\2\3\2\3\3\3\3\3\4\6"+
		"\4\27\n\4\r\4\16\4\30\3\4\5\4\34\n\4\3\4\7\4\37\n\4\f\4\16\4\"\13\4\3"+
		"\5\6\5%\n\5\r\5\16\5&\3\6\6\6*\n\6\r\6\16\6+\3\7\6\7/\n\7\r\7\16\7\60"+
		"\3\7\3\7\3\b\3\b\2\2\t\3\3\5\4\7\5\t\6\13\7\r\b\17\2\3\2\6\5\2C\\aac|"+
		"\4\2\f\f\17\17\3\2\13\13\3\2\62;:\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2"+
		"\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\3\21\3\2\2\2\5\23\3\2\2\2\7\26\3"+
		"\2\2\2\t$\3\2\2\2\13)\3\2\2\2\r.\3\2\2\2\17\64\3\2\2\2\21\22\7Y\2\2\22"+
		"\4\3\2\2\2\23\24\7?\2\2\24\6\3\2\2\2\25\27\5\17\b\2\26\25\3\2\2\2\27\30"+
		"\3\2\2\2\30\26\3\2\2\2\30\31\3\2\2\2\31\33\3\2\2\2\32\34\7\60\2\2\33\32"+
		"\3\2\2\2\33\34\3\2\2\2\34 \3\2\2\2\35\37\5\17\b\2\36\35\3\2\2\2\37\"\3"+
		"\2\2\2 \36\3\2\2\2 !\3\2\2\2!\b\3\2\2\2\" \3\2\2\2#%\t\2\2\2$#\3\2\2\2"+
		"%&\3\2\2\2&$\3\2\2\2&\'\3\2\2\2\'\n\3\2\2\2(*\t\3\2\2)(\3\2\2\2*+\3\2"+
		"\2\2+)\3\2\2\2+,\3\2\2\2,\f\3\2\2\2-/\t\4\2\2.-\3\2\2\2/\60\3\2\2\2\60"+
		".\3\2\2\2\60\61\3\2\2\2\61\62\3\2\2\2\62\63\b\7\2\2\63\16\3\2\2\2\64\65"+
		"\t\5\2\2\65\20\3\2\2\2\t\2\30\33 &+\60\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}