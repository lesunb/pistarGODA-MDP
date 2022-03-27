// Generated from br/unb/cic/RTRegex.g4 by ANTLR 4.3
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
public class RTRegexLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.3", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__6=1, T__5=2, T__4=3, T__3=4, T__2=5, T__1=6, T__0=7, FLOAT=8, SEQ=9, 
		INT=10, TASK=11, GOAL=12, SKIPP=13, X=14, NEWLINE=15, WS=16;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] tokenNames = {
		"'\\u0000'", "'\\u0001'", "'\\u0002'", "'\\u0003'", "'\\u0004'", "'\\u0005'", 
		"'\\u0006'", "'\\u0007'", "'\b'", "'\t'", "'\n'", "'\\u000B'", "'\f'", 
		"'\r'", "'\\u000E'", "'\\u000F'", "'\\u0010'"
	};
	public static final String[] ruleNames = {
		"T__6", "T__5", "T__4", "T__3", "T__2", "T__1", "T__0", "FLOAT", "SEQ", 
		"INT", "TASK", "GOAL", "SKIPP", "X", "NEWLINE", "WS", "DIGIT"
	};


	public RTRegexLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "RTRegex.g4"; }

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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\22c\b\1\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\3\2\3\2\3\3\3\3\3\4\3\4\3\4\3\4\3\5\3\5\3\5\3\5\3\5\3\6\3\6\3\7\3\7\3"+
		"\b\3\b\3\t\6\t:\n\t\r\t\16\t;\3\t\5\t?\n\t\3\t\7\tB\n\t\f\t\16\tE\13\t"+
		"\3\n\3\n\3\13\3\13\3\f\3\f\3\r\3\r\3\16\3\16\3\16\3\16\3\16\3\17\3\17"+
		"\3\20\6\20W\n\20\r\20\16\20X\3\21\6\21\\\n\21\r\21\16\21]\3\21\3\21\3"+
		"\22\3\22\2\2\23\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31"+
		"\16\33\17\35\20\37\21!\22#\2\3\2\5\4\2\f\f\17\17\3\2\13\13\3\2\62;f\2"+
		"\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2"+
		"\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2"+
		"\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\3%\3\2\2"+
		"\2\5\'\3\2\2\2\7)\3\2\2\2\t-\3\2\2\2\13\62\3\2\2\2\r\64\3\2\2\2\17\66"+
		"\3\2\2\2\219\3\2\2\2\23F\3\2\2\2\25H\3\2\2\2\27J\3\2\2\2\31L\3\2\2\2\33"+
		"N\3\2\2\2\35S\3\2\2\2\37V\3\2\2\2![\3\2\2\2#a\3\2\2\2%&\7A\2\2&\4\3\2"+
		"\2\2\'(\7B\2\2(\6\3\2\2\2)*\7F\2\2*+\7O\2\2+,\7*\2\2,\b\3\2\2\2-.\7v\2"+
		"\2./\7t\2\2/\60\7{\2\2\60\61\7*\2\2\61\n\3\2\2\2\62\63\7+\2\2\63\f\3\2"+
		"\2\2\64\65\7<\2\2\65\16\3\2\2\2\66\67\7.\2\2\67\20\3\2\2\28:\5#\22\29"+
		"8\3\2\2\2:;\3\2\2\2;9\3\2\2\2;<\3\2\2\2<>\3\2\2\2=?\7\60\2\2>=\3\2\2\2"+
		">?\3\2\2\2?C\3\2\2\2@B\5#\22\2A@\3\2\2\2BE\3\2\2\2CA\3\2\2\2CD\3\2\2\2"+
		"D\22\3\2\2\2EC\3\2\2\2FG\7=\2\2G\24\3\2\2\2HI\7%\2\2I\26\3\2\2\2JK\7V"+
		"\2\2K\30\3\2\2\2LM\7I\2\2M\32\3\2\2\2NO\7u\2\2OP\7m\2\2PQ\7k\2\2QR\7r"+
		"\2\2R\34\3\2\2\2ST\7Z\2\2T\36\3\2\2\2UW\t\2\2\2VU\3\2\2\2WX\3\2\2\2XV"+
		"\3\2\2\2XY\3\2\2\2Y \3\2\2\2Z\\\t\3\2\2[Z\3\2\2\2\\]\3\2\2\2][\3\2\2\2"+
		"]^\3\2\2\2^_\3\2\2\2_`\b\21\2\2`\"\3\2\2\2ab\t\4\2\2b$\3\2\2\2\b\2;>C"+
		"X]\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}