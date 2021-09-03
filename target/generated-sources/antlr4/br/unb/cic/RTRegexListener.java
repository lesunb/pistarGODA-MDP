// Generated from br/unb/cic/RTRegex.g4 by ANTLR 4.3
package br.unb.cic;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link RTRegexParser}.
 */
public interface RTRegexListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by the {@code gDM}
	 * labeled alternative in {@link RTRegexParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterGDM(@NotNull RTRegexParser.GDMContext ctx);
	/**
	 * Exit a parse tree produced by the {@code gDM}
	 * labeled alternative in {@link RTRegexParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitGDM(@NotNull RTRegexParser.GDMContext ctx);

	/**
	 * Enter a parse tree produced by the {@code blank}
	 * labeled alternative in {@link RTRegexParser#rt}.
	 * @param ctx the parse tree
	 */
	void enterBlank(@NotNull RTRegexParser.BlankContext ctx);
	/**
	 * Exit a parse tree produced by the {@code blank}
	 * labeled alternative in {@link RTRegexParser#rt}.
	 * @param ctx the parse tree
	 */
	void exitBlank(@NotNull RTRegexParser.BlankContext ctx);

	/**
	 * Enter a parse tree produced by the {@code gId}
	 * labeled alternative in {@link RTRegexParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterGId(@NotNull RTRegexParser.GIdContext ctx);
	/**
	 * Exit a parse tree produced by the {@code gId}
	 * labeled alternative in {@link RTRegexParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitGId(@NotNull RTRegexParser.GIdContext ctx);

	/**
	 * Enter a parse tree produced by the {@code gTry}
	 * labeled alternative in {@link RTRegexParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterGTry(@NotNull RTRegexParser.GTryContext ctx);
	/**
	 * Exit a parse tree produced by the {@code gTry}
	 * labeled alternative in {@link RTRegexParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitGTry(@NotNull RTRegexParser.GTryContext ctx);

	/**
	 * Enter a parse tree produced by the {@code gSkip}
	 * labeled alternative in {@link RTRegexParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterGSkip(@NotNull RTRegexParser.GSkipContext ctx);
	/**
	 * Exit a parse tree produced by the {@code gSkip}
	 * labeled alternative in {@link RTRegexParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitGSkip(@NotNull RTRegexParser.GSkipContext ctx);

	/**
	 * Enter a parse tree produced by the {@code gTime}
	 * labeled alternative in {@link RTRegexParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterGTime(@NotNull RTRegexParser.GTimeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code gTime}
	 * labeled alternative in {@link RTRegexParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitGTime(@NotNull RTRegexParser.GTimeContext ctx);

	/**
	 * Enter a parse tree produced by the {@code gRetry}
	 * labeled alternative in {@link RTRegexParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterGRetry(@NotNull RTRegexParser.GRetryContext ctx);
	/**
	 * Exit a parse tree produced by the {@code gRetry}
	 * labeled alternative in {@link RTRegexParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitGRetry(@NotNull RTRegexParser.GRetryContext ctx);

	/**
	 * Enter a parse tree produced by {@link RTRegexParser#id}.
	 * @param ctx the parse tree
	 */
	void enterId(@NotNull RTRegexParser.IdContext ctx);
	/**
	 * Exit a parse tree produced by {@link RTRegexParser#id}.
	 * @param ctx the parse tree
	 */
	void exitId(@NotNull RTRegexParser.IdContext ctx);

	/**
	 * Enter a parse tree produced by the {@code gDecisionMaking}
	 * labeled alternative in {@link RTRegexParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterGDecisionMaking(@NotNull RTRegexParser.GDecisionMakingContext ctx);
	/**
	 * Exit a parse tree produced by the {@code gDecisionMaking}
	 * labeled alternative in {@link RTRegexParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitGDecisionMaking(@NotNull RTRegexParser.GDecisionMakingContext ctx);

	/**
	 * Enter a parse tree produced by the {@code printExpr}
	 * labeled alternative in {@link RTRegexParser#rt}.
	 * @param ctx the parse tree
	 */
	void enterPrintExpr(@NotNull RTRegexParser.PrintExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code printExpr}
	 * labeled alternative in {@link RTRegexParser#rt}.
	 * @param ctx the parse tree
	 */
	void exitPrintExpr(@NotNull RTRegexParser.PrintExprContext ctx);
}