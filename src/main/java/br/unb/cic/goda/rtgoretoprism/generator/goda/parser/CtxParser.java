package br.unb.cic.goda.rtgoretoprism.generator.goda.parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.antlr.v4.runtime.tree.ParseTree;

import br.unb.cic.CtxRegexBaseVisitor;
import br.unb.cic.CtxRegexLexer;
import br.unb.cic.CtxRegexParser;
import br.unb.cic.CtxRegexParser.CAndContext;
import br.unb.cic.CtxRegexParser.CBoolContext;
import br.unb.cic.CtxRegexParser.CDIFFContext;
import br.unb.cic.CtxRegexParser.CEQContext;
import br.unb.cic.CtxRegexParser.CFloatContext;
import br.unb.cic.CtxRegexParser.CGEContext;
import br.unb.cic.CtxRegexParser.CGTContext;
import br.unb.cic.CtxRegexParser.CIntContext;
import br.unb.cic.CtxRegexParser.CLEContext;
import br.unb.cic.CtxRegexParser.CLTContext;
import br.unb.cic.CtxRegexParser.CNumContext;
import br.unb.cic.CtxRegexParser.COrContext;
import br.unb.cic.CtxRegexParser.CVarContext;
import br.unb.cic.CtxRegexParser.ConditionContext;
import br.unb.cic.CtxRegexParser.PrintExprContext;
import br.unb.cic.CtxRegexParser.TriggerContext;
import br.unb.cic.goda.rtgoretoprism.model.ctx.ContextCondition;
import br.unb.cic.goda.rtgoretoprism.model.ctx.CtxSymbols;

public class CtxParser{
	
//	public static void main (String [] args){
//		try {
//			System.out.println(CtxParser.parseRegex("assertion condition MEMORY<30&PROCESSOR<=80\n"));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
	
	public static Object[] parseRegex(String regex) throws IOException, ParseCancellationException {
		//Reading the DSL script
		InputStream is = new ByteArrayInputStream((regex + '\n').getBytes("UTF-8"));
	    
	    //Loading the DSL  into the ANTLR stream.
	    CharStream cs = new ANTLRInputStream(is);
	    
	    //Passing the input to the lexer to create tokens
	    CtxRegexLexer lexer = new CtxRegexLexer(cs);
	    lexer.removeErrorListeners();
	    lexer.addErrorListener(ThrowingErrorListener.INSTANCE);
	    
	    CommonTokenStream tokens = new CommonTokenStream(lexer);
	    
	    //Passing the tokens to the parser to create the parse trea. 
	    CtxRegexParser parser = new CtxRegexParser(tokens);
	    parser.removeErrorListeners();
	    parser.addErrorListener(ThrowingErrorListener.INSTANCE);
	    
	    ParseTree tree = parser.ctx();
	    CtxFormulaParserVisitor CtxRegexVisitor = new CtxFormulaParserVisitor();
	    
	    return new Object[]{CtxRegexVisitor.memory, CtxRegexVisitor.visit(tree), CtxRegexVisitor.type};
	}
}

class CtxFormulaParserVisitor extends  CtxRegexBaseVisitor<String> {

	Set<String[]> ctxVars = new HashSet<String[]>();
	List<ContextCondition> memory = new ArrayList<ContextCondition>();
	CtxSymbols type = null;
	
	@Override
	public String visitPrintExpr(PrintExprContext ctx) {
		return visit(ctx.ctx());		
	}
	
	@Override
	public String visitCondition(ConditionContext ctx) {
		type = CtxSymbols.COND;
		return visit(ctx.expr());
	}
	
	@Override
	public String visitTrigger(TriggerContext ctx) {
		type = CtxSymbols.TRIG;
		return visit(ctx.expr());
	}
	
	@Override
	public String visitCVar(CVarContext ctx) {
		
		String var = ctx.VAR().getText();
		
		if(ctx.getParent() instanceof CAndContext ||
		   ctx.getParent() instanceof COrContext ||
		   ctx.getParent() instanceof ConditionContext ||
		   ctx.getParent() instanceof TriggerContext){
			ctxVars.add(new String[]{var, "bool"});
			memory.add(new ContextCondition(var, CtxSymbols.BOOL,  CtxSymbols.BOOL, "true"));
		}
		return var;
	}
	
