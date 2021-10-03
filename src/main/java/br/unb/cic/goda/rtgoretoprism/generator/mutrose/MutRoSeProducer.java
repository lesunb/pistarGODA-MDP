package br.unb.cic.goda.rtgoretoprism.generator.mutrose;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Logger;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import br.unb.cic.goda.rtgoretoprism.generator.goda.writer.ManageWriter;

public class MutRoSeProducer {

    private static final Logger LOGGER = Logger.getLogger(MutRoSeProducer.class.getName());
    
//	@SuppressWarnings({ "resource", "unchecked" })
	public void execute(String model, String hddl, String configuration, String worldKnowledge) {
		if (model == null || model.isEmpty() || hddl == null || model.isEmpty() || configuration == null
				|| model.isEmpty() || worldKnowledge == null || model.isEmpty()) {
			throw new RuntimeException("Invalid File");
		}

		try {
			String dir = "mrs/";
			String dirOutput = dir + "output/";
			JSONObject jsonObject = this.updatePathConfigurationFile(configuration, dirOutput);

			// gerar arquivos
			ManageWriter.generateFile(dir, "model.txt", model);
		    ManageWriter.generateFile(dir, "configFile.json", jsonObject.toJSONString());
			ManageWriter.generateFile(dir, "worldKnowledge.xml", worldKnowledge);
			ManageWriter.generateFile(dir, "configHddl.hddl", hddl);
//			ManageWriter.generateFile(jsonObject.toJSONString(), "");
//			ManageWriter.generateFile(dirOutput, new File(pathConfig).getName(), "");

			
			StringBuilder command = new StringBuilder().append("./").append(dir).append("MRSDecomposer ").append(dir)
					.append("configHddl.hddl ").append(dir).append("model.txt ").append(dir).append("configFile.json ")
					.append(dir).append("worldKnowledge.xml ");

			Process proc = Runtime.getRuntime().exec(command.toString());
			LOGGER.info(proc.getInputStream().toString());
			LOGGER.info(proc.getOutputStream().toString());

			JSONObject outputConfig = (JSONObject) jsonObject.get("output");
			String output = (String) outputConfig.get("file_path");
			ManageWriter.toCompact(output, "src/main/webapp/mrs.zip");
		} catch (Exception error) {
			throw new RuntimeException(error);

		}
	}
	
	@SuppressWarnings("unchecked")
	private JSONObject updatePathConfigurationFile(String configuration, String dirOutput) {
		
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
			File generatedFile = new File(path);
			// atualizar o path do config
			outputConfig.replace("file_path", path);
			
			return jsonObject;
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
}
