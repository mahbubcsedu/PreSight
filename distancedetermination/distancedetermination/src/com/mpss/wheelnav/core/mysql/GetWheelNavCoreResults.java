/*
 * Distance determintaion
 * Author : Amol Deshpande, Mahbubur rahman
 * {date}
 * @author B. P. Hennessey
 * @version 1.0
 */
package com.mpss.wheelnav.core.mysql;

// TODO: Auto-generated Javadoc
/**
 * The Class GetWheelNavCoreResults.
 */
public class GetWheelNavCoreResults {

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {

		try {

			if(args.length!=2) {
				System.out.println("Usage: \n"
						+ "Arg0 : Input File containing locations of WheelNavCore outputs\n"
						+ "Arg1 : Result File");
				return;
			}

			String input_file = args[0];
			String output_file = args[1];

			GetWheelNavCoreResultsHelper helper = new GetWheelNavCoreResultsHelper();
			System.out.println("GetWheelNavCoreResultsHelper - Begin");
			helper.getWheelNavCoreResultsHelper(input_file, output_file);
			System.out.println("GetWheelNavCoreResultsHelper - Done");
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

}
