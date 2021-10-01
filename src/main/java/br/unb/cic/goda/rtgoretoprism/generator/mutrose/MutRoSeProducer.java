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
			String pathConfig = this.updatePathConfigurationFile(configuration, dirOutput);

			// gerar arquivos
			ManageWriter.generateFile(dir, "model.txt", model);
		    ManageWriter.generateFile(dir, "configFile.json", pathConfig);
			ManageWriter.generateFile(dir, "worldKnowledge.xml", worldKnowledge);
			ManageWriter.generateFile(dir, "configHddl.hddl", hddl);
//			ManageWriter.generateFile(dirOutput, new File(pathConfig).getName(), "");

			
			StringBuilder command = new StringBuilder().append("./").append(dir).append("MRSDecomposer ").append(dir)
					.append("configHddl.hddl ").append(dir).append("model.txt ").append(dir).append("configFile.json ")
					.append(dir).append("worldKnowledge.xml -p");

//			Runtime.getRuntime().exec(command.toString());

			invokeAndGetResult(command.toString(), pathConfig);
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
	

    private String invokeAndGetResult(String commandLine, String resultsPath) throws IOException {
        Process program = Runtime.getRuntime().exec(commandLine);
        try {
            program.waitFor();
        } catch (InterruptedException e) {
			throw new RuntimeException(e);
        }
        
        logExecResults(program);
        
        List<String> lines = Files.readAllLines(Paths.get(resultsPath), Charset.forName("UTF-8"));

        if(lines.size() > 0) {
        	LOGGER.info(lines.get(lines.size() - 1));
            return lines.get(lines.size() - 1);
        }
        
    	LOGGER.info(lines.get(lines.size()));
        return lines.get(lines.size());
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