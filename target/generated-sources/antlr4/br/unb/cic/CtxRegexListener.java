// Generated from br/unb/cic/CtxRegex.g4 by ANTLR 4.3
package br.unb.cic;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link CtxRegexParser}.
 */
public interface CtxRegexListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by the {@code cGE}
	 * labeled alternative in {@link CtxRegexParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterCGE(@NotNull CtxRegexParser.CGEContext ctx);
	/**
	 * Exit a parse tree produced by the {@code cGE}
	 * labeled alternative in {@link CtxRegexParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitCGE(@NotNull CtxRegexParser.CGEContext ctx);

	/**
	 * Enter a parse tree produced by the {@code cInt}
	 * labeled alternative in {@link CtxRegexParser#num}.
	 * @param ctx the parse tree
	 */
	void enterCInt(@NotNull CtxRegexParser.CIntContext ctx);
	/**
	 * Exit a parse tree produced by the {@code cInt}
	 * labeled alternative in {@link CtxRegexParser#num}.
	 * @param ctx the parse tree
	 */
	void exitCInt(@NotNull CtxRegexParser.CIntContext ctx);

	/**
	 * Enter a parse tree produced by the {@code blank}
	 * labeled alternative in {@link CtxRegexParser#ctx}.
	 * @param ctx the parse tree
	 */
	void enterBlank(@NotNull CtxRegexParser.BlankContext ctx);
	/**
	 * Exit a parse tree produced by the {@code blank}
	 * labeled alternative in {@link CtxRegexParser#ctx}.
	 * @param ctx the parse tree
	 */
	void exitBlank(@NotNull CtxRegexParser.BlankContext ctx);

	/**
	 * Enter a parse tree produced by the {@code cFloat}
	 * labeled alternative in {@link CtxRegexParser#num}.
	 * @param ctx the parse tree
	 */
	void enterCFloat(@NotNull CtxRegexParser.CFloatContext ctx);
	/**
	 * Exit a parse tree produced by the {@code cFloat}
	 * labeled alternative in {@link CtxRegexParser#num}.
	 * @param ctx the parse tree
	 */
	void exitCFloat(@NotNull CtxRegexParser.CFloatContext ctx);

	/**
	 * Enter a parse tree produced by the {@code cOr}
	 * labeled alternative in {@link CtxRegexParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterCOr(@NotNull CtxRegexParser.COrContext ctx);
	/**
	 * Exit a parse tree produced by the {@code cOr}
	 * labeled alternative in {@link CtxRegexParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitCOr(@NotNull CtxRegexParser.COrContext ctx);

	/**
	 * Enter a parse tree produced by the {@code cVar}
	 * labeled alternative in {@link CtxRegexParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterCVar(@NotNull CtxRegexParser.CVarContext ctx);
	/**
	 * Exit a parse tree produced by the {@code cVar}
	 * labeled alternative in {@link CtxRegexParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitCVar(@NotNull CtxRegexParser.CVarContext ctx);

	/**
	 * Enter a parse tree produced by the {@code cLT}
	 * labeled alternative in {@link CtxRegexParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterCLT(@NotNull CtxRegexParser.CLTContext ctx);
	/**
	 * Exit a parse tree produced by the {@code cLT}
	 * labeled alternative in {@link CtxRegexParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitCLT(@NotNull CtxRegexParser.CLTContext ctx);

	/**
	 * Enter a parse tree produced by the {@code trigger}
	 * labeled alternative in {@link CtxRegexParser#ctx}.
	 * @param ctx the parse tree
	 */
	void enterTrigger(@NotNull CtxRegexParser.TriggerContext ctx);
	/**
	 * Exit a parse tree produced by the {@code trigger}
	 * labeled alternative in {@link CtxRegexParser#ctx}.
	 * @param ctx the parse tree
	 */
	void exitTrigger(@NotNull CtxRegexParser.TriggerContext ctx);

	/**
	 * Enter a parse tree produced by the {@code cNum}
	 * labeled alternative in {@link CtxRegexParser#value}.
	 * @param ctx the parse tree
	 */
	void enterCNum(@NotNull CtxRegexParser.CNumContext ctx);
	/**
	 * Exit a parse tree produced by the {@code cNum}
	 * labeled alternative in {@link CtxRegexParser#value}.
	 * @param ctx the parse tree
	 */
	void exitCNum(@NotNull CtxRegexParser.CNumContext ctx);

	/**
	 * Enter a parse tree produced by the {@code cEQ}
	 * labeled alternative in {@link CtxRegexParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterCEQ(@NotNull CtxRegexParser.CEQContext ctx);
	/**
	 * Exit a parse tree produced by the {@code cEQ}
	 * labeled alternative in {@link CtxRegexParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitCEQ(@NotNull CtxRegexParser.CEQContext ctx);

	/**
	 * Enter a parse tree produced by the {@code cGT}
	 * labeled alternative in {@link CtxRegexParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterCGT(@NotNull CtxRegexParser.CGTContext ctx);
	/**
	 * Exit a parse tree produced by the {@code cGT}
	 * labeled alternative in {@link CtxRegexParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitCGT(@NotNull CtxRegexParser.CGTContext ctx);

	/**
	 * Enter a parse tree produced by the {@code condition}
	 * labeled alternative in {@link CtxRegexParser#ctx}.
	 * @param ctx the parse tree
	 */
	void enterCondition(@NotNull CtxRegexParser.ConditionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code condition}
	 * labeled alternative in {@link CtxRegexParser#ctx}.
	 * @param ctx the parse tree
	 */
	void exitCondition(@NotNull CtxRegexParser.ConditionContext ctx);

	/**
	 * Enter a parse tree produced by the {@code cParens}
	 * labeled alternative in {@link CtxRegexParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterCParens(@NotNull CtxRegexParser.CParensContext ctx);
	/**
	 * Exit a parse tree produced by the {@code cParens}
	 * labeled alternative in {@link CtxRegexParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitCParens(@NotNull CtxRegexParser.CParensContext ctx);

	/**
	 * Enter a parse tree produced by the {@code cAnd}
	 * labeled alternative in {@link CtxRegexParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterCAnd(@NotNull CtxRegexParser.CAndContext ctx);
	/**
	 * Exit a parse tree produced by the {@code cAnd}
	 * labeled alternative in {@link CtxRegexParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitCAnd(@NotNull CtxRegexParser.CAndContext ctx);

	/**
	 * Enter a parse tree produced by the {@code cDIFF}
	 * labeled alternative in {@link CtxRegexParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterCDIFF(@NotNull CtxRegexParser.CDIFFContext ctx);
	/**
	 * Exit a parse tree produced by the {@code cDIFF}
	 * labeled alternative in {@link CtxRegexParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitCDIFF(@NotNull CtxRegexParser.CDIFFContext ctx);

	/**
	 * Enter a parse tree produced by the {@code cLE}
	 * labeled alternative in {@link CtxRegexParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterCLE(@NotNull CtxRegexParser.CLEContext ctx);
	/**
	 * Exit a parse tree produced by the {@code cLE}
	 * labeled alternative in {@link CtxRegexParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitCLE(@NotNull CtxRegexParser.CLEContext ctx);

	/**
	 * Enter a parse tree produced by the {@code printExpr}
	 * labeled alternative in {@link CtxRegexParser#ctx}.
	 * @param ctx the parse tree
	 */
	void enterPrintExpr(@NotNull CtxRegexParser.PrintExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code printExpr}
	 * labeled alternative in {@link CtxRegexParser#ctx}.
	 * @param ctx the parse tree
	 */
	void exitPrintExpr(@NotNull CtxRegexParser.PrintExprContext ctx);

	/**
	 * Enter a parse tree produced by the {@code cBool}
	 * labeled alternative in {@link CtxRegexParser#value}.
	 * @param ctx the parse tree
	 */
	void enterCBool(@NotNull CtxRegexParser.CBoolContext ctx);
	/**
	 * Exit a parse tree produced by the {@code cBool}
	 * labeled alternative in {@link CtxRegexParser#value}.
	 * @param ctx the parse tree
	 */
	void exitCBool(@NotNull CtxRegexParser.CBoolContext ctx);
}