	@Override
	public String visitCNum(CNumContext ctx) {
		return ctx.getText();
	}
	
	@Override
	public String visitCEQ(CEQContext ctx) {
		String var = visit(ctx.expr());
		String value = ctx.value().getChild(0).getText();
		CtxSymbols type = checkTypeVar(var, value);

		memory.add(new ContextCondition(var, CtxSymbols.EQ,  type, value));
		return var + " = " + value;
	}
	
	@Override
	public String visitCDIFF(CDIFFContext ctx) {
		String var = visit(ctx.expr());
		String value = ctx.value().getChild(0).getText();
		CtxSymbols type = checkTypeVar(var, value);

		memory.add(new ContextCondition(var, CtxSymbols.DIFF, type, value));
		return var + " != " + value;
	}
	
	private CtxSymbols checkTypeVar(String var, String value) {

		if(ctxVars.contains(var)){
			return CtxSymbols.BOOL;
		}
		else if(value.equals("true") || value.equals("false")){
			ctxVars.add(new String[]{var, "bool"});
			return CtxSymbols.BOOL;
		}else if (value.contains(".")){
			ctxVars.add(new String[]{var, "double"});
			return CtxSymbols.DOUBLE;
		}else{
			ctxVars.add(new String[]{var, "int"});
			return CtxSymbols.INT;
		}
	}

	@Override
	public String visitCLE(CLEContext ctx) {
		String var = visit(ctx.expr());
		String value = visit(ctx.num());
		CtxSymbols type = checkTypeVar(var, value);

		memory.add(new ContextCondition(var, CtxSymbols.LE, type, value));
		return var + " <= " + value;
	}
	
	@Override
	public String visitCLT(CLTContext ctx) {
		String var = visit(ctx.expr());
		String value = visit(ctx.num());
		CtxSymbols type = checkTypeVar(var, value);

		memory.add(new ContextCondition(var, CtxSymbols.LT, type, value));
		return var + " < " + value;
	}
	
	@Override
	public String visitCGE(CGEContext ctx) {
		String var = visit(ctx.expr());
		String value = visit(ctx.num());
		CtxSymbols type = checkTypeVar(var, value);

		memory.add(new ContextCondition(var, CtxSymbols.GE, type, value));
		return var + " >= " + value;
	}
	
	@Override
	public String visitCGT(CGTContext ctx) {
		String var = visit(ctx.expr());
		String value = visit(ctx.num());
		CtxSymbols type = checkTypeVar(var, value);

		memory.add(new ContextCondition(var, CtxSymbols.GT, type, value));
		return var + " > " + value;
	}
	
	@Override
	public String visitCAnd(CAndContext ctx) {
		String varA = visit(ctx.expr(0));
		String varB = visit(ctx.expr(1));
		return varA + " & " + varB;
	}
	
	@Override
	public String visitCOr(COrContext ctx) {
		String varA = visit(ctx.expr(0));
		String varB = visit(ctx.expr(1));
		return varA + " | " + varB;
	}
	
	@Override
	public String visitCBool(CBoolContext ctx) {		
		return ctx.BOOL().getText();
	}
	
	@Override
	public String visitCFloat(CFloatContext ctx) {		
		return ctx.FLOAT().getText();
	}
	
	@Override
	public String visitCInt(CIntContext ctx){
		return ctx.INT().getText();
	}
	
	private CtxSymbols getEffectType(ParserRuleContext ctx){
		if(ctx instanceof ConditionContext)
			return CtxSymbols.COND;
		else// if(ctx instanceof TriggerContext)
			return CtxSymbols.TRIG;
	}
	
	private CtxSymbols parseSymbol(String op){
		for(CtxSymbols symbol : CtxSymbols.values())
			if(symbol.toString().equals(op))
				return symbol;
		return null;
	}
}