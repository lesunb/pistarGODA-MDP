package br.unb.cic.goda.rtgoretoprism.generator.analiser;

import java.util.logging.Level;
import java.util.logging.Logger;

import br.unb.cic.goda.exception.ResponseException;
import br.unb.cic.goda.rtgoretoprism.generator.goda.writer.ManageWriter;

public class SintaticAnaliserMutRoSe implements SintaticAnaliserInterface {

	private static final Logger LOGGER = Logger.getLogger(SintaticAnaliserMutRoSe.class.getName());


	@Override
	public String recoverLogs() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String recoverLogsError() {
		String dir = "sintaticAnaliser/";
		String dirSintatic = "sintaticAnaliser/goal-model-syntactic-analyzer-linux";

		try {
			// Runtime.getRuntime().exec("cp " + dirSintatic + " " + dir);

			StringBuilder command = new StringBuilder().append("./").append(dirSintatic);

			ManageWriter.generateFile(dir + "goal-model-list-error.json", "");

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
