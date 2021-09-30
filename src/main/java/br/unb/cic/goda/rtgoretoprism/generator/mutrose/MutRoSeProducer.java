package br.unb.cic.goda.rtgoretoprism.generator.mutrose;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import br.unb.cic.goda.rtgoretoprism.generator.goda.writer.ManageWriter;

public class MutRoSeProducer {

	private static final Logger LOGGER = Logger.getLogger(MutRoSeProducer.class.getName());

	public void execute(String model, String hddl, String configuration, String worldKnowledge) {
		if (model == null || model.isEmpty() || hddl == null || model.isEmpty() || configuration == null
				|| model.isEmpty() || worldKnowledge == null || model.isEmpty()) {
			throw new RuntimeException("Invalid File");
		}

		try {
			JSONObject jsonObject;
			JSONParser parser = new JSONParser();
			jsonObject = (JSONObject) parser.parse(configuration);

			JSONObject outputConfig = (JSONObject) jsonObject.get("output");
			String output = (String) outputConfig.get("file_path");

			String dir = "mrs/";
			String outputZip = "src/main/webapp/mrs.zip";
			ManageWriter.generateFile(dir, "model.txt", model);
			ManageWriter.generateFile(dir, "configFile.json", configuration);
			ManageWriter.generateFile(dir, "worldKnowledge.xml", worldKnowledge);
			ManageWriter.generateFile(dir, "configHddl.hddl", hddl);
//			ManageWriter.generateFile(output, "task_output.json", "");

			StringBuilder command = new StringBuilder().append("chmod 777 ").append(dir).append("MRSDecomposer.exe ")
					.append(dir).append("configHddl.hddl ").append(dir).append("model.txt ").append(dir)
					.append("configFile.json ").append(dir).append("worldKnowledge.xml ");

			runCommand(command.toString(), "src/main/webapp/");

			FileOutputStream fos = new FileOutputStream(outputZip);
			ZipOutputStream zos = new ZipOutputStream(fos);
			DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(output));
			for (Path path : directoryStream) {
				byte[] bytes = Files.readAllBytes(path);
				zos.putNextEntry(new ZipEntry(path.getFileName().toString()));
				zos.write(bytes, 0, bytes.length);
				zos.closeEntry();
			}
		} catch (Exception error) {
			throw new RuntimeException(error);

		}
	}

	private String runCommand(String commandLine, String resultsPath) throws IOException {

		LOGGER.info(commandLine);
		Process program = Runtime.getRuntime().exec(commandLine);
		int exitCode = 0;
		try {
			exitCode = program.waitFor();
		} catch (InterruptedException e) {
			LOGGER.severe("Error invoking param with command:" + commandLine);

			LOGGER.severe("Exit code: " + exitCode);
			LOGGER.log(Level.SEVERE, e.toString(), e);
		}
		
		logExecCommands(program);

		List<String> lines = Files.readAllLines(Paths.get(resultsPath), Charset.forName("UTF-8"));
		// Formula
		return lines.get(lines.size() - 1);
	}
	

    private void logExecCommands(Process proc) throws IOException  {
        BufferedReader stdInput = new BufferedReader(new 
       	     InputStreamReader(proc.getInputStream()));
       
       BufferedReader stdError = new BufferedReader(new 
       	     InputStreamReader(proc.getErrorStream()));
       
       String s = null;
       
       if(stdInput.ready()) {
	       // Read the output from the command
	       System.out.println("Here is the standard output of the command:");
	       while ((s = stdInput.readLine()) != null) {
	           System.out.println(s);
	       }
       }

       if(stdError.ready()) {
           // Read any errors from the attempted command
           System.err.println("Here is the standard error of the command (if any):");
           while ((s = stdError.readLine()) != null) {
               System.err.println(s);
           }
       }
    }
}