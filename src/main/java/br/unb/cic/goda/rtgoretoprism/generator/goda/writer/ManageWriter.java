package br.unb.cic.goda.rtgoretoprism.generator.goda.writer;

import br.unb.cic.goda.rtgoretoprism.generator.CodeGenerationException;
import br.unb.cic.goda.rtgoretoprism.util.FileUtility;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

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
}
