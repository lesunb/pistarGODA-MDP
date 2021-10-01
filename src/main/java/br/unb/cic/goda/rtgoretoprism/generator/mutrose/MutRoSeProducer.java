package br.unb.cic.goda.rtgoretoprism.generator.mutrose;

import java.io.File;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import br.unb.cic.goda.rtgoretoprism.generator.goda.writer.ManageWriter;

public class MutRoSeProducer {

//	@SuppressWarnings({ "resource", "unchecked" })
	public void execute(String model, String hddl, String configuration, String worldKnowledge) {
		if (model == null || model.isEmpty() || hddl == null || model.isEmpty() || configuration == null
				|| model.isEmpty() || worldKnowledge == null || model.isEmpty()) {
			throw new RuntimeException("Invalid File");
		}

		try {
			String dir = "mrs/";
			String dirOutput = dir + "output/";
			String pathConfig = this.updatePathConfigurationFile(configuration, dirOutput);

			// gerar arquivos
			ManageWriter.generateFile(dir, "model.txt", model);
		    ManageWriter.generateFile(dir, "configFile.json", pathConfig);
			ManageWriter.generateFile(dir, "worldKnowledge.xml", worldKnowledge);
			ManageWriter.generateFile(dir, "configHddl.hddl", hddl);
			ManageWriter.generateFile(dirOutput, new File(pathConfig).getName(), "");

			
			StringBuilder command = new StringBuilder().append("./").append(dir).append("MRSDecomposer ").append(dir)
					.append("configHddl.hddl ").append(dir).append("model.txt ").append(dir).append("configFile.json ")
					.append(dir).append("worldKnowledge.xml -p");

			Runtime.getRuntime().exec(command.toString());

			ManageWriter.toCompact(pathConfig, "src/main/webapp/mrs.zip");
		} catch (Exception error) {
			throw new RuntimeException(error);

		}
	}
	
	@SuppressWarnings("unchecked")
	private String updatePathConfigurationFile(String configuration, String dirOutput) {
		
		// recuperar o path original do arquivo de configuracao
		JSONObject jsonObject;
		JSONParser parser = new JSONParser();
		try {
			jsonObject = (JSONObject) parser.parse(configuration);
			JSONObject outputConfig = (JSONObject) jsonObject.get("output");
			String output = (String) outputConfig.get("file_path");
			File file = new File(output);
			String filename = file.getName();
			String path = dirOutput + filename;
//			File generatedFile = new File(dirOutput + filename);
			// atualizar o path do config
			outputConfig.replace("file_path", path);
			
			return path;
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
}