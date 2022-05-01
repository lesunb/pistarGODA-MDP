package br.unb.cic.goda.rtgoretoprism.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.channels.FileChannel;

/**
 * A set of utility in order to work with files
 * 
 * @author bertolini
 */
public class FileUtility {

	/**
	 * Copy source file into target one
	 * 
	 * @param source the source file
	 * @param dest the target file
	 * 
	 */
	public static void copyFile(File source, File dest) throws IOException {
		FileChannel in = null;
		FileChannel out = null;
		
		try {
			in = new FileInputStream(source).getChannel();
			out = new FileOutputStream(dest).getChannel();
			
			in.transferTo(0, in.size(), out);
		} catch( IOException ioe ) {
			throw ioe;
		}
		finally {
			in.close();
			out.close();
		}
	}

	/**
	 * Copy the specified file into target one 
	 * 
	 * @param source the source file path
	 * @param dest the target file path
	 * 
	 * @throws IOException
	 */
	public static void copyFile(String source, String dest) throws IOException {
		File src=new File (source);
		File dst=new File (dest);
		
		copyFile(src, dst);
	}
	
	/**
	 * Write the specified content into a file
	 * 
	 * @param content the content of the file
	 * @param filename the target file
	 * 
	 * @throws IOException 
	 * 
	 */
	public static void writeFile(String content, String filename) throws IOException {
		PrintWriter file = null;
		
		try {
			file = new PrintWriter(new BufferedWriter(new FileWriter(filename, true)));
			file.println(content);
		} catch( IOException ioe ) {
			throw ioe;
		}
		finally {
			file.close();
		}
	}
	
	/**
	 * Delete the specified file
	 * 
	 * @param filename the target file
	 * 
	 * @throws IOException 
	 * 
	 */
	public static void deleteFile(String filename, boolean onExit){
		File file = null;
		file = new File(filename);
		if(onExit)
			file.deleteOnExit();
		else
			file.delete();
	}
	
	/**
	 * Delete the specified file
	 * 
	 * @param filename the target file
	 * 
	 * @throws IOException 
	 * 
	 */
	public static void deleteFile(String filename){
		File file = null;
		file = new File(filename);
		file.deleteOnExit();
	}
	
	
	/**
	 * Read the specified file into a String
	 * 
	 * @param filePath the path of the file to read
	 * 
	 * @return the file content as a String
	 * 
	 * @throws IOException 
	 */
	public static String readFileAsString(String filePath) throws IOException {
		StringBuffer fileData = new StringBuffer(1000);
		BufferedReader reader = null;
		
		try {
			reader = new BufferedReader(new FileReader(filePath));
			char[] buf = new char[1024];
			int numRead = 0;
			
			while ((numRead = reader.read(buf)) != -1) {
				String readData = String.valueOf(buf, 0, numRead);
				fileData.append(readData);
				buf = new char[1024];
			}
			reader.close();
		} catch( Exception ioe ) {
			throw new RuntimeException("Failed to read file: " + filePath);
		}

		return fileData.toString();
	}

	public static long fileSize(String string) {
		return new File(string).length();		
	}
	
	public static String convertStreamToString(java.io.InputStream is) {
	    java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
	    return s.hasNext() ? s.next() : "";
	}
}