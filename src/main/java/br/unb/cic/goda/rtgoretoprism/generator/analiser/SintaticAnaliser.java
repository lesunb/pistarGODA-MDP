package br.unb.cic.goda.rtgoretoprism.generator.analiser;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.unb.cic.goda.exception.ResponseException;
import br.unb.cic.goda.model.ModelTypeEnum;
import br.unb.cic.goda.rtgoretoprism.generator.goda.writer.ManageWriter;

public class SintaticAnaliser {

	private static final Logger LOGGER = Logger.getLogger(SintaticAnaliser.class.getName());

	public static String recoverLogsError() {
		String dir = "sintaticAnaliser/";
		String dirSintatic = "sintaticAnaliser/goal-model-syntactic-analyzer-linux";

		try {
			//Runtime.getRuntime().exec("cp " + dirSintatic + " " + dir);
			
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
	
	public static String verifySintaxModel(String rtRegex, String typeModel) {
		if (rtRegex != null) {
			// Removes os espaços em branco dentro dos colchetes
			rtRegex = removeBlankSpaceInBrackets(rtRegex);
			String regexAux = rtRegex;
			if (isMDP(typeModel)) {
				// Verifica se o modulo NonDeterminism existe
				if (rtRegex.toUpperCase().contains("DM")) {
					generateMessageError(RegexDecompositionEnum.DM, regexAux);
				}
			}
			if (rtRegex.toUpperCase().contains("TRY")) {
				generateMessageError(RegexDecompositionEnum.TRY, regexAux);
			}
			if (rtRegex.toUpperCase().contains("#")) {
				generateMessageError(RegexDecompositionEnum.OR_AND_PARALEL, regexAux);
			}
			if (rtRegex.toUpperCase().contains("@")) {
				generateMessageError(RegexDecompositionEnum.RETRY, regexAux);
			}
		}

		return rtRegex;
	}

	private static String removeBlankSpaceInBrackets(String rtRegex) {
		Matcher bracketsMatcher = Pattern.compile("\\[.*\\]").matcher(rtRegex);
		if (bracketsMatcher.find()) {
			String regexStart = rtRegex.substring(0, bracketsMatcher.start());
			String regexEnd = rtRegex.substring(bracketsMatcher.start(), bracketsMatcher.end());
			return regexStart + regexEnd.replaceAll(" ", "");
		}

		return rtRegex;
	}

	private static void generateMessageError(RegexDecompositionEnum enm, String rtRegex) {
		Matcher bracketsMatcher = Pattern.compile("\\[.*\\]").matcher(rtRegex);
		String reg1 = enm.getPrimary();
		String reg2 = enm.getSecundary();

		Matcher matcherDM1 = Pattern.compile(reg1).matcher(rtRegex);
		boolean find1 = matcherDM1.find();

		if (find1) {
			rtRegex = rtRegex.substring(matcherDM1.start(), matcherDM1.end());
		} else {
			if (!bracketsMatcher.find()) {
				throw new RuntimeException("Invalid sintax " + rtRegex + ". The correct sintax to " + enm.getName()
						+ " is " + enm.getCorrectSintax() + ".");
			}
			rtRegex = rtRegex.substring(bracketsMatcher.start(), bracketsMatcher.end());
		}

		if (!reg2.isEmpty()) {
			Matcher matcherDM2 = Pattern.compile(reg2).matcher(rtRegex);
			if (matcherDM2.find()) {
				throw new RuntimeException("Invalid sintax " + rtRegex + " in ' " + matcherDM2.group()
						+ " '. The correct sintax to " + enm.getName() + " is " + enm.getCorrectSintax() + ".");
			}
		} else {
			if (!find1) {
				throw new RuntimeException("Invalid sintax " + rtRegex + ". The correct sintax to " + enm.getName()
						+ " is " + enm.getCorrectSintax() + ".");
			}
		}

	}

	private static boolean isMDP(String typeModel) {
		ModelTypeEnum enumn = ModelTypeEnum.valueOf(typeModel);
		return enumn.equals(ModelTypeEnum.MDP);
	}

	@SuppressWarnings("unused")
	private static boolean isDTMC(String typeModel) {
		ModelTypeEnum enumn = ModelTypeEnum.valueOf(typeModel);
		return enumn.equals(ModelTypeEnum.DTMC);
	}

	@SuppressWarnings("unused")
	private enum RegexDecompositionEnum {
		RETRY(0, "RETRY", "x@x", "\\[.*\\@[0-9].*\\]", ""),
		TRY(1, "TRY", "[try(x)?(x | skip):(x | skip)]", "\\[try\\(.*\\)\\?.*\\:.*\\]", ""),
		DM(2, "DM", "[DM(x,x)]", "\\[DM\\(.*?\\)\\]", "[^0-9A-Za-z\\.\\_\\-\\(\\,\\)\\[\\]]"),
		OR_AND_PARALEL(3, "OR_AND_PARALEL", "x#x", "\\[.*\\#.*\\]", "");

		private final String name;
		private final Integer code;
		private final String primary;
		private final String secundary;
		private final String correctSintax;

		RegexDecompositionEnum(Integer code, String name, String correctSintax, String primary, String secundary) {
			this.name = name;
			this.code = code;
			this.primary = primary;
			this.secundary = secundary;
			this.correctSintax = correctSintax;
		}

		public Integer getCode() {
			return this.code;
		}

		public String getPrimary() {
			return primary;
		}

		public String getSecundary() {
			return secundary;
		}

		public String getCorrectSintax() {
			return correctSintax;
		}

		public String getName() {
			return name;
		}

	}
}
