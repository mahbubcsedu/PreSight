/*
 * Distance determintaion
 * Author : Amol Deshpande, Mahbubur rahman
 * {date}
 * @author B. P. Hennessey
 * @version 1.0
 */
package com.mpss.wheelnav.core.mysql;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;


// TODO: Auto-generated Javadoc
/**
 * The Class GetWheelNavCoreResultsHelper.
 */
public class GetWheelNavCoreResultsHelper {

	/**
	 * Write to file.
	 *
	 * @param file the file
	 * @param text the text
	 */
	private void writeToFile(String file, String text) {
		BufferedWriter writer = null;
		try {
			File output = new File(file);
			writer = new BufferedWriter(new FileWriter(output));
			writer.write(text);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (Exception e) {
			}
		}
	}

	/**
	 * Read result record from file.
	 *
	 * @param file the file
	 * @param flag_get_header_only the flag_get_header_only
	 * @return the string
	 */
	private String readResultRecordFromFile(String file, boolean flag_get_header_only) {
		String record="";
		try{
			FileInputStream fstream = new FileInputStream(file);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			strLine = br.readLine();
			if(flag_get_header_only) {
				record = strLine;
			}
			else {
				record = br.readLine();
			}
		} catch(Exception ex) {
			ex.printStackTrace(); 
		}
		return record;
	}

	/**
	 * Read wheel nav core outputs.
	 *
	 * @param inputFile the input file
	 * @return the string
	 */
	private String readWheelNavCoreOutputs(String inputFile) {
		StringBuilder sb = new StringBuilder();
		StringBuilder sb_failures= new StringBuilder();
		int count=0;
		int failures=0;
		try{
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			while ((strLine = br.readLine()) != null)   {
				if(strLine.trim().length()>0) {
					if(count==0) {
						String record = readResultRecordFromFile(strLine, true);
						sb.append(record + "\n");
					}
					String record = readResultRecordFromFile(strLine, false);
					if(record.trim().length()==0) {
						failures++;
						sb_failures.append(strLine + "\n");
					}
					else {
						sb.append(record + "\n");
						count++;
					}
				}
			}

		} catch(Exception ex) {
			ex.printStackTrace(); 
		}
		System.out.println();
		System.out.println("\tNumber of WheelNavCore outputs read: " + (count+failures));
		System.out.println("\tNumber of WheelNavCore outputs successfully written to file : " + count);
		System.out.println();
		if(failures>0) {
			System.out.println("\tFailed to read following results: " + sb_failures.toString());
		}
		return sb.toString();
	}

	/**
	 * Gets the wheel nav core results helper.
	 *
	 * @param input_file the input_file
	 * @param output_file the output_file
	 * @return the wheel nav core results helper
	 */
	public void getWheelNavCoreResultsHelper(String input_file, String output_file) {
		writeToFile(output_file, readWheelNavCoreOutputs(input_file));
	}
}
