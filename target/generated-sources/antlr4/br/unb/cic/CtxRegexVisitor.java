// Generated from br/unb/cic/CtxRegex.g4 by ANTLR 4.3
package br.unb.cic;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link CtxRegexParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface CtxRegexVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by the {@code cGE}
	 * labeled alternative in {@link CtxRegexParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCGE(@NotNull CtxRegexParser.CGEContext ctx);

	/**
	 * Visit a parse tree produced by the {@code cInt}
	 * labeled alternative in {@link CtxRegexParser#num}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCInt(@NotNull CtxRegexParser.CIntContext ctx);

	/**
	 * Visit a parse tree produced by the {@code blank}
	 * labeled alternative in {@link CtxRegexParser#ctx}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlank(@NotNull CtxRegexParser.BlankContext ctx);

	/**
	 * Visit a parse tree produced by the {@code cFloat}
	 * labeled alternative in {@link CtxRegexParser#num}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCFloat(@NotNull CtxRegexParser.CFloatContext ctx);

	/**
	 * Visit a parse tree produced by the {@code cOr}
	 * labeled alternative in {@link CtxRegexParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCOr(@NotNull CtxRegexParser.COrContext ctx);

	/**
	 * Visit a parse tree produced by the {@code cVar}
	 * labeled alternative in {@link CtxRegexParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCVar(@NotNull CtxRegexParser.CVarContext ctx);

	/**
	 * Visit a parse tree produced by the {@code cLT}
	 * labeled alternative in {@link CtxRegexParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCLT(@NotNull CtxRegexParser.CLTContext ctx);

	/**
	 * Visit a parse tree produced by the {@code trigger}
	 * labeled alternative in {@link CtxRegexParser#ctx}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTrigger(@NotNull CtxRegexParser.TriggerContext ctx);

	/**
	 * Visit a parse tree produced by the {@code cNum}
	 * labeled alternative in {@link CtxRegexParser#value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCNum(@NotNull CtxRegexParser.CNumContext ctx);

	/**
	 * Visit a parse tree produced by the {@code cEQ}
	 * labeled alternative in {@link CtxRegexParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCEQ(@NotNull CtxRegexParser.CEQContext ctx);

	/**
	 * Visit a parse tree produced by the {@code cGT}
	 * labeled alternative in {@link CtxRegexParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCGT(@NotNull CtxRegexParser.CGTContext ctx);

	/**
	 * Visit a parse tree produced by the {@code condition}
	 * labeled alternative in {@link CtxRegexParser#ctx}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCondition(@NotNull CtxRegexParser.ConditionContext ctx);

	/**
	 * Visit a parse tree produced by the {@code cParens}
	 * labeled alternative in {@link CtxRegexParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCParens(@NotNull CtxRegexParser.CParensContext ctx);

	/**
	 * Visit a parse tree produced by the {@code cAnd}
	 * labeled alternative in {@link CtxRegexParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCAnd(@NotNull CtxRegexParser.CAndContext ctx);

	/**
	 * Visit a parse tree produced by the {@code cDIFF}
	 * labeled alternative in {@link CtxRegexParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCDIFF(@NotNull CtxRegexParser.CDIFFContext ctx);

	/**
	 * Visit a parse tree produced by the {@code cLE}
	 * labeled alternative in {@link CtxRegexParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCLE(@NotNull CtxRegexParser.CLEContext ctx);

	/**
	 * Visit a parse tree produced by the {@code printExpr}
	 * labeled alternative in {@link CtxRegexParser#ctx}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrintExpr(@NotNull CtxRegexParser.PrintExprContext ctx);

	/**
	 * Visit a parse tree produced by the {@code cBool}
	 * labeled alternative in {@link CtxRegexParser#value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCBool(@NotNull CtxRegexParser.CBoolContext ctx);
}