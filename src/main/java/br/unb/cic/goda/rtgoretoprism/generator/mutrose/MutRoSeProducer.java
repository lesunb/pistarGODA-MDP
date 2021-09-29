package br.unb.cic.goda.rtgoretoprism.generator.mutrose;

import java.io.File;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import br.unb.cic.goda.rtgoretoprism.generator.goda.writer.ManageWriter;

public class MutRoSeProducer {

	public void execute(String model, String hddl, String configuration, String worldKnowledge) {
		try {
			String dir = "mrs/";
			ManageWriter.generateFile(dir, "model.txt", model);
			ManageWriter.generateFile(dir, "configFile.json", configuration);
			ManageWriter.generateFile(dir, "worldKnowledge.xml", worldKnowledge);
			ManageWriter.generateFile(dir, "configHddl.hddl", hddl);
			
//			Runtime.getRuntime().exec("mrs/MRSDecomposer.exe mrs/configHddl.hddl mrs/model.txt mrs/configFile.json mrs/worldKnowledge.xml");
			ProcessBuilder pb = new ProcessBuilder("mrs/MRSDecomposer.exe", "mrs/configHddl.hddl", "mrs/model.txt", "mrs/configFile.json", "mrs/worldKnowledge.xml");
//			
			Process p = pb.start();
			ZipOutputStream zos = new ZipOutputStream(p.getOutputStream());
			DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(dir));
			for (Path path : directoryStream) {
				byte[] bytes = Files.readAllBytes(path);
				zos.putNextEntry(new ZipEntry(path.getFileName().toString()));
				zos.write(bytes, 0, bytes.length);
				zos.closeEntry();
			}
		}catch(Exception error) {
			throw new RuntimeException(error);
			
		}
	}
}