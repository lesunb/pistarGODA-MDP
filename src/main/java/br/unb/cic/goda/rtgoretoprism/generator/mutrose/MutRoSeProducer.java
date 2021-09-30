package br.unb.cic.goda.rtgoretoprism.generator.mutrose;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import br.unb.cic.goda.rtgoretoprism.generator.goda.writer.ManageWriter;

public class MutRoSeProducer {


	@SuppressWarnings({ "resource", "unchecked" })
	public void execute(String model, String hddl, String configuration, String worldKnowledge) {
		if (model == null || model.isEmpty() || hddl == null || model.isEmpty() || configuration == null
				|| model.isEmpty() || worldKnowledge == null || model.isEmpty()) {
			throw new RuntimeException("Invalid File");
		}

		try {
			String dir = "mrs/";
			String dirOutput = dir + "output/";

			// recuperar o path original do arquivo de configuracao
			JSONObject jsonObject;
			JSONParser parser = new JSONParser();
			jsonObject = (JSONObject) parser.parse(configuration);
			JSONObject outputConfig = (JSONObject) jsonObject.get("output");
			String output = (String) outputConfig.get("file_path");
			File file = new File(output);
			String filename = file.getName();
			File pathToFile = new File(dirOutput + filename);
			// atualizar o path do config
			outputConfig.replace("file_path", pathToFile.getAbsolutePath());

			// gerar arquivos
			ManageWriter.generateFile(dir, "model.txt", model);
			ManageWriter.generateFile(dir, "configFile.json", jsonObject.toJSONString());
			ManageWriter.generateFile(dir, "worldKnowledge.xml", worldKnowledge);
			ManageWriter.generateFile(dir, "configHddl.hddl", hddl);

			StringBuilder command = new StringBuilder().append("chmod +x ").append(dir).append("MRSDecomposer ")
					.append(dir).append("configHddl.hddl ").append(dir).append("model.txt ").append(dir)
					.append("configFile.json ").append(dir).append("worldKnowledge.xml ");

			Runtime.getRuntime().exec(command.toString());

			FileOutputStream fos = new FileOutputStream("src/main/webapp/mrs.zip");
			ZipOutputStream zoss = new ZipOutputStream(fos);
			DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(dirOutput));
			for (Path path : directoryStream) {
				byte[] bytes = Files.readAllBytes(path);
				zoss.putNextEntry(new ZipEntry(path.getFileName().toString()));
				zoss.write(bytes, 0, bytes.length);
				zoss.closeEntry();
			}
			
//			ManageWriter.generateFile(file.getAbsolutePath(), configuration);
		} catch (Exception error) {
			throw new RuntimeException(error);

		}
	}
}