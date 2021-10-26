package br.unb.cic.goda.rtgoretoprism.generator.mutrose;

import java.io.File;
import java.util.logging.Logger;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.unb.cic.goda.exception.ResponseException;
import br.unb.cic.goda.rtgoretoprism.generator.goda.writer.ManageWriter;
import br.unb.cic.goda.utils.GodaUtils;
import br.unb.cic.pistar.model.PistarModel;

public class MutRoSeProducer {

	private static final Logger LOGGER = Logger.getLogger(MutRoSeProducer.class.getName());

	public String execute(String model, String hddl, String configuration, String worldKnowledge) {
		if (model == null || model.isEmpty()) {
			throw new ResponseException("Invalid Model File");
		}
		if (hddl == null || hddl.isEmpty()) {
			throw new ResponseException("Invalid HDDL File");
		}
		if (configuration == null || configuration.isEmpty()) {
			throw new ResponseException("Invalid Configuration File");
		}
		if (worldKnowledge == null || worldKnowledge.isEmpty()) {
			throw new ResponseException("Invalid World Knowledge File");
		}

		String dirOutputZIP = "src/main/webapp/mrs.zip";
		try {
			String dir = "mrs/";
			String dirOutput =  dir + "results/";
			JSONObject jsonObject = this.updatePathConfigurationFile(configuration, dirOutput);
			this.removeCustomPropDiagram(model);

			// gerar arquivos
			File modelFile = ManageWriter.generateFile(dir, "model.txt", model);
			File configFile = ManageWriter.generateFile(dir, "configFile.json", jsonObject.toJSONString());
			File worldKnowledgeFile = ManageWriter.generateFile(dir, "worldKnowledge.xml", worldKnowledge);
			File hddlFile = ManageWriter.generateFile(dir, "configHddl.hddl", hddl);

			JSONObject outputConfig = (JSONObject) jsonObject.get("output");
			String output = (String) outputConfig.get("file_path");

//			ManageWriter.createFolder(dirOutput);
		//	ManageWriter.generateFile(output, "");
			StringBuilder command = new StringBuilder().append("./")
				.append(dir).append("MRSDecomposer").append(" ")
				.append(dir).append("configHddl.hddl").append(" ")
				.append(dir).append("model.txt").append(" ")
				.append(dir).append("configFile.json").append(" ")
				.append(dir).append("worldKnowledge.xml").append(" ")
				.append("");

			Process proc = Runtime.getRuntime().exec(command.toString());
			String result = ManageWriter.readFileAsString(output);
			if(result == null) {
				throw new ResponseException("Fail to execute MutRoSe.");
			}
			
			ManageWriter.toCompact(output, dirOutputZIP);
		} catch (Exception error) {
			throw new ResponseException(error);

		}
		return dirOutputZIP;
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

			if (output.isEmpty()) {
				throw new ResponseException("Configuration file does not have decomposed file generation path.");
			} else {
				File file = new File(output);
				String filename = file.getName();
				String path = dirOutput + filename;
//				File generatedFile = new File(path);
				// atualizar o path do config
				outputConfig.replace("file_path", path);

				return jsonObject;
			}
		} catch (ParseException e) {
			throw new ResponseException(e);
		}
	}
	

	@SuppressWarnings("unchecked")
	private void removeCustomPropDiagram (String model) {
		JSONObject jsonObject;
		JSONParser parser = new JSONParser();
		try {
			jsonObject = (JSONObject) parser.parse(model);
			JSONObject outputConfig = (JSONObject) jsonObject.get("diagram");
			String output = (String) outputConfig.remove("customProperties");
		} catch (ParseException e) {
			throw new ResponseException(e);
		}
	}
}
