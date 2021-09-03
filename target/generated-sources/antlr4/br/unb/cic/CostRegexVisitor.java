// Generated from br/unb/cic/CostRegex.g4 by ANTLR 4.3
package br.unb.cic;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link CostRegexParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface CostRegexVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by the {@code blank}
	 * labeled alternative in {@link CostRegexParser#cost}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlank(@NotNull CostRegexParser.BlankContext ctx);

	/**
	 * Visit a parse tree produced by the {@code gVariable}
	 * labeled alternative in {@link CostRegexParser#cost}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGVariable(@NotNull CostRegexParser.GVariableContext ctx);

	/**
	 * Visit a parse tree produced by the {@code gFloat}
	 * labeled alternative in {@link CostRegexParser#cost}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGFloat(@NotNull CostRegexParser.GFloatContext ctx);

	/**
	 * Visit a parse tree produced by the {@code gExpression}
	 * labeled alternative in {@link CostRegexParser#cost}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGExpression(@NotNull CostRegexParser.GExpressionContext ctx);
}