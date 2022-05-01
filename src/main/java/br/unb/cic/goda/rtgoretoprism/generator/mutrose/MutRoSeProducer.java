package br.unb.cic.goda.rtgoretoprism.generator.mutrose;

import java.io.File;
import java.util.logging.Logger;

import br.unb.cic.goda.rtgoretoprism.generator.ManageCMD;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import br.unb.cic.goda.exception.ResponseException;
import br.unb.cic.goda.rtgoretoprism.generator.goda.writer.ManageWriter;

public class MutRoSeProducer {

	private static final Logger LOGGER = Logger.getLogger(MutRoSeProducer.class.getName());

	@SuppressWarnings("unused")
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

		try {
			String dir = "mrs/";
			String dirConfig = "mrs/config/";
			String dirOutput = dir + "results/";
			String dirOutputZIP = "src/main/webapp/mrs.zip";
			JSONObject jsonObject = this.updatePathConfigurationFile(configuration, dirOutput);
			ManageWriter.cleanFolder(dirConfig);

			// gerar arquivos
			File modelFile = ManageWriter.generateFile(dirConfig, "model.txt", model);
			File configFile = ManageWriter.generateFile(dirConfig, "configFile.json", jsonObject.toJSONString());
			File worldKnowledgeFile = ManageWriter.generateFile(dirConfig, "worldKnowledge.xml", worldKnowledge);
			File hddlFile = ManageWriter.generateFile(dirConfig, "configHddl.hddl", hddl);

			JSONObject outputConfig = (JSONObject) jsonObject.get("output");
			String output = (String) outputConfig.get("file_path");

			ManageWriter.createFolder(dirOutput);
			//ManageWriter.generateFile(output, "");
			StringBuilder command = new StringBuilder().append("./").append(dir).append("MutroseMissionDecomposer").append(" ")
					.append(dirConfig).append("configHddl.hddl").append(" ").append(dirConfig).append("model.txt").append(" ")
					.append(dirConfig).append("configFile.json").append(" ").append(dirConfig).append("worldKnowledge.xml")
					.append(" ").append("");

			ManageCMD.ResultCMD resultCMD = ManageCMD.executeCommand(command.toString());
			if(resultCMD.getError().isEmpty()){
				String result = ManageWriter.readFileAsString(output);
				if (result == null) {
					throw new ResponseException("Fail to execute MutRoSe.");
				}else {
					ManageWriter.toCompact(output, dirOutputZIP);
					ManageWriter.cleanFolder(output);
					ManageWriter.cleanFolder(dirConfig);
					return result;
				}
			}else{
				LOGGER.warning("Fail to execute Mutrose: " + resultCMD.getError());
				throw new ResponseException(resultCMD.getError());
			}
		} catch (Exception error) {
			throw new ResponseException(error);
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
}
