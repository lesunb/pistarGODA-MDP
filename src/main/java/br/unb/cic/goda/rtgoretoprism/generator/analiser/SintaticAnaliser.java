package br.unb.cic.goda.rtgoretoprism.generator.analiser;

import java.util.logging.Level;
import java.util.logging.Logger;

import br.unb.cic.goda.exception.ResponseException;
import br.unb.cic.goda.rtgoretoprism.generator.goda.writer.ManageWriter;

public class SintaticAnaliser {

	private static final Logger LOGGER = Logger.getLogger(SintaticAnaliser.class.getName());

	public static String recoverLogsError() {
		String dir = "/tmp/";
		String dirSintatic = "sintaticAnaliser/goal-model-syntactic-analyzer-linux";

		try {
			Runtime.getRuntime().exec("cp " + dirSintatic + " " + dir);
			
			StringBuilder command = new StringBuilder().append("./").append("tmp/goal-model-syntactic-analyzer-linux");

			Process proc = Runtime.getRuntime().exec(command.toString());
			LOGGER.info(proc.getInputStream().toString());
			LOGGER.info(proc.getOutputStream().toString());

			return ManageWriter.readFileAsString(dir + "goal-model-list-error.json");
		
		} catch (Exception error) {
			LOGGER.log(Level.SEVERE, error.getMessage());
			throw new ResponseException(error);

		}

	}
}
