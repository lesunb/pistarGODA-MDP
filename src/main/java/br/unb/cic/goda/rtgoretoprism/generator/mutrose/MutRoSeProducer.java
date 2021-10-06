package br.unb.cic.goda.rtgoretoprism.generator.mutrose;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import br.unb.cic.goda.rtgoretoprism.generator.goda.writer.ManageWriter;

public class MutRoSeProducer {

    private static final Logger LOGGER = Logger.getLogger(MutRoSeProducer.class.getName());
    
	public String execute(String model, String hddl, String configuration, String worldKnowledge) {
		if (model == null || model.isEmpty() || hddl == null || model.isEmpty() || configuration == null
				|| model.isEmpty() || worldKnowledge == null || model.isEmpty()) {
			throw new RuntimeException("Invalid File");
		}

		try {
			String dir = "mrs/";
			String dirOutput =  dir + "output/";
			JSONObject jsonObject = this.updatePathConfigurationFile(configuration, dirOutput);

			// gerar arquivos
			File modelFile = ManageWriter.generateFile(dir, "model.txt", model);
			File configFile = ManageWriter.generateFile(dir,  "configFile.json", jsonObject.toJSONString());
			File worldKnowledgeFile = ManageWriter.generateFile(dir,  "worldKnowledge.xml", worldKnowledge);
			File hddlFile = ManageWriter.generateFile(dir,  "configHddl.hddl", hddl);
			
			
			JSONObject outputConfig = (JSONObject) jsonObject.get("output");
			String output = (String) outputConfig.get("file_path");
			
			StringBuilder command = new StringBuilder()
					.append("./").append(dir).append("MRSDecomposer").append(" ")
					.append(hddlFile.getAbsolutePath()).append(" ")
					.append(modelFile.getAbsolutePath()).append(" ")
					.append(configFile.getAbsolutePath()).append(" ")
					.append(worldKnowledgeFile.getAbsolutePath()).append(" -p");

			Process proc = Runtime.getRuntime().exec(command.toString());
			LOGGER.info(proc.getInputStream().toString());
			LOGGER.info(proc.getOutputStream().toString());
			

			ManageWriter.toCompact(output, "src/main/webapp/mrs.zip");
			return "src/main/webapp/mrs.zip";
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

    private String invokeAndGetResult(String commandLine, String resultsPath) throws IOException {
        LOGGER.fine(commandLine);
        Process program = Runtime.getRuntime().exec(commandLine);
        int exitCode = 0;
        try {
            exitCode = program.waitFor();
        } catch (InterruptedException e) {
        	LOGGER.severe("Error invoking param with command:" + commandLine);
        	
            LOGGER.severe("Exit code: " + exitCode);
            LOGGER.log(Level.SEVERE, e.toString(), e);
        }
        
        logExecResults(program);
        
        List<String> lines = Files.readAllLines(Paths.get(resultsPath), Charset.forName("UTF-8"));
        // Formula
        return lines.get(lines.size() - 1);
    }
    
    private void logExecResults(Process proc) throws IOException {
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
