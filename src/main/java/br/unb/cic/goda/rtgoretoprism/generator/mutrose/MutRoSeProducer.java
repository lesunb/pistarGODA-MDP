package br.unb.cic.goda.rtgoretoprism.generator.mutrose;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Logger;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import br.unb.cic.goda.exception.ResponseException;
import br.unb.cic.goda.rtgoretoprism.generator.goda.writer.ManageWriter;

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
//			/var/folders/sw
			String dir = "mrs/";
			String dirOutput = "/tmp/";
			JSONObject jsonObject = this.updatePathConfigurationFile(configuration, dirOutput);

			// gerar arquivos
			File modelFile = ManageWriter.generateFile(dir, "model.txt", model);
			File configFile = ManageWriter.generateFile(dir, "configFile.json", jsonObject.toJSONString());
			File worldKnowledgeFile = ManageWriter.generateFile(dir, "worldKnowledge.xml", worldKnowledge);
			File hddlFile = ManageWriter.generateFile(dir, "configHddl.hddl", hddl);

			JSONObject outputConfig = (JSONObject) jsonObject.get("output");
			String output = (String) outputConfig.get("file_path");

//			ManageWriter.createFolder(dirOutput);
			ManageWriter.generateFile(output, "");
			StringBuilder command = new StringBuilder().append("./").append(dir).append("MRSDecomposer").append(" ")
					.append(hddlFile.getAbsolutePath()).append(" ").append(modelFile.getAbsolutePath()).append(" ")
					.append(configFile.getAbsolutePath()).append(" ").append(worldKnowledgeFile.getAbsolutePath())
					.append(" ").append("-p");

			Process proc = Runtime.getRuntime().exec(command.toString());
			LOGGER.info(proc.getInputStream().toString());
			LOGGER.info(proc.getOutputStream().toString());

			List<String> lines = Files.readAllLines(Paths.get(output), Charset.forName("UTF-8"));

			if (lines.size() - 1 < 0) {
				throw new ResponseException("Fail to execute MutRoSe.");
			} else {
				String outputJson = lines.get(lines.size() - 1);
				ManageWriter.generateFile(output, outputJson);
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
}
