package br.unb.cic.goda.rtgoretoprism.generator.goda.parser;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.misc.ParseCancellationException;

import br.unb.cic.goda.model.ModelTypeEnum;

public class ThrowingErrorListener extends BaseErrorListener {

    public static final ThrowingErrorListener INSTANCE = new ThrowingErrorListener();

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e)
            throws ParseCancellationException {
        System.out.println("Error creating model: line " + line + ":" + charPositionInLine + " " + msg);
        throw new ParseCancellationException("line " + line + ":" + charPositionInLine + " " + msg);
    }
}