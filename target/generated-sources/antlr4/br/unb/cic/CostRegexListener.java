// Generated from br/unb/cic/CostRegex.g4 by ANTLR 4.3
package br.unb.cic;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link CostRegexParser}.
 */
public interface CostRegexListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by the {@code blank}
	 * labeled alternative in {@link CostRegexParser#cost}.
	 * @param ctx the parse tree
	 */
	void enterBlank(@NotNull CostRegexParser.BlankContext ctx);
	/**
	 * Exit a parse tree produced by the {@code blank}
	 * labeled alternative in {@link CostRegexParser#cost}.
	 * @param ctx the parse tree
	 */
	void exitBlank(@NotNull CostRegexParser.BlankContext ctx);

	/**
	 * Enter a parse tree produced by the {@code gVariable}
	 * labeled alternative in {@link CostRegexParser#cost}.
	 * @param ctx the parse tree
	 */
	void enterGVariable(@NotNull CostRegexParser.GVariableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code gVariable}
	 * labeled alternative in {@link CostRegexParser#cost}.
	 * @param ctx the parse tree
	 */
	void exitGVariable(@NotNull CostRegexParser.GVariableContext ctx);

	/**
	 * Enter a parse tree produced by the {@code gFloat}
	 * labeled alternative in {@link CostRegexParser#cost}.
	 * @param ctx the parse tree
	 */
	void enterGFloat(@NotNull CostRegexParser.GFloatContext ctx);
	/**
	 * Exit a parse tree produced by the {@code gFloat}
	 * labeled alternative in {@link CostRegexParser#cost}.
	 * @param ctx the parse tree
	 */
	void exitGFloat(@NotNull CostRegexParser.GFloatContext ctx);

	/**
	 * Enter a parse tree produced by the {@code gExpression}
	 * labeled alternative in {@link CostRegexParser#cost}.
	 * @param ctx the parse tree
	 */
	void enterGExpression(@NotNull CostRegexParser.GExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code gExpression}
	 * labeled alternative in {@link CostRegexParser#cost}.
	 * @param ctx the parse tree
	 */
	void exitGExpression(@NotNull CostRegexParser.GExpressionContext ctx);
}