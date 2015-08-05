package com.mpss.wheelnav.mysql;

public class SaveHITResultsToDB {

	public static final String MYSQL_SERVER_URL = "mpss.csce.uark.edu";
	public static final String MYSQLDB_RESULTS_TABLE_NAME = "AMTSidewalkTileAnnotationResults";

	public static void main(String[] args) {
		try {

			if(args.length!=4) {
				System.out.println("Usage: \n"
						+ "Arg0 : MYSQL USER \n"
						+ "Arg1 : MYSQL PWD \n"
						+ "Arg2 : MYSQL Database \n"
						+ "Arg3 : Turk Reponse File (output.result) \n");
				return;
			}

			String mysql_user = args[0];
			String mysql_password = args[1];
			String mysql_db = args[2];
			String output_file = args[3];
			
			System.out.println("SaveHITResultsToDB - Begin");
			SaveHITResultsToDBHelper helper = new SaveHITResultsToDBHelper();
			helper.processTurkOutput(MYSQL_SERVER_URL, mysql_user, mysql_password, mysql_db, MYSQLDB_RESULTS_TABLE_NAME, output_file);
			System.out.println("SaveHITResultsToDB - Done");

		}
		catch (Exception ex) {
			ex.printStackTrace(); 
		}
	}

}
