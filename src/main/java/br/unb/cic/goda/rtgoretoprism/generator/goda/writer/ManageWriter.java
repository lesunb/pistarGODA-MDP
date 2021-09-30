package br.unb.cic.goda.rtgoretoprism.generator.goda.writer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import br.unb.cic.goda.rtgoretoprism.generator.CodeGenerationException;
import br.unb.cic.goda.rtgoretoprism.util.FileUtility;

public class ManageWriter {


    public static PrintWriter createFile(String adf, String outputFolder) throws CodeGenerationException {
        try {
            PrintWriter adfFile = new PrintWriter(
                    new BufferedWriter(new FileWriter(outputFolder + adf)));
            return adfFile;
        } catch (IOException e) {
            String msg = "Error: Can't create output model file.";
            System.out.println(msg);
            throw new CodeGenerationException(msg);
        }
    }

    public static String readFileAsString(String filePath) throws CodeGenerationException {
        String res = null;
        try {
            res = FileUtility.readFileAsString(filePath);
        } catch (IOException e) {
            String msg = "Error: file " + filePath + " not found.";
            System.out.println(msg);
            throw new CodeGenerationException(msg);
        }
        return res;
    }

    public static void printModel(PrintWriter adf, String... modules) {
        for (String module : modules) {
            adf.println(module);
        }
        adf.close();
    }

	public static void generateFile(final String path, final String nameFile, final String message) {
		try {
			File dir = new File(path);
			dir.mkdirs();

			FileWriter arq = new FileWriter(dir + "/" + nameFile);
			PrintWriter writeArq = new PrintWriter(arq);
			writeArq.printf(message);
			arq.close();
		} catch (IOException e) {
			throw new RuntimeException("Error: Can't create file.");
		}
	}
	
	public static void generateFile(final String nameFile, final String message) {
		try {
			File file = new File(nameFile);
			FileWriter arq = new FileWriter(file.getAbsolutePath());
			PrintWriter writeArq = new PrintWriter(arq);
			writeArq.printf(message);
			arq.close();
		} catch (IOException e) {
			throw new RuntimeException("Error: Can't create file.");
		}
	}

}
